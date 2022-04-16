package in.macrocodes.onlineauctionapp.AdminUsage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import in.macrocodes.onlineauctionapp.Models.UserData;
import in.macrocodes.onlineauctionapp.R;

public class StudentList extends AppCompatActivity {

    List<UserData> userDataList=new ArrayList<>();
    ApproveListAdapter approveListAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        recyclerView=findViewById(R.id.stdrecyc);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        GetData();


    }
    public void GetData(){

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData= snapshot.getValue(UserData.class);
                userDataList.add(userData);
                approveListAdapter=new ApproveListAdapter(getApplicationContext(),userDataList);
                recyclerView.setAdapter(approveListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentList.this, "student list "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}