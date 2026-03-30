package com.Shakti.Shakti.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;
    String page="";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView answer,question,CreatedDate;



        public MyViewHolder(View view) {
            super(view);

            answer =  view.findViewById(R.id.answer);
            question =  view.findViewById(R.id.question);
            CreatedDate =  view.findViewById(R.id.CreatedDate);
        }
    }

    public FaqAdapter(ArrayList<Datares> transactionsList, Context mContext,String page) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
        this.page = page;
    }

    @Override
    public FaqAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faq_adapter, parent, false);

        return new FaqAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FaqAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);

        if(page.equalsIgnoreCase("1")){

            holder.answer.setText("" + operator.getAns());
            holder.question.setText("" + operator.getQns());


        }else if(page.equalsIgnoreCase("3")){

            holder.answer.setText("" + operator.getCreatedDate());
            holder.question.setText("" + operator.getWish());


        }else {


            holder.answer.setText("" + operator.getDescription());
            holder.question.setText("" + operator.getCategoryName());
            holder.CreatedDate.setText("" + operator.getCreatedDate());

            holder.CreatedDate.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }
}