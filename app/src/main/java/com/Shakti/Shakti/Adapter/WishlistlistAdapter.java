package com.Shakti.Shakti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WishlistlistAdapter extends RecyclerView.Adapter<WishlistlistAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;
    String type="";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView discription,title,date;
        RelativeLayout main_relative;
        CircleImageView imagevideo;



        public MyViewHolder(View view) {
            super(view);

            imagevideo =  view.findViewById(R.id.imagevideo);
            discription =  view.findViewById(R.id.discription);
            title =  view.findViewById(R.id.title);
            main_relative =  view.findViewById(R.id.main_relative);
            date =  view.findViewById(R.id.date);

        }
    }

    public WishlistlistAdapter(ArrayList<Datares> transactionsList, Context mContext,String type) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
        this.type = type;
    }

    @Override
    public WishlistlistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wishkarolist_adapter, parent, false);

        return new WishlistlistAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final WishlistlistAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);



        if(type.equalsIgnoreCase("Notification")){


            holder.discription.setText("" + operator.getDate());
            holder.title.setText("" + operator.getNotification());
            holder.date.setVisibility(View.GONE);
            holder.imagevideo.setVisibility(View.GONE);



        }else {



            holder.discription.setText("" + operator.getWish());
            holder.title.setText("" + operator.getUserName());
            holder.date.setText("" + operator.getCreatedDate());
            RequestOptions options = new RequestOptions();
            Glide.with(mContext)
                    .load(operator.getPhoto())
                    .into(holder.imagevideo);

        }



        holder.main_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });


    }



    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}