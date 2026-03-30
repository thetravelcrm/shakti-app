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

public class ProductPurchaseDealerAdapter extends RecyclerView.Adapter<ProductPurchaseDealerAdapter.MyViewHolder>
{

    private ArrayList<Datares> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public  TextView Sno,EntryDate,PurchaseDate,Product,Quantity;


        public MyViewHolder(View view) {
            super(view);
            Sno =  view.findViewById(R.id.Sno);
            EntryDate =  view.findViewById(R.id.EntryDate);
            PurchaseDate =  view.findViewById(R.id.PurchaseDate);
            Product =  view.findViewById(R.id.Product);
            Quantity=  view.findViewById(R.id.Quantity);


        }
    }

    public ProductPurchaseDealerAdapter(ArrayList<Datares> transactionsList, Context mContext) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
        Log.e("DRS", "In ProductDealerAdapter");
    }

    @Override
    public ProductPurchaseDealerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dealer_purchaseproduct_adapter, parent, false);
        Log.e("DRS", "In ProductDealerAdapter.MyViewHolder");
        return new ProductPurchaseDealerAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final ProductPurchaseDealerAdapter.MyViewHolder holder, int position) {
        final Datares operator = transactionsList.get(position);
        Log.e("DRS","position : "+position+" product: "+operator.getProductName());
        holder.Sno.setText("" + (position+1));
        holder.EntryDate.setText("" + operator.getCreatedDate());
        holder.PurchaseDate.setText("" + operator.getPurchaseDate());
        holder.Product.setText("" + operator.getProductName());
        holder.Quantity.setText("" + operator.getQuantity());

    }
    @Override
    public int getItemCount() {
        return transactionsList.size();
    }
}
