package in.macrocodes.onlineauctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.macrocodes.onlineauctionapp.Adapter.Get_Votes;
import in.macrocodes.onlineauctionapp.Adapter.pInfo_AllImageView;
import in.macrocodes.onlineauctionapp.Models.EventModels;
import in.macrocodes.onlineauctionapp.Models.VoteModel;

public class ProductInfoActivity extends AppCompatActivity  implements View.OnClickListener {
    String name,desc,bid,uid,mine,status;
    pInfo_AllImageView mAdapter;
    TextView pname,pdesc,sellername,totalvotes;
    ImageView imageView;
    TextView rate,sellercity;
    CircleImageView sellerImage;
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    List<String> imageList = new ArrayList<>();
    RecyclerView bidView;
    List<EventModels> biddingList = new ArrayList<>();
    List<VoteModel> voteModels=new ArrayList<>();
    EditText bidtext;
    Get_Votes mAdapter2;
    Button bidBtn;
    TextView title;
    LinearLayout biddingLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        pdesc=findViewById(R.id.desis);
        progressDialog=new ProgressDialog(this);
        sellername = (TextView) findViewById(R.id.SellerName);
        totalvotes=findViewById(R.id.totalvotes);
        sellercity = (TextView) findViewById(R.id.SellerCity);
        sellerImage = (CircleImageView) findViewById(R.id.sellerProfile);
        title = (TextView) findViewById(R.id.title);
        firebaseFirestore=FirebaseFirestore.getInstance();
        biddingLayout = (LinearLayout) findViewById(R.id.bidLayout);
        progressDialog=new ProgressDialog(this);
        bidBtn = (Button) findViewById(R.id.bidbtn);
        bidtext = (EditText) findViewById(R.id.bidtxt);
        bidView =(RecyclerView) findViewById(R.id.bidView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        imageView = (ImageView) findViewById(R.id.pImage);
        rate = (TextView) findViewById(R.id.pbid);
        pname = (TextView) findViewById(R.id.pname);
      //  mAdapter2 = new LiveBidding(ProductInfoActivity.this,biddingList);
       // bidView.setAdapter(mAdapter2);
        getPreInfo();
        getSellerInfo();
        getData();
        GetVotes(name);
        bidBtn.setOnClickListener(this);
    }
    private void getBidding(){
        biddingList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products")
                .child(name).child("bidding");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        reference.child(Objects.requireNonNull(dataSnapshot.getKey())).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                EventModels eventModels = snapshot.getValue(EventModels.class);
                                biddingList.add(eventModels);
                                bidView.scrollToPosition(biddingList.size() - 1);
                                mAdapter2.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getSellerInfo(){
        System.out.println("user id is _______________"+uid);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String name = snapshot.child("name").getValue().toString();
//                String profile = snapshot.child("image").getValue().toString();
//              //  String city = snapshot.child("city").getValue().toString();
//                sellername.setText(name);
//              //  sellercity.setText("From "+city);
//                Glide.with(Objects.requireNonNull(ProductInfoActivity.this))
//                        .load(profile)
//                        .diskCacheStrategy(DiskCacheStrategy.DATA)
//                        .placeholder(R.drawable.default_avatar)
//                        .into(sellerImage);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getData(){
        imageList.clear();
        System.out.println("data is ________________"+name);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Selected_Canidate").child(name);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url=snapshot.child("image").getValue(String.class);
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.default_send_image)
                        .into(imageView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error is __________________getdata"+error.getMessage());
            }
        });
    }
    private void getPreInfo(){
        name = getIntent().getStringExtra("pname");
        desc = getIntent().getStringExtra("pdesc");
        bid = getIntent().getStringExtra("prate");
        uid = getIntent().getStringExtra("uid");
        status = getIntent().getStringExtra("status");
        mine = getIntent().getStringExtra("mine");
        sellername.setText(name);
        if (mine!=null){
           // bidNow.setText("View Bidding of Your Product");
        }
        pname.setText(name);
        pdesc.setText(desc);
        rate.setText("Bidding Starts at Rs "+bid);
    }

    public void CastVote(String cname,String castername,String usesid){
        VoteModel voteModel=new VoteModel(cname,castername,usesid);
        firebaseFirestore.collection("Candidate Total Votes").document("data").collection(cname).add(voteModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                startActivity(new Intent(ProductInfoActivity.this,HomeActivity.class));
                progressDialog.cancel();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("error is ___________in productinfo"+e.getMessage());
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bidbtn:
                CastVote(name,bidtext.getText().toString(),uid);
                progressDialog.setTitle("Progress");
                progressDialog.setMessage("Loading..........");
                progressDialog.show();
                break;
        }
    }
    public void GetVotes(String cname){
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        bidView.setLayoutManager(linearLayoutManager2);
        progressDialog.setTitle("Progress");
        progressDialog.setMessage("Loadings........");
        progressDialog.show();
        progressDialog.setCancelable(false);
        firebaseFirestore.collection("Candidate Total Votes").document("data").collection(cname).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    progressDialog.cancel();
                }else {
                    List<VoteModel> voteModels=queryDocumentSnapshots.toObjects(VoteModel.class);
                    mAdapter2 = new Get_Votes(ProductInfoActivity.this,voteModels);
                     bidView.setAdapter(mAdapter2);
                     progressDialog.cancel();
                     totalvotes.setText("Total Votes:"+voteModels.size());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("get votes error is __________________"+e.getMessage());
                progressDialog.cancel();
            }
        });

    }
}