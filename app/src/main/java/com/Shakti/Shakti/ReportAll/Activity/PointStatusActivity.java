package com.Shakti.Shakti.ReportAll.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.Dashbord.ui.MyAdapter;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.ReportAll.Adapter.PointStatusAgentAdapter;
import com.Shakti.Shakti.ReportAll.Adapter.PointStatusReedemAgentAdapter;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class PointStatusActivity extends Fragment {

    LinearLayout li_Product,li_Quantity,li_Credit,li_Date;
    TextView TV_Product,TV_Quantity,TV_Credit,TV_Date;
    ListView ListDate,ListCredit,ListQuantity,ListProduct;
    Loader loader;
    RegisterResponse transactionspack = new RegisterResponse();
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();

    RecyclerView recycler_view;
    PointStatusReedemAgentAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    ArrayList<String> ArrayDate = new ArrayList<String>();
    ArrayList<String> ArrayCredit = new ArrayList<String>();
    ArrayList<String> ArrayQuantity = new ArrayList<String>();
    ArrayList<String> ArrayProduct = new ArrayList<String>();
    ImageView image;
    HorizontalScrollView horizantal;


   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_status);
*/

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View v= inflater.inflate(R.layout.activity_earn_point_status, container, false);



       horizantal=v.findViewById(R.id.horizantal);
        image=v.findViewById(R.id.image);
        recycler_view=v.findViewById(R.id.recycler_view);

       li_Product=v.findViewById(R.id.li_Product);
       li_Quantity=v.findViewById(R.id.li_Quantity);
       li_Credit=v.findViewById(R.id.li_Credit);
       li_Date=v.findViewById(R.id.li_Date);
       ListDate=v.findViewById(R.id.ListDate);
       ListCredit=v.findViewById(R.id.ListCredit);
       ListQuantity=v.findViewById(R.id.ListQuantity);
       ListProduct=v.findViewById(R.id.ListProduct);

       TV_Product=v.findViewById(R.id.TV_Product);
       TV_Quantity=v.findViewById(R.id.TV_Quantity);
       TV_Credit=v.findViewById(R.id.TV_Credit);
       TV_Date=v.findViewById(R.id.TV_Date);

        loader = new Loader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);



        HitApi();

        return v;

    }

    private void HitApi() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.PointStatusReport(getActivity(), loader,horizantal,image);

        } else {
            UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }



    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }



    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("PointStatusReport")) {
            String startelist=""+activityFragmentMessage.getFrom();
            Log.i("PointStatus","startelist - "+startelist);
            dataParse(startelist);

        }

    }

    public void dataParse(String response) {


        Log.i("PointStatus","startelist response - "+response);
        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, RegisterResponse.class);
        transactionsObjectspack = transactionspack.getEarn();
        Log.i("PointStatus","startelist transactionsObjectspacke - "+transactionsObjectspack.size());
        if (transactionsObjectspack != null && transactionsObjectspack.size() > 0) {

            Log.i("PointStatus","startelist transactionsObjectspacke - "+transactionsObjectspack.size());
            if (transactionsObjectspack != null && transactionsObjectspack.size() > 0) {

                for (int i = 0; i < transactionsObjectspack.size(); i++) {
                    ArrayCredit.add(transactionsObjectspack.get(i).getCredit());
                    ArrayDate.add(transactionsObjectspack.get(i).getDate());
                    ArrayProduct.add(transactionsObjectspack.get(i).getProduct());
                    ArrayQuantity.add(transactionsObjectspack.get(i).getQuantity());
                    Log.i("PointStatus","ArrayProduct - "+transactionsObjectspack.get(i).getProduct());
                }
            }


            ListCredit.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview,ArrayCredit));
            TV_Credit.measure(0,0);
            Helpernew.getListViewSize(ListCredit);
            Helpernew.setListViewWidth(ListCredit,li_Credit,TV_Credit.getMeasuredWidth());

            ListDate.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview,ArrayDate));
            TV_Date.measure(0,0);
            Helpernew.getListViewSize(ListDate);
            Helpernew.setListViewWidth(ListDate,li_Date,TV_Date.getMeasuredWidth());

            ListProduct.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview,ArrayProduct));
            TV_Product.measure(0,0);
            Helpernew.getListViewSize(ListProduct);
            Helpernew.setListViewWidth(ListProduct,li_Product,TV_Product.getMeasuredWidth());

            ListQuantity.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview,ArrayQuantity));
            TV_Quantity.measure(0,0);
            Helpernew.getListViewSize(ListQuantity);
            Helpernew.setListViewWidth(ListQuantity,li_Quantity,TV_Quantity.getMeasuredWidth());


            //horizantal.setVisibility(View.VISIBLE);
            //image.setVisibility(View.GONE);

        } else {

            //image.setVisibility(View.VISIBLE);
            //horizantal.setVisibility(View.GONE);


        }
//        Gson gson = new Gson();
//        transactionspack = gson.fromJson(response, RegisterResponse.class);
//        transactionsObjectspack = transactionspack.getEarn();
//
//        if (transactionsObjectspack.size() > 0) {
            //mAdapter = new PointStatusReedemAgentAdapter(transactionsObjectspack, getActivity());
//            mLayoutManager = new GridLayoutManager(getActivity(),1);
//            recycler_view.setLayoutManager(mLayoutManager);
//            recycler_view.setItemAnimator(new DefaultItemAnimator());
//            recycler_view.setAdapter(mAdapter);
//
//            recycler_view.setVisibility(View.VISIBLE);
//        } else {
//            recycler_view.setVisibility(View.GONE);
//
//            horizantal.setVisibility(View.GONE);
//            image.setVisibility(View.VISIBLE);
//        }

    }



}
