package in.macrocodes.onlineauctionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.macrocodes.onlineauctionapp.Models.Parti_Model;
import in.macrocodes.onlineauctionapp.R;

public class Participants_Adapter extends RecyclerView.Adapter<Participants_Adapter.myHolder> {
    Context context;
    List<Parti_Model> list;

    public Participants_Adapter(Context context, List<Parti_Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Participants_Adapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parti_item,parent,false);
        return new Participants_Adapter.myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Participants_Adapter.myHolder holder, int position) {

        Parti_Model parti_model=list.get(position);
        holder.tvname.setText("Name : "+parti_model.getName());
        holder.tvemail.setText("Email : "+parti_model.getEmail());
        holder.tvreg.setText("Reg : "+parti_model.getRegistrationNo());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {
        TextView tvname,tvemail,tvreg;

        public myHolder(@NonNull View itemView) {
            super(itemView);
            tvemail=itemView.findViewById(R.id.itememail);
            tvname=itemView.findViewById(R.id.itemname);
            tvreg=itemView.findViewById(R.id.itemreg);

        }
    }
}
