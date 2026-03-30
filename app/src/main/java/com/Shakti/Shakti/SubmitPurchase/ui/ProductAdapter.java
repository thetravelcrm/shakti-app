package com.Shakti.Shakti.SubmitPurchase.ui;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.Dashbord.ui.HomeFragment;
import com.Shakti.Shakti.Dashbord.ui.ProfileFragment;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private ArrayList<Datares> operatorList;
    private Context mContext;
    String type="";
    private int row_index=0;





    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  package_description;
        ImageView packageican;
        CardView main;
        LinearLayout linera;

        public MyViewHolder(View view) {
            super(view);
             package_description = (TextView) view.findViewById(R.id.package_description);
             packageican=view.findViewById(R.id.packageican);
            main=view.findViewById(R.id.main);
            linera=view.findViewById(R.id.linera);

        }
    }

    public ProductAdapter(ArrayList<Datares> operatorList, Context mContext,String type) {
        this.operatorList = operatorList;
        this.mContext = mContext;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_adapter, parent, false);

        return new MyViewHolder(itemView);

    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Datares operator = operatorList.get(position);

         holder.package_description.setText(operator.getDescription());

         holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(type.equalsIgnoreCase("2")){

                    row_index = position;
                    notifyDataSetChanged();


                    ((SubmitPurchaseActivity) mContext).ItemClickproduct(operator.getProductID(),operator.getUnit());

                }
                else if(type.equalsIgnoreCase("1")){

                    row_index = position;
                    notifyDataSetChanged();

                      holder.linera.setBackgroundResource(R.drawable.rectnewtt);


                    ((ExistingActivity) mContext).ItemClickproduct(operator.getProductID(),operator.getUnit());

                }
                else if(type.equalsIgnoreCase("3"))
                {
                    row_index=position;
                    notifyDataSetChanged();
                    ((DealerpurchaseActivity)mContext).ItemClickproduct(operator.getProductID(),operator.getUnit());
                }


            }
        });




Log.e("position","row_index  "+row_index  +  "  position   "+ position  );


        if (row_index == position) {
             holder.linera.setBackgroundResource(R.drawable.rectnewtt);


            if(type.equalsIgnoreCase("2")){
    ((SubmitPurchaseActivity) mContext).ItemClickproduct(operator.getProductID(),operator.getUnit());

            }else if(type.equalsIgnoreCase("1")){

                ((ExistingActivity) mContext).ItemClickproduct(operator.getProductID(),operator.getUnit());

            }
            else if(type.equalsIgnoreCase("3"))
            {

                ((DealerpurchaseActivity)mContext).ItemClickproduct(operator.getProductID(),operator.getUnit());
            }




        } else {
            holder.linera.setBackgroundResource(R.drawable.rectpro);

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
