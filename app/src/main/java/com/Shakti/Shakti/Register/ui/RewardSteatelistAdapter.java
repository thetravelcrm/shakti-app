package com.Shakti.Shakti.Register.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Setting.RewardRedeemActivity;
import com.Shakti.Shakti.SubmitPurchase.ui.ExistingActivity;
import com.Shakti.Shakti.SubmitPurchase.ui.SubmitPurchaseActivity;

import java.util.ArrayList;

public class RewardSteatelistAdapter extends RecyclerView.Adapter<RewardSteatelistAdapter.MyViewHolder> {

    private ArrayList<Datares> operatorList;
    private Context mContext;
    int resourceId = 0;
    String type="";

    public void filter(ArrayList<Datares> newlist) {
        operatorList=new ArrayList<>();
        operatorList.addAll(newlist);
        notifyDataSetChanged();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);


        }
    }

    public RewardSteatelistAdapter(ArrayList<Datares> operatorList, Context mContext, String type) {
        this.operatorList = operatorList;
        this.mContext = mContext;
        this.type = type;
     }

    @Override
    public RewardSteatelistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.starte_list_adapter, parent, false);

        return new RewardSteatelistAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RewardSteatelistAdapter.MyViewHolder holder, int position) {
        final Datares operator = operatorList.get(position);


            if(type.equalsIgnoreCase("State")){
                holder.name.setText(""+operator.getName() );
            }else if(type.equalsIgnoreCase("District")){
                holder.name.setText(""+operator.getDistrictName() );
            }



        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equalsIgnoreCase("State")){

                    ((RewardRedeemActivity) mContext).ItemClicksts(operator.getId());

                }else if(type.equalsIgnoreCase("District")){

                    ((RewardRedeemActivity) mContext).ItemClickid(operator.getDistrictID(),operator.getStateID(),operator.getDistrictName(),operator.getStateName());

                }



            }
        });

    }

    @Override
    public int getItemCount() {
        return operatorList.size();
    }


}
