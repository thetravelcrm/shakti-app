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

public class DashBoardAdapterSitepending extends RecyclerView.Adapter<DashBoardAdapterSitepending.MyViewHolder> {

    private ArrayList<ProductList> operatorList;
    private Context mContext;
    int resourceId = 0;
    private int row_index;





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

    public DashBoardAdapterSitepending(ArrayList<ProductList> operatorList, Context mContext) {
        this.operatorList = operatorList;
        this.mContext = mContext;
     }

    @Override
    public DashBoardAdapterSitepending.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboad_product_adapter, parent, false);

        return new DashBoardAdapterSitepending.MyViewHolder(itemView);

    }



    @Override
    public void onBindViewHolder(final DashBoardAdapterSitepending.MyViewHolder holder, final int position) {
        final ProductList operator = operatorList.get(position);

         holder.package_description.setText(operator.getDescription());
         holder.productpoint.setText("Points : \n"+operator.getPoints());
         holder.productprise.setText("Rs.  \n"+operator.getPrice());

         holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                row_index = position;
                notifyDataSetChanged();


            }
        });


        if (row_index == position) {

            holder.linera.setBackgroundResource(R.drawable.rectnew);

        } else {

            holder.linera.setBackgroundResource(R.drawable.rectnew);

        }


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
