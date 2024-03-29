package in.macrocodes.onlineauctionapp.AdminUsage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.macrocodes.onlineauctionapp.Adapter.AdapterClass;
import in.macrocodes.onlineauctionapp.Models.Status;
import in.macrocodes.onlineauctionapp.Models.UserData;
import in.macrocodes.onlineauctionapp.R;

public class ApproveListAdapter extends RecyclerView.Adapter<ApproveListAdapter.myHolder> {

    Context context;
    List<UserData> userDataList;

    public ApproveListAdapter(Context context, List<UserData> userDataList) {
        this.context = context;
        this.userDataList = userDataList;
    }

    @NonNull
    @Override
    public ApproveListAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approve_item,parent,false);
        return new ApproveListAdapter.myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveListAdapter.myHolder holder, int position) {

        UserData userData=userDataList.get(position);
        holder.name.setText("Name : "+userData.getName());
        holder.email.setText("Email : "+userData.getEmail());
        holder.city.setText("City : "+userData.getCity());
        holder.btnapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveOrder(userData.getName(),userData.getName(),userData.getRollnomber());
            }
        });

       holder.rollnumber.setText(userData.getRollnomber());
      //  Picasso.get().load(userData.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {
        TextView name,email,city,rollnumber;
        ImageView imageView;
        Button btnapprove;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameitem);
            email=itemView.findViewById(R.id.emailitem);
            rollnumber=itemView.findViewById(R.id.rolllnumber);
            city=itemView.findViewById(R.id.cityis);
            imageView=itemView.findViewById(R.id.stdimg);
            btnapprove=itemView.findViewById(R.id.approve);
        }

    }
    public void ApproveOrder(String status,String name,String rollno){

        Status status1=new Status("1",name,rollno);
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Status").document("data").collection(rollno).add(status1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "Request Send", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
