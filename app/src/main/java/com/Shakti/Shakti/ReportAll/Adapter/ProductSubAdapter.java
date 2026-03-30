package com.Shakti.Shakti.ReportAll.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class ProductSubAdapter extends RecyclerView.Adapter<ProductSubAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
         public  TextView Date,Status,SiteName,District,Orderno,Product,Quantity,Points,Dealertv,Remark;


        public MyViewHolder(View view) {
            super(view);
            Status =  view.findViewById(R.id.Status);
            Date =  view.findViewById(R.id.Date);
            SiteName =  view.findViewById(R.id.SiteName);
            Orderno =  view.findViewById(R.id.Orderno);
            District =  view.findViewById(R.id.District);
            Product =  view.findViewById(R.id.Product);
            Quantity =  view.findViewById(R.id.Quantity);
            Points =  view.findViewById(R.id.Points);
            Dealertv =  view.findViewById(R.id.Dealertv);
            Remark =  view.findViewById(R.id.Remark);


        }
    }

    public ProductSubAdapter(ArrayList<Datares> transactionsList, Context mContext) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
    }

    @Override
    public ProductSubAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_product_adapter, parent, false);

        return new ProductSubAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final ProductSubAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);

        holder.Date.setText("" + operator.getCreatedDate());
         holder.Status.setText("" + operator.getPurchaseStatus());
        holder.SiteName.setText("" + operator.getSiteName());
         holder.District.setText("" + operator.getDistrictName());
        holder.Orderno.setText("" + operator.getOrderNo());
        holder.Product.setText("" + operator.getProductName());
        holder.Quantity.setText("" + operator.getQuantity());
        holder.Points.setText("" + operator.getPoints());
        holder.Dealertv.setText("" + operator.getDealerName());
        holder.Remark.setText("" + operator.getRemark());


        if(operator.getPurchaseStatus().equalsIgnoreCase("pending")){

            holder.Status.setTextColor(mContext.getResources().getColorStateList(R.color.white));
            holder.Status.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.yallow));


        }else if(operator.getPurchaseStatus().equalsIgnoreCase("active")){

            holder.Status.setTextColor(mContext.getResources().getColorStateList(R.color.lightwite));



        }




    }
    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}