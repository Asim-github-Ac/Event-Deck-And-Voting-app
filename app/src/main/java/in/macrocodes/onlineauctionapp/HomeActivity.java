package in.macrocodes.onlineauctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import in.macrocodes.onlineauctionapp.Adapter.AdapterClass;
import in.macrocodes.onlineauctionapp.AdminUsage.Admin_Panel;
import in.macrocodes.onlineauctionapp.Models.EventModels;
import in.macrocodes.onlineauctionapp.Models.Products;
import in.macrocodes.onlineauctionapp.SharedPrefrence.PrefManager;

public class HomeActivity extends AppCompatActivity {
    int max = -1;
    String maxUserId = null;
    FloatingActionButton addProduct;
    PrefManager prefManager;
    RecyclerView mRecyclerView;
    AdapterClass mAdapter;
    BottomAppBar bottomAppBar;
    ImageView refresh;
    String vali;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int itemPos = 0;
    private static final int TOTAL_ITEMS_TO_LOAD = 2000;
    private int mCurrentPage = 1;
    private String mLastKey = "";
    private String mPrevKey = "";
    List<Products>allProducts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        prefManager=new PrefManager(this);
        addProduct = (FloatingActionButton) findViewById(R.id.addProduct);
        mRecyclerView = (RecyclerView) findViewById(R.id.productList);


        //prefManager.setToken_Email("Admin");
        refresh = (ImageView) findViewById(R.id.refresh);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        bottomAppBar =  findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        mAdapter = new AdapterClass(HomeActivity.this,allProducts);
        mRecyclerView.setAdapter(mAdapter);



        getAllProducts();
     //   checkProductDate();

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(HomeActivity.this,addProductforBid.class);
//                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllProducts();
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });


    }
    public void checkProductDate(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Selected_Canidate");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    reference.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot2) {
                          try {
                              String uid = snapshot2.child("uid").getValue().toString();
                              if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                  Products products = snapshot2.getValue(Products.class);

                                  assert products != null;
                                  String expireDate= products.getTimestamp();
                                  SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                                  Date date = new Date();
                                  String todayDate = formatter.format(date);

                                  int result = todayDate.compareTo(expireDate);

                                  if (result >= 0){

                                      HashMap<String,Object> hashMap = new HashMap();
                                      hashMap.put("status","stop");
                                      reference.child(products.getName()).updateChildren(hashMap);
                                      getBiddingData(reference,products);

                                  }

                              }
                          }catch (Exception e){

                          }
                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    private void getBiddingData(DatabaseReference reference,Products products) {

        reference.child(products.getName()).child("bidding").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                       EventModels eventModels = dataSnapshot.getValue(EventModels.class);

                        assert eventModels != null;
                        int bid = Integer.parseInt(eventModels.getBid());
                       if (bid>max){
                           max = bid;
                           maxUserId = eventModels.getUid();
                       }
                    }
                }


                HashMap<String,Object> map = new HashMap<>();
                map.put("bid",max);
                map.put("uid",maxUserId);

                reference.child(products.getName()).child("winner").updateChildren(map);

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    public void getAllProducts(){

        allProducts.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Selected_Canidate");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ref.child(Objects.requireNonNull(dataSnapshot.getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Products products = dataSnapshot.getValue(Products.class);
                            allProducts.add(products);
                            mAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        ref.keepSynced(true);
//
//        Query messageQuery = ref.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
//
//
//        messageQuery.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                Products products = dataSnapshot.getValue(Products.class);
//                 allProducts.add(products);
//                 mAdapter.notifyDataSetChanged();
//
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.bottom_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.winnername:
                Intent i2 = new Intent(HomeActivity.this,Anouncement.class);
                startActivity(i2);
                return true;
            case R.id.mycamp:
                Intent intent2 = new Intent(HomeActivity.this,MyCampaigns.class);
                startActivity(intent2);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onStart() {
        super.onStart();
        System.out.println("user type is +++++++"+prefManager.getToken_Email());
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().goOnline();
        if(currentUser == null){
            Intent startIntent = new Intent(this, MainActivity.class);
            startActivity(startIntent);
            finish();

        }else if (prefManager.getToken_Email().equals("Admin")){
            Intent intent=new Intent(getApplicationContext(), Admin_Panel.class);
            startActivity(intent);
        }
        else if (prefManager.getToken_Email().equals("Student")){

            final DatabaseReference mUserDatabase = FirebaseDatabase.getInstance()
                    .getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot != null) {

                        mUserDatabase.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}