package in.macrocodes.onlineauctionapp.AdminUsage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

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
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(StudentList.this, "Record No Found", Toast.LENGTH_SHORT).show();
                }else {
                    List<UserData> userData=queryDocumentSnapshots.toObjects(UserData.class);
                    userDataList.addAll(userData);
                    approveListAdapter=new ApproveListAdapter(getApplicationContext(),userDataList);
                    recyclerView.setAdapter(approveListAdapter);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentList.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}