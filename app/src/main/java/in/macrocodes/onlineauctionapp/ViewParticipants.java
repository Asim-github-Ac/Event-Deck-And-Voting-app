package in.macrocodes.onlineauctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import in.macrocodes.onlineauctionapp.Adapter.Participants_Adapter;
import in.macrocodes.onlineauctionapp.Models.Parti_Model;

public class ViewParticipants extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Parti_Model> parti_model=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_participants);
        recyclerView=findViewById(R.id.recy);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getParti();

    }
    public void getParti(){
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();


        firestore.collection("Participants").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(ViewParticipants.this, "Record Not Found", Toast.LENGTH_SHORT).show();
                }else {
                    List<Parti_Model> parti_models=queryDocumentSnapshots.toObjects(Parti_Model.class);
                    parti_model.addAll(parti_models);
                    recyclerView.setAdapter(new Participants_Adapter(getApplicationContext(),parti_model));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}