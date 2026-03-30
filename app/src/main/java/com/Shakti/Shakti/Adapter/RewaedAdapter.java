package com.Shakti.Shakti.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.Dashbord.ui.RequesttechexpressActivity;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Setting.RewardRedeemActivity;
import com.Shakti.Shakti.Setting.WishkaroActivity;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.Shakti.Shakti.Youtube.ui.PlayVideo;
import com.Shakti.Shakti.Youtube.ui.VideosListActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RewaedAdapter extends RecyclerView.Adapter<RewaedAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView discription,title,redeempay,pointt;
         ImageView imagevideo;



        public MyViewHolder(View view) {
            super(view);

            imagevideo =  view.findViewById(R.id.imagevideo);
            discription =  view.findViewById(R.id.discription);
            pointt =  view.findViewById(R.id.pointt);
            title =  view.findViewById(R.id.title);
             redeempay =  view.findViewById(R.id.redeempay);

        }
    }

    public RewaedAdapter(ArrayList<Datares> transactionsList, Context mContext) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
    }

    @Override
    public RewaedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_adapter, parent, false);

        return new RewaedAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final RewaedAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);


        SharedPreferences myPreferences = mContext.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String setremaining = ""+myPreferences.getString(ApplicationConstant.INSTANCE.remaining, null);

     int redempiny= Integer.parseInt(setremaining);
     int getPoints= Integer.parseInt(operator.getPoints());

     if(redempiny>getPoints){

         holder.redeempay.setVisibility(View.VISIBLE);


     }else {

         holder.redeempay.setVisibility(View.GONE);

     }




        //  holder.date.setText("" + operator.getEventDate());
        holder.discription.setText("" + operator.getDescription());
         holder.title.setText("" + operator.getTitle());
         holder.pointt.setText("Points - " + operator.getPoints());

         RequestOptions options = new RequestOptions();
        Glide.with(mContext)
                .load(operator.getPhoto())
                  .into(holder.imagevideo);

        holder.redeempay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i=new Intent(mContext, RewardRedeemActivity.class);
                i.putExtra("redeempayid",""+operator.getId());
                i.putExtra("Description",""+ operator.getDescription());
                i.putExtra("Title",""+operator.getTitle());
                i.putExtra("image",""+operator.getPhoto());
                i.putExtra("pointsreed",""+operator.getPoints());
                 mContext.startActivity(i);


            }
        });

    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}