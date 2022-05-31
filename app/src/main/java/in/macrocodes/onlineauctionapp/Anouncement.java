package in.macrocodes.onlineauctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import in.macrocodes.onlineauctionapp.Models.ResultModel;

public class Anouncement extends AppCompatActivity {

    TextView tvname,tvemail,tvper;
    List<ResultModel> resultModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anouncement);
        tvemail=findViewById(R.id.cani_email);
        tvname=findViewById(R.id.cani_name);
        tvper=findViewById(R.id.cani_perfomace);
        resultModels=new ArrayList<>();

        GetWinners();

    }
    public void GetWinners(){
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();

        firestore.collection("Candidate Total Votes").document("finalresult").collection("name").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(Anouncement.this, "Record Not Found", Toast.LENGTH_SHORT).show();
                }else {
                    List<ResultModel> resultModels1=queryDocumentSnapshots.toObjects(ResultModel.class);
                    resultModels.addAll(resultModels1);
                    tvname.setText("Name : "+resultModels.get(0).getName());
                    tvemail.setText("Email :"+resultModels.get(0).getEmail());
                    tvper.setText("Perfomance :"+resultModels1.get(0).getPerofmance());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Anouncement.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}