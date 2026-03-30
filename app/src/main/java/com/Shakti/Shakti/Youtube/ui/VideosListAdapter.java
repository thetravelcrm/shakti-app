package com.Shakti.Shakti.Youtube.ui;

import android.content.Context;
import android.content.Intent;
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

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public  class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView discription;
        RelativeLayout main_relative;
        ImageView imagevideo;



        public MyViewHolder(View view) {
            super(view);

            imagevideo =  view.findViewById(R.id.imagevideo);
            discription =  view.findViewById(R.id.discription);
            main_relative =  view.findViewById(R.id.main_relative);

        }
    }

    public VideosListAdapter(ArrayList<Datares> transactionsList, Context mContext) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
    }

    @Override
    public VideosListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videolist_adapter, parent, false);

        return new VideosListAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final VideosListAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);

          holder.discription.setText("" + operator.getTitle());

         Glide.with(mContext)
                .load(operator.getThumbnail())
                .into(holder.imagevideo);


        holder.main_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i=new Intent(mContext,PlayVideo.class);
                i.putExtra("videoid",""+operator.getUrl().replace("https://www.youtube.com/watch?v=",""));
                i.putExtra("discriptiom",""+operator.getTitle());
                mContext.startActivity(i);


            }
        });





    }



    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}