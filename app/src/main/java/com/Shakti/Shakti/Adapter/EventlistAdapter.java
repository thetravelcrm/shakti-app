package com.Shakti.Shakti.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Youtube.ui.PlayVideo;
import com.Shakti.Shakti.Youtube.ui.VideosListAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class EventlistAdapter extends RecyclerView.Adapter<EventlistAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView discription,title;
        RelativeLayout main_relative;
        ImageView imagevideo;
        CircleImageView attachment;



        public MyViewHolder(View view) {
            super(view);

            attachment =  view.findViewById(R.id.attachment);
            imagevideo =  view.findViewById(R.id.imagevideo);
            discription =  view.findViewById(R.id.discription);
            title =  view.findViewById(R.id.title);
            main_relative =  view.findViewById(R.id.main_relative);

        }
    }

    public EventlistAdapter(ArrayList<Datares> transactionsList, Context mContext) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
    }

    @Override
    public EventlistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eventlist_adapter, parent, false);

        return new EventlistAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final EventlistAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);

     //   Log.e("getAttachment","getAttachment  :  "+ operator.getAttachment());

        holder.discription.setText("" + operator.getDescription());
         holder.title.setText("" + operator.getTitle());


         if(operator.getAttachment().endsWith(".pdf")){


             holder.attachment.setVisibility(View.VISIBLE);
         }else {

             holder.attachment.setVisibility(View.INVISIBLE);


         }


        RequestOptions options = new RequestOptions();
        Glide.with(mContext)
                .load(operator.getPhoto())
                  .into(holder.imagevideo);

        holder.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContext. startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(operator.getAttachment())));

            }
        });

    }



    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}