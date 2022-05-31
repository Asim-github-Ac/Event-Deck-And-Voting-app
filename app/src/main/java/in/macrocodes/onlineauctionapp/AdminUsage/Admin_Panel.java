package in.macrocodes.onlineauctionapp.AdminUsage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import in.macrocodes.onlineauctionapp.AddEvents;
import in.macrocodes.onlineauctionapp.MainActivity;
import in.macrocodes.onlineauctionapp.R;
import in.macrocodes.onlineauctionapp.SettingsActivity;
import in.macrocodes.onlineauctionapp.addProductforBid;

public class Admin_Panel extends AppCompatActivity {

    Button btn,logout,addcanidate,addevents,addresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        btn=findViewById(R.id.stdapprove);
        logout=findViewById(R.id.logout);
        addcanidate=findViewById(R.id.Addcadidate);
        addevents=findViewById(R.id.addevents);
        addresult=findViewById(R.id.addresult);


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
    }
}