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

import com.Shakti.Shakti.Dashbord.ui.HomeFragment;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.SubmitPurchase.ui.ExistingActivity;
import com.Shakti.Shakti.SubmitPurchase.ui.SubmitPurchaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DashBoardProductAdapter extends RecyclerView.Adapter<DashBoardProductAdapter.MyViewHolder> {

    private ArrayList<ProductList> operatorList;
    private Context mContext;
    int resourceId = 0;
    private int row_index;

    String type="";





    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  package_description,productprise,productpoint;
        ImageView packageican;
        CardView main;
        LinearLayout linera;

        public MyViewHolder(View view) {
            super(view);
             package_description = (TextView) view.findViewById(R.id.package_description);
            productprise = (TextView) view.findViewById(R.id.productprise);
            productpoint = (TextView) view.findViewById(R.id.productpoint);
             packageican=view.findViewById(R.id.packageican);
            main=view.findViewById(R.id.main);
            linera=view.findViewById(R.id.linera);

        }
    }

    public DashBoardProductAdapter(ArrayList<ProductList> operatorList, Context mContext,String type) {
        this.operatorList = operatorList;
        this.mContext = mContext;
        this.type = type;
     }

    @Override
    public DashBoardProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboad_product_adapter, parent, false);

        return new DashBoardProductAdapter.MyViewHolder(itemView);

    }



    @Override
    public void onBindViewHolder(final DashBoardProductAdapter.MyViewHolder holder, final int position) {
        final ProductList operator = operatorList.get(position);




        holder.package_description.setText(operator.getDescription());
        holder.productpoint.setText(mContext.getString(R.string.Points)+" : \n"+operator.getPoints());
        holder.productprise.setText(mContext.getString(R.string.ruppee)+"  \n"+operator.getPrice());


        RequestOptions options = new RequestOptions();
        Glide.with(mContext)
                .load(operator.getImage())


                .into(holder.packageican);



    }

    @Override
    public int getItemCount() {
        return operatorList.size();
    }

}
