package in.macrocodes.onlineauctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import in.macrocodes.onlineauctionapp.AdminUsage.Admin_Panel;
import in.macrocodes.onlineauctionapp.SharedPrefrence.PrefManager;

public class LoginActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    private EditText mLoginEmail;
    private EditText mLoginPassword;
    ProgressDialog loadingBar;
    private Button mLogin_btn,mSign_up,forgetpassword;
    private Context mContext;
    String usertype;
    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;
    AlertDialog dialog_verifying,profile_dialog;
    private ProgressDialog mRegProgress;
    private DatabaseReference mUserDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent=getIntent();
        usertype=intent.getStringExtra("user");
        mAuth = FirebaseAuth.getInstance();

        mLogin_btn=(Button)findViewById(R.id.lg_login);
        mSign_up=(Button)findViewById(R.id.lg_signup);

        forgetpassword=findViewById(R.id.forgetpass);
        mLoginEmail=(EditText)findViewById(R.id.lg_email);
        mRegProgress = new ProgressDialog(LoginActivity.this);
        mLoginPassword=(EditText)findViewById(R.id.lg_pass);

        mContext = this;


        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });
        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=mLoginEmail.getText().toString().trim();
                String password=mLoginPassword.getText().toString().trim();


//                LayoutInflater inflater = getLayoutInflater();
//                View alertLayout= inflater.inflate(R.layout.profile_create_dialog,null);
//                AlertDialog.Builder show = new AlertDialog.Builder(mContext);
//                show.setView(alertLayout);
//                show.setCancelable(false);
//                dialog_verifying = show.create();
//                dialog_verifying.show();



                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(mContext, "You can't leave fields empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    mRegProgress.setTitle("Logging in");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    loginUser(email,password);
                }
            }
        });

        mSign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });





        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    //Log.e("rg", "onComplete: Failed=" + Objects.requireNonNull(task.getException()).getMessage());

                    String current_user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                mUserDatabase.child(current_user_id).child("device_token").setValue(Objects.requireNonNull(task.getResult()).getToken()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        // dialog_verifying.cancel();
                                        //dialog_verifying = null;
                                        mRegProgress.dismiss();
                                        if (usertype.equals("Student")) {
                                            PrefManager prefManager=new PrefManager(getApplicationContext());
                                            prefManager.setToken_Email("Student");
                                            Intent mainIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                            mainIntent.putExtra("id", current_user_id);
                                            startActivity(mainIntent);
                                            finish();
                                        }else if (usertype.equals("Admin")){
                                            PrefManager prefManager=new PrefManager(getApplicationContext());
                                            prefManager.setToken_Email("Admin");
                                            Intent mainIntent = new Intent(LoginActivity.this, Admin_Panel.class);
                                            mainIntent.putExtra("id", current_user_id);
                                            startActivity(mainIntent);
                                            finish();
                                        }

                                    }
                                });

                            } else {
                                Log.e("TAG", "exception=" + Objects.requireNonNull(task.getException()).toString());
                                Toast.makeText(LoginActivity.this, "Error - " +task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


            }
        });
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        FirebaseDatabase.getInstance().goOnline();
    }
    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailet= new EditText(this);

        // write the email using which you registered
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void beginRecovery(String email) {
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,"Done sent",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error Occured",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this,"Error Failed"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}