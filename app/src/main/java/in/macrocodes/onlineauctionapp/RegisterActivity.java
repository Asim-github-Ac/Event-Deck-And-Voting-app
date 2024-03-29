package in.macrocodes.onlineauctionapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import in.macrocodes.onlineauctionapp.AdminUsage.Admin_Panel;
import in.macrocodes.onlineauctionapp.Models.UserData;
import in.macrocodes.onlineauctionapp.SharedPrefrence.PrefManager;

public class RegisterActivity extends AppCompatActivity {

    AlertDialog dialog_verifying, profile_dialog;
    private EditText mDisplayName, mEmail, mPassword, mCity,addrollno;
    private Button mCreateBtn;
    private DatabaseReference mDatabase;
    private final int i = 0;
    String usertype;
    //ProgressDialog
    private ProgressDialog mRegProgress;
    //Firebase Auth
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Intent intent=getIntent();
        usertype=intent.getStringExtra("user");
        mRegProgress = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mRegProgress = new ProgressDialog(this);
        mDisplayName = (EditText) findViewById(R.id.reg_name);
        mEmail = (EditText) findViewById(R.id.reg_email);
        mPassword = (EditText) findViewById(R.id.reg_pass);
        mCity = (EditText) findViewById(R.id.reg_city);
        mCreateBtn = (Button) findViewById(R.id.register);
        addrollno=findViewById(R.id.addroll_Number);
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String display_name=mDisplayName.getText().toString();
                final String  email=mEmail.getText().toString();
                final String password=mPassword.getText().toString();
                final String city=mCity.getText().toString();
                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name, email, password, city);

                }
            }
        });
        // Android Fields
    }
    private void register_user(final String display_name, final String email, String password, final String city) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // dialog_verifying.dismiss();
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    assert current_user != null;
                    String uid = current_user.getUid();
                    UserData userData = new UserData(display_name,email,city,"default",uid,addrollno.getText().toString());
                    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                    mDatabase.child(uid).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                if (usertype.equals("Student")) {
                                   firebaseFirestore.collection("Users").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                       @Override
                                       public void onSuccess(DocumentReference documentReference) {
                                           PrefManager prefManager=new PrefManager(getApplicationContext());
                                           prefManager.setToken_Email("Student");
                                           Intent mainIntent = new Intent(RegisterActivity.this, Student_Approve.class);
                                           mainIntent.putExtra("approve",addrollno.getText().toString());
                                           mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                           startActivity(mainIntent);
                                           finish();
                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(RegisterActivity.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                   });
                                }else if(usertype.equals("Admin")){

                                }

                            }

                        }
                    });

                } else {
                    String task_result = task.getException().getMessage().toString();
                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this, task_result, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}