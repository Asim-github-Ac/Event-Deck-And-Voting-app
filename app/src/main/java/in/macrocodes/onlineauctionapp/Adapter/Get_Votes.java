package in.macrocodes.onlineauctionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.macrocodes.onlineauctionapp.Models.VoteModel;
import in.macrocodes.onlineauctionapp.ProductInfoActivity;
import in.macrocodes.onlineauctionapp.R;

public class Get_Votes extends RecyclerView.Adapter<Get_Votes.Viewholder> {
    List<VoteModel> biddingList = new ArrayList<>();
    Context mContext;
    public Get_Votes(ProductInfoActivity productInfoActivity, List<VoteModel> biddingList) {
        this.mContext = productInfoActivity;
        this.biddingList=biddingList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.votes_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        VoteModel biddingModal = biddingList.get(position);
        holder.bidTxt.setText(biddingModal.getCastername());
//
    }

    @Override
    public int getItemCount() {
        return biddingList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public static class Viewholder extends RecyclerView.ViewHolder{

        private TextView bidTxt;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            bidTxt = (TextView) itemView.findViewById(R.id.bidtxt);
        }
    }
}
