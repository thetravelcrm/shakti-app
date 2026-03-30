package com.Shakti.Shakti.ReportAll.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AgentSubAdapter extends RecyclerView.Adapter<AgentSubAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;
    private Integer lenght=0;
    public class MyViewHolder extends RecyclerView.ViewHolder {
         public  TextView Sno,Agentname,Mobilenum,District,Agenttype,Block;
         CircleImageView customerimage;


        public MyViewHolder(View view) {
            super(view);
            Sno =  view.findViewById(R.id.Sno);
            Agentname =  view.findViewById(R.id.Agentname);
            Mobilenum =  view.findViewById(R.id.Mobilenum);
            District =  view.findViewById(R.id.District);
            customerimage =  view.findViewById(R.id.customerimage);
            Agenttype=view.findViewById(R.id.Agenttype);
            Block=view.findViewById(R.id.Block);

        }
    }

    public AgentSubAdapter(ArrayList<Datares> transactionsList, Context mContext) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
    }

    @Override
    public AgentSubAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_report_adapter, parent, false);

        return new AgentSubAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final AgentSubAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);
        holder.Sno.setText("" + (position+1));
        holder.Agentname.setText("" + operator.getDealerName());
         holder.Mobilenum.setText("" + operator.getMobile());
          holder.District.setText("" + operator.getDistrict());
        holder.Agenttype.setText(""+operator.getagentType());
        holder.Block.setText(""+operator.getBlockName());
        RequestOptions options = new RequestOptions();
        Glide.with(mContext)
                .load(operator.getPhoto())
                .into(holder.customerimage);
        if(operator.getDealerName().length()>lenght) {
            lenght = operator.getDealerName().length();
        }
        Log.i("Loop","Position - "+position);
        Log.i("Loop","lenght - "+operator.getDealerName().length());
        Log.i("Loop","final lenght - "+lenght);
        holder.Agentname.setWidth(lenght);
    }
    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}