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


public class PointStatusAgentAdapter extends RecyclerView.Adapter<PointStatusAgentAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
         public  TextView Date,Status,Ordernumber,ProductName,Quantity,RedeemPoints ;


        public MyViewHolder(View view) {
            super(view);
            Status =  view.findViewById(R.id.Status);
            Date =  view.findViewById(R.id.Date);
            Ordernumber =  view.findViewById(R.id.Ordernumber);
            Quantity =  view.findViewById(R.id.Quantity);
            ProductName =  view.findViewById(R.id.ProductName);
            RedeemPoints =  view.findViewById(R.id.RedeemPoints);

        }
    }

    public PointStatusAgentAdapter(ArrayList<Datares> transactionsList, Context mContext) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
    }

    @Override
    public PointStatusAgentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.point_agent_adapter, parent, false);

        return new PointStatusAgentAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final PointStatusAgentAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);

        holder.Date.setText("" + operator.getDate());
        holder.Status.setText("" + operator.getDeliveryStatus());
        holder.Ordernumber.setText("" + operator.getOrderNo());
        holder.ProductName.setText("" + operator.getProduct());
        holder.Quantity.setText("" + operator.getQuantity());
        holder.RedeemPoints.setText("" + operator.getDebit());


        if(operator.getDeliveryStatus().equalsIgnoreCase("pending")){

            holder.Status.setTextColor(mContext.getResources().getColorStateList(R.color.white));
            holder.Status.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.yallow));

        }else if(operator.getDeliveryStatus().equalsIgnoreCase("active")){

            holder.Status.setTextColor(mContext.getResources().getColorStateList(R.color.lightwite));

        }


    }
    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}