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


public class SiteAgentAdapter extends RecyclerView.Adapter<SiteAgentAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
         public  TextView Date,Status,SiteOwner,OwnerMobile,District,SiteType,SiteAddress,SiteStarting,Remark;


        public MyViewHolder(View view) {
            super(view);
            Status =  view.findViewById(R.id.Status);
            Date =  view.findViewById(R.id.Date);
            SiteOwner =  view.findViewById(R.id.SiteOwner);
            District =  view.findViewById(R.id.District);
            OwnerMobile =  view.findViewById(R.id.OwnerMobile);
            SiteType =  view.findViewById(R.id.SiteType);
            SiteAddress =  view.findViewById(R.id.SiteAddress);
            SiteStarting =  view.findViewById(R.id.SiteStarting);
             Remark =  view.findViewById(R.id.Remark);


        }
    }

    public SiteAgentAdapter(ArrayList<Datares> transactionsList, Context mContext) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
    }

    @Override
    public SiteAgentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.site_agent_adapter, parent, false);

        return new SiteAgentAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final SiteAgentAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);

        holder.Date.setText("" + operator.getDate());
        holder.Status.setText("" + operator.getSiteStatus());
        holder.SiteType.setText("" + operator.getSiteType());
        holder.District.setText("" + operator.getDistrict());
        holder.SiteStarting.setText("" + operator.getStartingDate());
        holder.SiteOwner.setText("" + operator.getSiteName());
        holder.SiteAddress.setText("" + operator.getAddress());
        holder.OwnerMobile.setText("" + operator.getMobile());
        holder.Remark.setText("" + operator.getRemark());


        if(operator.getSiteStatus().equalsIgnoreCase("pending")){

            holder.Status.setTextColor(mContext.getResources().getColorStateList(R.color.white));
            holder.Status.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.yallow));

        }else if(operator.getSiteStatus().equalsIgnoreCase("active")){

            holder.Status.setTextColor(mContext.getResources().getColorStateList(R.color.lightwite));

        }


    }
    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}