package com.Shakti.Shakti.ReportAll.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.Shakti.Shakti.Dashbord.ui.MyAdapter;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.ReportAll.Adapter.ProductSubAdapter;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class PurchaseStatusActivity extends AppCompatActivity  implements View.OnClickListener {

     Loader loader;
    String SiteID="0";

   // int cou=0;
   Button bt_search;
    MyAdapter adapter;
    RecyclerView recycler_view;

    LinearLayout LI_filter,LI_filterOpen;
    ProductSubAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    RegisterResponse transactionspack = new RegisterResponse();
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();



    Spinner sitensamelist;

    String cityid="0";

    RegisterResponse transactions = new RegisterResponse();
    ArrayList<Datares> transactionsspinner = new ArrayList<>();
    ArrayList<String> bankList = new ArrayList<String>();

    ImageView image,closesearch,opensearch;
    HorizontalScrollView horizantal;
    ListView ListStatus,ListDate,ListSiteName,ListDistrict,ListOrderNo,ListProduct,ListQuantity,ListPoints,ListDealer,ListRemark;
    LinearLayout li_Status,li_Date,li_SiteName,li_District,li_OrderNo,li_Product,Li_Quantity,Li_Points,Li_Dealer,Li_Remark;
    TextView TV_Date,TV_SiteName,TV_District,TV_OrderNo,TV_Product,TV_Quantity,TV_Points,TV_Dealer,TV_Remark;
    ArrayList<String> ArrayStatus = new ArrayList<String>();
    ArrayList<String> ArrayDate = new ArrayList<String>();
    ArrayList<String> ArraySiteName = new ArrayList<String>();
    ArrayList<String> ArrayDistrict = new ArrayList<String>();
    ArrayList<String> ArrayOrderNo = new ArrayList<String>();
    ArrayList<String> ArrayProduct = new ArrayList<String>();
    ArrayList<String> ArrayQuantity = new ArrayList<String>();
    ArrayList<String> ArrayPoints = new ArrayList<String>();
    ArrayList<String> ArrayDealer = new ArrayList<String>();
    ArrayList<String> ArrayRemark = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_status);

        horizantal=findViewById(R.id.horizantal);
        image=findViewById(R.id.image);

          recycler_view=findViewById(R.id.recycler_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.PurchaseStatus);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);


        sitensamelist=findViewById(R.id.sitensamelist);
        getID();
        HitSiteList();
        HitApi();


    }

    public void getID(){
        ListStatus=findViewById(R.id.ListStatus);
        ListDate=findViewById(R.id.ListDate);
        ListSiteName=findViewById(R.id.ListSiteName);
        ListDistrict=findViewById(R.id.ListDistrict);
        ListOrderNo=findViewById(R.id.ListOrderNo);
        ListProduct=findViewById(R.id.ListProduct);
        ListQuantity=findViewById(R.id.ListQuantity);
        ListPoints=findViewById(R.id.ListPoints);
        ListDealer=findViewById(R.id.ListDealer);
        ListRemark=findViewById(R.id.ListRemark);
        li_Status=findViewById(R.id.li_Status);
        li_Date=findViewById(R.id.li_Date);
        li_SiteName=findViewById(R.id.li_SiteName);
        li_District=findViewById(R.id.li_District);
        li_OrderNo=findViewById(R.id.li_OrderNo);
        li_Product=findViewById(R.id.li_Product);
        Li_Quantity=findViewById(R.id.Li_Quantity);
        Li_Points=findViewById(R.id.Li_Points);
        Li_Dealer=findViewById(R.id.Li_Dealer);
        Li_Remark=findViewById(R.id.Li_Remark);

        TV_Date=findViewById(R.id.TV_Date);
        TV_SiteName=findViewById(R.id.TV_SiteName);
        TV_District=findViewById(R.id.TV_District);
        TV_OrderNo=findViewById(R.id.TV_OrderNo);
        TV_Product=findViewById(R.id.TV_Product);
        TV_Quantity=findViewById(R.id.TV_Quantity);
        TV_Points=findViewById(R.id.TV_Points);
        TV_Dealer=findViewById(R.id.TV_Dealer);
        TV_Remark=findViewById(R.id.TV_Remark);

        LI_filter=findViewById(R.id.LI_filter);
        LI_filterOpen=findViewById(R.id.LI_filterOpen);
        
        closesearch=findViewById(R.id.closesearch);
        closesearch.setOnClickListener(this);
        opensearch=findViewById(R.id.opensearch);
        opensearch.setOnClickListener(this);
        bt_search=findViewById(R.id.bt_search);
        bt_search.setOnClickListener(this);
    }


    public void dataParsesCityList(String response) {

        bankList.add("All Sites");

        Gson gson = new Gson();
        transactions = gson.fromJson(response, RegisterResponse.class);
        transactionsspinner = transactions.getData();

        if (transactionsspinner!=null && transactionsspinner.size() > 0) {

            if (transactionsspinner != null && transactionsspinner.size() > 0) {

                for (int i = 0; i < transactionsspinner.size(); i++) {

                    bankList.add(transactionsspinner.get(i).getName());

                }
            }


            sitensamelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Log.e("onItemSelected","  position   "+ position + "  ,  id  "+  id +"  ,  cou   ");

                    if (parent.getItemAtPosition(position).toString().equals("All Sites")) {

                        SiteID="0";

                    } else {

                        SiteID = transactionsspinner.get(position-1).getId();






                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            ArrayAdapter<String> countryAdapter;
            countryAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, bankList);
            sitensamelist.setAdapter(countryAdapter);

        } else {

            image.setVisibility(View.VISIBLE);
            horizantal.setVisibility(View.GONE);

        }

    }


    private void HitSiteList()
    {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.SiteList(this, loader);


        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }
    }
    private void HitApi() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.PurchaseAgentReport(this,SiteID, loader,horizantal,image);

        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("PurchaseAgentReport")) {
            String startelist=""+activityFragmentMessage.getFrom();

           // dataParseRequestTechReport(startelist);
            dataParse(startelist);

        }
        else if(activityFragmentMessage.getMessage().equalsIgnoreCase("SiteList")){
            String productlist=""+activityFragmentMessage.getFrom();
            dataParsesCityList(productlist);

        }

    }

    public void dataParse(String response) {


        ResetArray();
        Gson gson = new Gson();
        RegisterResponse transactionspack = gson.fromJson(response, RegisterResponse.class);


        if (transactionspack.getList().size() > 0) {

            if (transactionspack.getList() != null && transactionspack.getList().size() > 0) {

                for (int i = 0; i < transactionspack.getList().size(); i++) {

                    ArrayStatus.add(transactionspack.getList().get(i).getPurchaseStatus());
                    ArrayDate.add(transactionspack.getList().get(i).getCreatedDate());
                    ArraySiteName.add(transactionspack.getList().get(i).getSiteName());
                    ArrayDistrict.add(transactionspack.getList().get(i).getDistrictName());
                    ArrayOrderNo.add(transactionspack.getList().get(i).getOrderNo());
                    ArrayProduct.add(transactionspack.getList().get(i).getProductName());
                    ArrayQuantity.add(transactionspack.getList().get(i).getQuantity());
                    ArrayPoints.add(transactionspack.getList().get(i).getPoints());
                    ArrayDealer.add(transactionspack.getList().get(i).getDealerName());
                    ArrayRemark.add(transactionspack.getList().get(i).getRemark());
                }

            }

            adapter = new MyAdapter(this, ArrayStatus,"5");
            ListStatus.setAdapter(adapter);
            Helpernew.setListViewWidth(ListStatus,li_SiteName,100);

            ListDate.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, ArrayDate));
            Helpernew.getListViewSize(ListDate);
            TV_Date.measure(0,0);
            Helpernew.setListViewWidth(ListDate,li_Date,TV_Date.getMeasuredWidth());

            ListSiteName.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, ArraySiteName));
            Helpernew.getListViewSize(ListSiteName);
            TV_SiteName.measure(0,0);
            Helpernew.setListViewWidth(ListSiteName,li_SiteName,TV_SiteName.getMeasuredWidth());

            ListDistrict.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, ArrayDistrict));
            Helpernew.getListViewSize(ListDistrict);
            TV_District.measure(0,0);
            Helpernew.setListViewWidth(ListDistrict,li_District,TV_District.getMeasuredWidth());

            ListOrderNo.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, ArrayOrderNo));
            TV_OrderNo.measure(0,0);
            Helpernew.getListViewSize(ListOrderNo);
            Helpernew.setListViewWidth(ListOrderNo,li_OrderNo,TV_OrderNo.getMeasuredWidth());

            ListProduct.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, ArrayProduct));
            TV_Product.measure(0,0);
            Helpernew.getListViewSize(ListProduct);
            Helpernew.setListViewWidth(ListProduct,li_Product,TV_Product.getMeasuredWidth());

            ListQuantity.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, ArrayQuantity));
            TV_Quantity.measure(0,0);
            Helpernew.getListViewSize(ListQuantity);
            Helpernew.setListViewWidth(ListQuantity,Li_Quantity,TV_Quantity.getMeasuredWidth());

            ListPoints.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, ArrayPoints));
            TV_Points.measure(0,0);
            Helpernew.getListViewSize(ListPoints);
            Helpernew.setListViewWidth(ListPoints,Li_Points,TV_Points.getMeasuredWidth());

            ListDealer.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, ArrayDealer));
            TV_Dealer.measure(0,0);
            Helpernew.getListViewSize(ListDealer);
            Helpernew.setListViewWidth(ListDealer,Li_Dealer,TV_Dealer.getMeasuredWidth());

            ListRemark.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, ArrayRemark));
            TV_Remark.measure(0,0);
            Helpernew.getListViewSize(ListRemark);
            Helpernew.setListViewWidth(ListRemark,Li_Remark,TV_Remark.getMeasuredWidth());
            horizantal.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);

        } else {
            horizantal.setVisibility(View.GONE);
            //LI_PENDINGSITES.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);

        }

