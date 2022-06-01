package in.macrocodes.onlineauctionapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.macrocodes.onlineauctionapp.AddpartisPants;
import in.macrocodes.onlineauctionapp.Models.AddEventModel;
import in.macrocodes.onlineauctionapp.R;

public class AndroidDataAdapter extends RecyclerView.Adapter<AndroidDataAdapter.ViewHolder> {
    private ArrayList<AddEventModel> arrayList;
    private Context mcontext;
    public static int position;
    public Calendar calendar;
    DateFormat df;
    public static String name;
    boolean selected=false;
    public static String id;


    public AndroidDataAdapter(Context context, ArrayList<AddEventModel> android) {
        this.arrayList = android;
        this.mcontext = context;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = arrayList.get(position).name;
                Log.d("imagename",name);
                if(selected){
                    holder.imageView.clearColorFilter();
                    selected=false;
                }
                else{
                    ColorFilter cf = new PorterDuffColorFilter(Color.RED,PorterDuff.Mode.SCREEN);

                    holder.imageView.setColorFilter(cf);
                    selected=true;
                }
            }
        });

        Picasso.get().load(arrayList.get(i).image).into(holder.imageView);
        holder.eventText.setText("Event Name : "+arrayList.get(i).name);
        holder.timeText.setText("Start Date : "+arrayList.get(i).getStartTime());

        holder.userText.setText("Description : "+arrayList.get(i).getDescription());
        holder.department.setText("Departments : "+arrayList.get(i).getDepartment());
        holder.semster.setText("Semster : "+arrayList.get(i).getSemester());
//
        df =  new SimpleDateFormat("HH:mm");

//
        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mcontext,AddpartisPants.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });
        holder.eventText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                Intent intent=new Intent(mcontext, AddpartisPants.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup vGroup, int i) {

        View view = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.total_events, vGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView eventText,timeText,userText,department,semster;

        Button btnadd;
        private ImageView imageView;

        public ViewHolder(View v) {
            super(v);

            eventText = (TextView) v.findViewById(R.id.eventname);
            timeText = (TextView) v.findViewById(R.id.eventdate);

            btnadd=v.findViewById(R.id.addparti);
            userText = (TextView) v.findViewById(R.id.eventdescription);

            imageView = (ImageView) v.findViewById(R.id.eventpic);
            department=v.findViewById(R.id.departmentitem);
            semster=v.findViewById(R.id.semsteritem);




        }
    }


}
