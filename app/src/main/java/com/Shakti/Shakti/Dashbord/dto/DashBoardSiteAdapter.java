package com.Shakti.Shakti.Dashbord.dto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.Dashbord.ui.VerifySiteActivity;
import com.Shakti.Shakti.R;

import java.util.ArrayList;

public class DashBoardSiteAdapter extends RecyclerView.Adapter<DashBoardSiteAdapter.MyViewHolder> {

    private ArrayList<ProductList> operatorList;
    private Context mContext;
    int resourceId = 0;
    private int row_index;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  District,SiteName,MobileNo,SiteType,AgentName,Date,AgentMobileno,Verify_tv;


        public MyViewHolder(View view) {
            super(view);

            District = (TextView) view.findViewById(R.id.District);
            SiteName = (TextView) view.findViewById(R.id.SiteName);
            MobileNo = (TextView) view.findViewById(R.id.MobileNo);
            SiteType = (TextView) view.findViewById(R.id.SiteType);
            AgentName = (TextView) view.findViewById(R.id.AgentName);
            Date = (TextView) view.findViewById(R.id.Date);
            AgentMobileno = (TextView) view.findViewById(R.id.AgentMobileno);
            Verify_tv = (TextView) view.findViewById(R.id.Verify_tv);


        }
    }

    public DashBoardSiteAdapter(ArrayList<ProductList> operatorList, Context mContext) {
        this.operatorList = operatorList;
        this.mContext = mContext;
     }

    @Override
    public DashBoardSiteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboad_site_adapter, parent, false);

        return new DashBoardSiteAdapter.MyViewHolder(itemView);

    }



    @Override
    public void onBindViewHolder(final DashBoardSiteAdapter.MyViewHolder holder, final int position) {
        final ProductList operator = operatorList.get(position);

         holder.SiteName.setText(operator.getSiteName());
         holder.District.setText(operator.getDistrictName());
         holder.MobileNo.setText(operator.getMobileNo());
         holder.SiteType.setText(operator.getSiteType());
         holder.AgentName.setText(operator.getAgentName());
         holder.Date.setText(operator.getCreatedDate());
         holder.AgentMobileno.setText(operator.getMobileNo());


         holder.Verify_tv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {


                 Intent i=new Intent(mContext, VerifySiteActivity.class);
                 i.putExtra("id",""+operator.getId());
                 mContext.startActivity(i);





             }
         });

    }

    @Override
    public int getItemCount() {
        return operatorList.size();
    }

}
