package com.Shakti.Shakti.Register.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.Dashbord.ui.RequesttechexpressActivity;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.ReportAll.Activity.EditprofileActivity;
import com.Shakti.Shakti.Setting.RewardRedeemActivity;
import com.Shakti.Shakti.SubmitPurchase.ui.DealerpurchaseActivity;
import com.Shakti.Shakti.SubmitPurchase.ui.ExistingActivity;
import com.Shakti.Shakti.SubmitPurchase.ui.SubmitPurchaseActivity;
import com.Shakti.Shakti.Youtube.ui.VideosListActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class SteatelistAdapter extends RecyclerView.Adapter<SteatelistAdapter.MyViewHolder> {

    private ArrayList<Datares> operatorList;
    private Context mContext;
    int resourceId = 0;
    String type="";
    String page="";

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

    public SteatelistAdapter(ArrayList<Datares> operatorList, Context mContext,String type,String page) {
        this.operatorList = operatorList;
        this.mContext = mContext;
        this.type = type;
        this.page = page;
    }

    @Override
    public SteatelistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.starte_list_adapter, parent, false);

        return new SteatelistAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SteatelistAdapter.MyViewHolder holder, int position) {
        final Datares operator = operatorList.get(position);




            if(type.equalsIgnoreCase("State")){
                holder.name.setText(""+operator.getName() );
            }else if(type.equalsIgnoreCase("District")){
                holder.name.setText(""+operator.getDistrictName() );
            }else if(type.equalsIgnoreCase("Construction Stage")){
                holder.name.setText(""+operator.getName() );
            }else if(type.equalsIgnoreCase("Dealer")){
                holder.name.setText(""+operator.getName() );
            }else if(type.equalsIgnoreCase("BrandList")){
                holder.name.setText(""+operator.getName() );
            }else if(type.equalsIgnoreCase("Brand")){
                holder.name.setText(""+operator.getName() );
            }else if (type.equalsIgnoreCase("Block")){
                holder.name.setText(""+operator.getBlockName());
            }else if (type.equalsIgnoreCase("AgentType")){
                holder.name.setText(""+operator.getagentType());
            }


        if(page.equalsIgnoreCase("21")){

            if(type.equalsIgnoreCase("Site")){
                holder.name.setText(""+operator.getName() );
            }

        }else {

            if(type.equalsIgnoreCase("Site")){
                holder.name.setText(""+operator.getSitetype() );
            }


        }




        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(page.equalsIgnoreCase("21")){

                    if(type.equalsIgnoreCase("Site")){

                        ((ExistingActivity) mContext).ItemClickSite(operator.getSiteTypeID(),operator.getId(),operator.getName());


                    }else if(type.equalsIgnoreCase("Construction Stage")){


                        ((ExistingActivity) mContext).ItemClickidConstructionside(operator.getSiteTypeID(),operator.getSitetype(),operator.getId(),operator.getName());


                    }else if(type.equalsIgnoreCase("Brand")){


                        ((ExistingActivity) mContext).ItemClickidBrandList(operator.getId(),operator.getName() );


                    }else if(type.equalsIgnoreCase("Dealer")){


                        ((ExistingActivity) mContext).ItemClickidDealer(operator.getId(),operator.getName() ); }

                }
                else if(page.equalsIgnoreCase("1")){

                    if(type.equalsIgnoreCase("State")){

                        ((RegisterActivity) mContext).ItemClick(operator.getId());


                    }else if(type.equalsIgnoreCase("District")){


                        ((RegisterActivity) mContext).ItemClickid(operator.getDistrictID(),operator.getStateID(),operator.getDistrictName(),operator.getStateName());


                    }
                    if(type.equalsIgnoreCase("Block")){
                        ((RegisterActivity) mContext).ItemClickidBlockList(operator.getBlockID(),operator.getBlockName());
                    }
                    if(type.equalsIgnoreCase("AgentType")){
                        ((RegisterActivity) mContext).ItemClickidAgentTypeList(operator.getagentType());
                    }

                }
                else if(page.equalsIgnoreCase("3")){

                    if(type.equalsIgnoreCase("State")){

                        ((EditprofileActivity) mContext).ItemClick(operator.getId());


                    }else if(type.equalsIgnoreCase("District")){


                        ((EditprofileActivity) mContext).ItemClickid(operator.getDistrictID(),operator.getStateID(),operator.getDistrictName(),operator.getStateName());
                    }
                    if(type.equalsIgnoreCase("Block")){
                        ((EditprofileActivity) mContext).ItemClickidBlockList(operator.getBlockID(),operator.getBlockName());
                    }
                    if(type.equalsIgnoreCase("AgentType")){
                        ((EditprofileActivity) mContext).ItemClickidAgentTypeList(operator.getagentType());
                    }

                }
                else if(page.equalsIgnoreCase("44")){

                    if(type.equalsIgnoreCase("State")){

                        ((RewardRedeemActivity) mContext).ItemClicksts(operator.getId());


                    }else if(type.equalsIgnoreCase("District")){


                        ((RewardRedeemActivity) mContext).ItemClickid(operator.getDistrictID(),operator.getStateID(),operator.getDistrictName(),operator.getStateName());


                    }

                }
                else if(page.equalsIgnoreCase("2")){

                    if(type.equalsIgnoreCase("State")){

                        ((SubmitPurchaseActivity) mContext).ItemClickPurchase(operator.getId());


                    }else if(type.equalsIgnoreCase("District")){


                        ((SubmitPurchaseActivity) mContext).ItemClickidPurchase(operator.getDistrictID(),operator.getStateID(),operator.getDistrictName(),operator.getStateName());


                    }else if(type.equalsIgnoreCase("Construction Stage")){


                        ((SubmitPurchaseActivity) mContext).ItemClickidConstructionside(operator.getSiteTypeID(),operator.getSitetype(),operator.getId(),operator.getName());


                    }else if(type.equalsIgnoreCase("Site")){


                         ((SubmitPurchaseActivity) mContext).ItemClickidSite(operator.getId(),operator.getSitetype() );


                    }else if(type.equalsIgnoreCase("Dealer")){


                         ((SubmitPurchaseActivity) mContext).ItemClickidDealer(operator.getId(),operator.getName() );


                    }else if(type.equalsIgnoreCase("Brand")){


                         ((SubmitPurchaseActivity) mContext).ItemClickidBrandList(operator.getId(),operator.getName() );


                    }else if(type.equalsIgnoreCase("Block")){


                        ((SubmitPurchaseActivity) mContext).ItemClickidBlockList(operator.getBlockID(),operator.getBlockName() );


                    }


                }
                else if(page.equalsIgnoreCase("4")){
                    if(type.equalsIgnoreCase("Brand")){


                        ((DealerpurchaseActivity) mContext).ItemClickidBrandList(operator.getId(),operator.getName() );


                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return operatorList.size();
    }

}
