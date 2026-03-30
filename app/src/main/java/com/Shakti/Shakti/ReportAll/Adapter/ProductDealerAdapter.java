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

import java.util.ArrayList;


public class ProductDealerAdapter extends RecyclerView.Adapter<ProductDealerAdapter.MyViewHolder> {

    private ArrayList<Datares> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
         public  TextView Date,Status,SiteName,District,Orderno,Product,Quantity,Remark;


        public MyViewHolder(View view) {
            super(view);
            Status =  view.findViewById(R.id.Status);
            Date =  view.findViewById(R.id.Date);
            SiteName =  view.findViewById(R.id.SiteName);
            Orderno =  view.findViewById(R.id.Orderno);
            District =  view.findViewById(R.id.District);
            Product =  view.findViewById(R.id.Product);
            Quantity =  view.findViewById(R.id.Quantity);
            Remark=view.findViewById(R.id.DealerRemark);


        }
    }

    public ProductDealerAdapter(ArrayList<Datares> transactionsList, Context mContext) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
        Log.e("DRS", "In ProductDealerAdapter");
    }

    @Override
    public ProductDealerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dealer_product_adapter, parent, false);
        Log.e("DRS", "In ProductDealerAdapter.MyViewHolder");
        return new ProductDealerAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final ProductDealerAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);
        Log.e("DRS","position : "+position+" product: "+operator.getProductName());
        holder.Date.setText("" + operator.getCreatedDate());
         holder.Status.setText("" + operator.getPurchaseStatus());
        holder.SiteName.setText("" + operator.getDealerName());
         holder.District.setText("" + operator.getDistrictName());
        holder.Orderno.setText("" + operator.getOrderNo());
        holder.Product.setText("" + operator.getProductName());
        holder.Quantity.setText("" + operator.getQuantity());
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