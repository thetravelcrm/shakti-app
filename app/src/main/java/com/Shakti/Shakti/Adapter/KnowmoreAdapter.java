package com.Shakti.Shakti.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.appevents.ml.Utils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class KnowmoreAdapter extends RecyclerView.Adapter<KnowmoreAdapter.MyViewHolder> {

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

    public KnowmoreAdapter(ArrayList<Datares> transactionsList, Context mContext, String type) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
        this.type = type;
    }

    @Override
    public KnowmoreAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.knowmore_adapter, parent, false);

        return new KnowmoreAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final KnowmoreAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);

            holder.title.setText("" + operator.getName());


            if(operator.getPdf().endsWith(".pdf")){
                holder.imagevideo.setVisibility(View.VISIBLE);


            }else {

                holder.imagevideo.setVisibility(View.INVISIBLE);


            }



        holder.imagevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContext. startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(operator.getPdf())));


            }
        });


    }



    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}