package in.macrocodes.onlineauctionapp.AdminUsage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import in.macrocodes.onlineauctionapp.AddEvents;
import in.macrocodes.onlineauctionapp.HomeActivity;
import in.macrocodes.onlineauctionapp.MainActivity;
import in.macrocodes.onlineauctionapp.MyCampaigns;
import in.macrocodes.onlineauctionapp.R;
import in.macrocodes.onlineauctionapp.SettingsActivity;
import in.macrocodes.onlineauctionapp.SharedPrefrence.PrefManager;
import in.macrocodes.onlineauctionapp.Student_Approve;
import in.macrocodes.onlineauctionapp.ViewParticipants;
import in.macrocodes.onlineauctionapp.addProductforBid;

public class Admin_Panel extends AppCompatActivity {

    Button btn,logout,addcanidate,addevents,addresult;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        btn=findViewById(R.id.stdapprove);
        logout=findViewById(R.id.logout);
        addcanidate=findViewById(R.id.Addcadidate);
        addevents=findViewById(R.id.addevents);
        addresult=findViewById(R.id.addresult);
        drawerLayout=findViewById(R.id.drawer);
        prefManager=new PrefManager(this);
        navigationView=findViewById(R.id.navviewstd);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        prefManager.setToken_Email("Admin");
      //  this.getActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        addresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),AddResult_Activity.class));

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent =new Intent(Admin_Panel.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addcanidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), addProductforBid.class));
            }
        });
        addevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddEvents.class));
            }
        }); 
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),StudentList.class);
                startActivity(intent);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.viewevents:
                        Intent intent=new Intent(getApplicationContext(), MyCampaigns.class);
                        startActivity(intent);
                        break;
                    case R.id.viewparti:

                        Intent mainIntent = new Intent(Admin_Panel.this, ViewParticipants.class);
                        startActivity(mainIntent);

                        break;
                }
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            switch (item.getItemId()){
                case R.id.viewevents:
                    Intent intent=new Intent(getApplicationContext(),MyCampaigns.class);
                    startActivity(intent);

                    break;
                case R.id.viewparti:
                    Intent mainIntent = new Intent(Admin_Panel.this, ViewParticipants.class);
                    mainIntent.putExtra("key","Admin");
                    startActivity(mainIntent);
//                    prefManager.setToken_Email("Student");

                    break;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        prefManager.setToken_Email("Admin");
    }
}