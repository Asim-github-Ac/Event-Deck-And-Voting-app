package in.macrocodes.onlineauctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.macrocodes.onlineauctionapp.Adapter.AndroidDataAdapter;
import in.macrocodes.onlineauctionapp.Adapter.MyCampaignAdapter;
import in.macrocodes.onlineauctionapp.Models.AddEventModel;
import in.macrocodes.onlineauctionapp.Models.Products;

public class MyCampaigns extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    ImageView imageView;
    ProgressDialog progressDialog;
    ArrayList<AddEventModel> addEventModelslist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_campaigns);
        mRecyclerView = (RecyclerView) findViewById(R.id.myCamp);
        imageView=findViewById(R.id.addevent);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading Events");
        progressDialog.setMessage("Loadings......");
        progressDialog.show();
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mAdapter = new MyCampaignAdapter(MyCampaigns.this,myProducts);
//        mRecyclerView.setAdapter(mAdapter);
        GetEvents();
        imageView.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addevent:
                startActivity(new Intent(getApplicationContext(),AddEvents.class));
                break;
        }
    }
    public void GetEvents(){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TotalEvents").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(MyCampaigns.this, "No Event Were Found", Toast.LENGTH_SHORT).show();
                }else{
                    List<AddEventModel> addEventModels=queryDocumentSnapshots.toObjects(AddEventModel.class);
                    addEventModelslist.addAll(addEventModels);
                    progressDialog.dismiss();
                    AndroidDataAdapter mAdapter = new AndroidDataAdapter(getApplicationContext(), addEventModelslist);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
            }
        });

    }
}