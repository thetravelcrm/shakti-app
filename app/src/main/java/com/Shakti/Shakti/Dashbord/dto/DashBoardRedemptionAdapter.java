package com.Shakti.Shakti.Dashbord.dto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DashBoardRedemptionAdapter extends RecyclerView.Adapter<DashBoardRedemptionAdapter.MyViewHolder> {

    private ArrayList<ProductList> operatorList;
    private Context mContext;
    int resourceId = 0;
    private int row_index;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  Status,Redeempoint,Quantity,Productname,Orderno,Date;


        public MyViewHolder(View view) {
            super(view);

            Status = (TextView) view.findViewById(R.id.Status);
            Redeempoint = (TextView) view.findViewById(R.id.Redeempoint);
            Quantity = (TextView) view.findViewById(R.id.Quantity);
            Productname = (TextView) view.findViewById(R.id.Productname);
            Orderno = (TextView) view.findViewById(R.id.Orderno);
            Date = (TextView) view.findViewById(R.id.Date);


        }
    }

    public DashBoardRedemptionAdapter(ArrayList<ProductList> operatorList, Context mContext) {
        this.operatorList = operatorList;
        this.mContext = mContext;
     }

    @Override
    public DashBoardRedemptionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboad_redeem_adapter, parent, false);

        return new DashBoardRedemptionAdapter.MyViewHolder(itemView);

    }



    @Override
    public void onBindViewHolder(final DashBoardRedemptionAdapter.MyViewHolder holder, final int position) {
        final ProductList operator = operatorList.get(position);

         holder.Status.setText(operator.getDeliveryStatus());
         holder.Redeempoint.setText(operator.getPoints());
         holder.Quantity.setText(operator.getQuantity());
         holder.Productname.setText(operator.getProductName());
         holder.Orderno.setText(operator.getOrderNo());
         holder.Date.setText(operator.getCreatedDate());




    }

    @Override
    public int getItemCount() {
        return operatorList.size();
    }

}
