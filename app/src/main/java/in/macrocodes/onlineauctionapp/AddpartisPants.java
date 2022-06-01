package in.macrocodes.onlineauctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import in.macrocodes.onlineauctionapp.Models.Parti_Model;

public class AddpartisPants extends AppCompatActivity {

    EditText name,email,reg;
    Button btnaddd;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpartis_pants);
        name=findViewById(R.id.partiname);
        email=findViewById(R.id.partiemail);
        reg=findViewById(R.id.partireg);
        btnaddd=findViewById(R.id.addNow);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Adding Now");
        progressDialog.setTitle("Wait.................");
        progressDialog.setCancelable(false);


        btnaddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                AddPartiMethod(name.getText().toString(),email.getText().toString(),reg.getText().toString());

            }
        });

    }
    public void AddPartiMethod(String name,String email,String regis){
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        Parti_Model PM=new Parti_Model(name,email,regis);

        firestore.collection("Participants").add(PM).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                progressDialog.dismiss();
                Toast.makeText(AddpartisPants.this, "Participants Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(AddpartisPants.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}