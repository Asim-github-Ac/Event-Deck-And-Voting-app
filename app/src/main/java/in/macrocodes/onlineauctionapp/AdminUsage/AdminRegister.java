package in.macrocodes.onlineauctionapp.AdminUsage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import in.macrocodes.onlineauctionapp.R;
import in.macrocodes.onlineauctionapp.RegisterActivity;
import in.macrocodes.onlineauctionapp.SharedPrefrence.PrefManager;

public class AdminRegister extends AppCompatActivity {

    EditText name,email,pass,uniserial;
    Button regbtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        name=findViewById(R.id.admin_regname);
        email=findViewById(R.id.admin_regemail);
        pass=findViewById(R.id.admin_reg_pass);
        uniserial=findViewById(R.id.admin_serial);
        regbtn=findViewById(R.id.adminregister);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("progress");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading......");

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Signup();
            }
        });
    }
    public void Signup(){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent mainIntent = new Intent(AdminRegister.this, Admin_Panel.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                    PrefManager prefManager=new PrefManager(getApplicationContext());
                    prefManager.setToken_Email("Admin");
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(AdminRegister.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminRegister.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
}