//        Gson gson = new Gson();
//        transactionspack = gson.fromJson(response, RegisterResponse.class);
//        transactionsObjectspack = transactionspack.getList();
//
//        if (transactionsObjectspack.size() > 0) {
//            mAdapter = new ProductSubAdapter(transactionsObjectspack, this);
//            //  mLayoutManager = new LinearLayoutManager(this);
//            mLayoutManager = new GridLayoutManager(this,1);
//            recycler_view.setLayoutManager(mLayoutManager);
//            recycler_view.setItemAnimator(new DefaultItemAnimator());
//            recycler_view.setAdapter(mAdapter);
//
//            recycler_view.setVisibility(View.VISIBLE);
//        } else {
//            recycler_view.setVisibility(View.GONE);
//        }

    }

    public void ResetArray()
    {
        ArrayStatus.clear();
        ArrayDate.clear();
        ArraySiteName.clear();
        ArrayDistrict.clear();
        ArrayOrderNo.clear();
        ArrayProduct.clear();
        ArrayQuantity.clear();
        ArrayPoints.clear();
        ArrayDealer.clear();
        ArrayRemark.clear();
    }
    @Override
    public void onClick(View view) {
        if(view==bt_search)
        {

            HitApi();

        }
        if(view==closesearch)
        {
            LI_filter.setVisibility(View.GONE);
            LI_filterOpen.setVisibility(View.VISIBLE);
        }
        if(view==opensearch)
        {
            LI_filter.setVisibility(View.VISIBLE);
            LI_filterOpen.setVisibility(View.GONE);
        }
    }
}
