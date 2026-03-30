package com.Shakti.Shakti.ReportAll.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;

import java.util.ArrayList;


public class PointStatusReedemAgentAdapter extends RecyclerView.Adapter<PointStatusReedemAgentAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
         public  TextView Date,quantity,product,credit ;


        public MyViewHolder(View view) {
            super(view);
            Date =  view.findViewById(R.id.Date);
            quantity =  view.findViewById(R.id.quantity);
            credit =  view.findViewById(R.id.credit);
            product =  view.findViewById(R.id.product);

        }
    }

    public PointStatusReedemAgentAdapter(ArrayList<Datares> transactionsList, Context mContext) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
    }

    @Override
    public PointStatusReedemAgentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.point_eren_agent_adapter, parent, false);

        return new PointStatusReedemAgentAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final PointStatusReedemAgentAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);

        holder.Date.setText("" + operator.getDate());
        holder.credit.setText("" + operator.getCredit());
        holder.product.setText("" + operator.getProduct());
         holder.quantity.setText("" + operator.getQuantity());


      /*  if(operator.getDeliveryStatus().equalsIgnoreCase("pending")){

            holder.Status.setTextColor(mContext.getResources().getColorStateList(R.color.white));
            holder.Status.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.yallow));

        }else if(operator.getDeliveryStatus().equalsIgnoreCase("active")){

            holder.Status.setTextColor(mContext.getResources().getColorStateList(R.color.lightwite));

        }*/


    }
    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}