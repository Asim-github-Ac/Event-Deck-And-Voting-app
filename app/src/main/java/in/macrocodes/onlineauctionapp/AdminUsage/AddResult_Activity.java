package in.macrocodes.onlineauctionapp.AdminUsage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import in.macrocodes.onlineauctionapp.Models.ResultModel;
import in.macrocodes.onlineauctionapp.R;

public class AddResult_Activity extends AppCompatActivity {

    TextInputLayout tvname,tvemail,tvvotes;
    Button btnresult;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_result);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Add Result");
        progressDialog.setMessage("Loading.......");
        progressDialog.setCancelable(false);
        tvname=findViewById(R.id.canidatename);
        tvemail=findViewById(R.id.canidate_email);
        tvvotes=findViewById(R.id.enter_total_votes);
        btnresult=findViewById(R.id.send_result);

        btnresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                SendResults(tvname.getEditText().getText().toString().trim(),tvemail.getEditText().getText().toString().trim(),tvvotes.getEditText().getText().toString().trim());
            }
        });





    }
    public void SendResults(String name,String email,String perfomance){

        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        ResultModel resultModel=new ResultModel(name,email,perfomance);
        firestore.collection("Candidate Total Votes").document("finalresult").collection("name").add(resultModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),Admin_Panel.class));
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(AddResult_Activity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(AddResult_Activity.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}