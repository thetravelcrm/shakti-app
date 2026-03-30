package com.Shakti.Shakti.ReportAll.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Shakti.Shakti.Dashbord.ui.MyAdapter;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.ReportAll.Adapter.ProductDealerAdapter;
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

public class DealerPurchaseStatusActivity extends AppCompatActivity implements View.OnClickListener {

    Loader loader;
    RecyclerView recycler_view;
    HorizontalScrollView horizantal;
    ImageView image,closesearch,opensearch;
    Button bt_search;
    Spinner ProductSpinner,StatusSpinner,AgentSpinner;
    ProductDealerAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    ListView ListSno,ListDistrict,ListAgent,ListDate,ListQuantity,ListProduct,ListStatus,ListOrderNo,ListRemark;
    LinearLayout LIProduct,LIDistrict,LIAgent,LIDate,LIStatus,LIRemark,LIOrderNo,LIQuantity;
    TextView TV_Product,TV_District,TV_Agent,TV_Date,TV_Status,TV_Remark,TV_OrderNo,TV_Quantity;
    LinearLayout LI_filter,LI_filterOpen;
    RegisterResponse transactionspack = new RegisterResponse();
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();

    RegisterResponse transactions = new RegisterResponse();
    ArrayList<Datares> transactionsspinner = new ArrayList<>();
    ArrayList<String> ProductList = new ArrayList<String>();
    ArrayList<String> StatusList = new ArrayList<String>();
    ArrayList<String> AgentList = new ArrayList<String>();

    ArrayList<String> SnoArray = new ArrayList<String>();
    ArrayList<String> DistrictArray = new ArrayList<String>();
    ArrayList<String> AgentArray = new ArrayList<String>();
    ArrayList<String> DateArray = new ArrayList<String>();
    ArrayList<String> QuantityArray = new ArrayList<String>();
    ArrayList<String> ProductArray = new ArrayList<String>();
    ArrayList<String> OrderNoArray = new ArrayList<String>();
    ArrayList<String> StatusArray = new ArrayList<String>();
    ArrayList<String> RemarkArray = new ArrayList<String>();
    String ProductID="0",AgentID="0",Status="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_purchase_new_status);
        ProductSpinner=findViewById(R.id.ProductSpinner);
        AgentSpinner=findViewById(R.id.AgentSpinner);
        StatusSpinner=findViewById(R.id.StatusSpinner);
        closesearch=findViewById(R.id.closesearch);
        closesearch.setOnClickListener(this);
        opensearch=findViewById(R.id.opensearch);
        opensearch.setOnClickListener(this);
        bt_search=findViewById(R.id.bt_search);
        bt_search.setOnClickListener(this);
        LI_filter=findViewById(R.id.LI_filter);
        LI_filterOpen=findViewById(R.id.LI_filterOpen);
        image=findViewById(R.id.image);
        horizantal=findViewById(R.id.horizantal);
        recycler_view=findViewById(R.id.recycler_view);

        ListSno=findViewById(R.id.ListSNO);
        ListDistrict=findViewById(R.id.ListDistrict);
        ListAgent=findViewById(R.id.ListAgent);
        ListDate=findViewById(R.id.ListDate);
        ListQuantity=findViewById(R.id.ListQuantity);
        ListProduct=findViewById(R.id.ListProduct);
        ListStatus=findViewById(R.id.ListStatus);
        ListOrderNo=findViewById(R.id.ListOrderNo);
        ListRemark=findViewById(R.id.ListRemark);
        LIProduct=findViewById(R.id.LIProduct);
        LIDistrict=findViewById(R.id.LIDistrict);
        LIAgent=findViewById(R.id.LIAgent);
        LIDate=findViewById(R.id.LIDate);
        LIStatus=findViewById(R.id.LIStatus);
        LIRemark=findViewById(R.id.LIRemark);
        LIOrderNo=findViewById(R.id.LIOrderNo);
        LIQuantity=findViewById(R.id.LIQuantity);

        TV_Product=findViewById(R.id.TV_Product);
        TV_District=findViewById(R.id.TV_District);
        TV_Agent=findViewById(R.id.TV_Agent);
        TV_Date=findViewById(R.id.TV_Date);
        TV_Remark=findViewById(R.id.TV_Remark);
        TV_OrderNo=findViewById(R.id.TV_OrderNo);
        TV_Quantity=findViewById(R.id.TV_Quantity);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.SaleReport);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);



        HitProductList();
        HitApi();
        //ShowFilterDialog();

    }


    private void HitApi() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.PurchaseDealerReport(this,ProductID,Status,AgentID, loader,horizantal,image);

        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }




    }

    private void HitProductList()
    {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.ProductList(this, loader);
            UtilMethods.INSTANCE.AgentList(this,loader);

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
        Log.e("AFGM",activityFragmentMessage.getMessage().toString());
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("PurchaseDealerReport")) {
            String startelist=""+activityFragmentMessage.getFrom();

             dataParse(startelist);

        }else if(activityFragmentMessage.getMessage().equalsIgnoreCase("ProductList")){
            String productlist=""+activityFragmentMessage.getFrom();
            dataParsesProductList(productlist);
            dataParsesStatusList();
        }
        else if(activityFragmentMessage.getMessage().equalsIgnoreCase("AgentListnew")){
            String agentlist=""+activityFragmentMessage.getFrom();
            dataParsesAgentList(agentlist);

        }



    }


    public void dataParse(String response) {
        SnoArray.clear();
        StatusArray.clear();
        ProductArray.clear();
        DistrictArray.clear();
        AgentArray.clear();
        DateArray.clear();
        QuantityArray.clear();
        OrderNoArray.clear();
        RemarkArray.clear();
        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, RegisterResponse.class);
        //transactionsObjectspack = transactionspack.getList();
        Integer Sno=1;
        Log.e("DRS", transactionspack.getList().get(0).getPurchaseStatus());

            if (transactionspack.getList() != null && transactionspack.getList().size() > 0) {

                for (int i = 0; i < transactionspack.getList().size(); i++) {
                    SnoArray.add(Sno.toString());
                    StatusArray.add(transactionspack.getList().get(i).getPurchaseStatus());
                    ProductArray.add(transactionspack.getList().get(i).getProductName());
                    DistrictArray.add(transactionspack.getList().get(i).getDistrictName());
                    AgentArray.add(transactionspack.getList().get(i).getDealerName());
                    DateArray.add(transactionspack.getList().get(i).getCreatedDate());
                    QuantityArray.add(transactionspack.getList().get(i).getQuantity());
                    OrderNoArray.add(transactionspack.getList().get(i).getOrderNo());
                    RemarkArray.add(transactionspack.getList().get(i).getRemark());
                    Sno++;
                }

                ListSno.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, SnoArray));
                Helpernew.getListViewSize(ListSno);

                MyAdapter adapter = new MyAdapter(this, StatusArray, "5");
                ListStatus.setAdapter(adapter);

                ListProduct.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, ProductArray));
                TV_Product.measure(0,0);
                Helpernew.getListViewSize(ListProduct);
                Helpernew.setListViewWidth(ListProduct, LIProduct, TV_Product.getMeasuredWidth());

                ListQuantity.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, QuantityArray));
                TV_Quantity.measure(0,0);
                Helpernew.getListViewSize(ListQuantity);
                Helpernew.setListViewWidth(ListQuantity, LIQuantity, TV_Quantity.getMeasuredWidth());

                ListDate.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, DateArray));
                TV_Date.measure(0,0);
                Helpernew.getListViewSize(ListDate);
                Helpernew.setListViewWidth(ListDate, LIDate, TV_Date.getMeasuredWidth());

                ListDistrict.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, DistrictArray));
                TV_District.measure(0,0);
                Helpernew.getListViewSize(ListDistrict);
                Helpernew.setListViewWidth(ListDistrict, LIDistrict, TV_District.getMeasuredWidth());

                ListAgent.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, AgentArray));
                TV_Agent.measure(0,0);
                Helpernew.getListViewSize(ListAgent);
                Helpernew.setListViewWidth(ListAgent, LIAgent, TV_Agent.getMeasuredWidth());

                ListOrderNo.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, OrderNoArray));
                TV_OrderNo.measure(0,0);
                Helpernew.getListViewSize(ListOrderNo);
                Helpernew.setListViewWidth(ListOrderNo, LIOrderNo, TV_OrderNo.getMeasuredWidth());

                ListRemark.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, RemarkArray));
                TV_Remark.measure(0,0);
                Helpernew.getListViewSize(ListRemark);
                Helpernew.setListViewWidth(ListRemark, LIRemark, TV_Remark.getMeasuredWidth());

                horizantal.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
            }
            else
            {
                image.setVisibility(View.VISIBLE);
                horizantal.setVisibility(View.GONE);
            }

//        if (transactionsObjectspack.size() > 0) {
//            Log.e("DRS", "In size >0");
//            mAdapter = new ProductDealerAdapter(transactionsObjectspack, this);
//            //  mLayoutManager = new LinearLayoutManager(this);
//            mLayoutManager = new GridLayoutManager(this,1);
//            recycler_view.setLayoutManager(mLayoutManager);
//            recycler_view.setItemAnimator(new DefaultItemAnimator());
//            recycler_view.setAdapter(mAdapter);
//
//            recycler_view.setVisibility(View.VISIBLE);
//            horizantal.setVisibility(View.VISIBLE);
//            image.setVisibility(View.GONE);
//            LI_filter.setVisibility(View.GONE);
//            LI_filterOpen.setVisibility(View.VISIBLE);
//        }
//        else {
//            recycler_view.setVisibility(View.GONE);
//            horizantal.setVisibility(View.GONE);
//            image.setVisibility(View.VISIBLE);
//            LI_filter.setVisibility(View.VISIBLE);
//            LI_filterOpen.setVisibility(View.GONE);
//
//        }

    }

    public void ShowFilterDialog()
    {


        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.filter_popup);

        dialog.show();
    }

    public void dataParsesProductList(String response) {
        Log.e("DPSA","Enter dataParsesProductList");
        ProductList.add("All Products");

        Gson gson = new Gson();
        transactions = gson.fromJson(response, RegisterResponse.class);
        transactionsspinner = transactions.getData();
        Log.e("DPSA","transactionsspinner");
        if (transactionsspinner.size() > 0) {
            Log.e("DPSA","transactionsspinner >0");
            if (transactionsspinner != null && transactionsspinner.size() > 0) {
                Log.e("DPSA",""+transactionsspinner.size());
                for (int i = 0; i < transactionsspinner.size(); i++) {
                    Log.e("DPSA",transactionsspinner.get(i).getProductName());
                    ProductList.add(transactionsspinner.get(i).getProductName());

                }
            }


            ProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Log.e("onItemSelected","  position   "+ position + "  ,  id  "+  id +"  ,  cou   ");

                    if (parent.getItemAtPosition(position).toString().equals("All Products")) {

                        Log.e("DPSA","0");
                        ProductID="0";

                    } else {

                        ProductID=transactionsspinner.get(position-1).getProductID();
                        //SiteID = transactionsspinner.get(position-1).getId();

                    }
                    //HitApi();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            ArrayAdapter<String> countryAdapter;
            countryAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, ProductList);
            ProductSpinner.setAdapter(countryAdapter);
            //Log.e("DPSA",transactionsspinner.get(i).getProductName());
            image.setVisibility(View.GONE);
            horizantal.setVisibility(View.VISIBLE);
        } else {

            image.setVisibility(View.VISIBLE);
            horizantal.setVisibility(View.GONE);

        }

    }

    public void dataParsesAgentList(String response) {
        Log.e("DPSA","Enter dataParsesProductList");
        AgentList.add("All Agents");

        Gson gson = new Gson();
        transactions = gson.fromJson(response, RegisterResponse.class);
        transactionsspinner = transactions.getData();
        Log.e("DPSA","transactionsspinner");
        if (transactionsspinner.size() > 0) {
            Log.e("DPSA","transactionsspinner >0");
            if (transactionsspinner != null && transactionsspinner.size() > 0) {
                Log.e("DPSA",""+transactionsspinner.size());
                for (int i = 0; i < transactionsspinner.size(); i++) {

                    AgentList.add(transactionsspinner.get(i).getName());

                }
            }


            AgentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Log.e("onItemSelected","  position   "+ position + "  ,  id  "+  id +"  ,  cou   ");

                    if (parent.getItemAtPosition(position).toString().equals("All Agents")) {

                        Log.e("DPSA","0");
                        AgentID="0";

                    } else {

                        AgentID=transactionsspinner.get(position-1).getId();
                        //SiteID = transactionsspinner.get(position-1).getId();

                    }
                    //HitApi();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            ArrayAdapter<String> countryAdapter;
            countryAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, AgentList);
            AgentSpinner.setAdapter(countryAdapter);
            //Log.e("DPSA",transactionsspinner.get(i).getProductName());

        } else {

            //image.setVisibility(View.VISIBLE);
            //horizantal.setVisibility(View.GONE);

        }

    }

    public void dataParsesStatusList() {
        Log.e("DPSA","Enter dataParsesProductList");

        StatusList.add("All Status");
        StatusList.add("Pending");
        StatusList.add("Approved");
        StatusList.add("Rejected");

        StatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Log.e("onItemSelected","  position   "+ position + "  ,  id  "+  id +"  ,  cou   ");

                if (parent.getItemAtPosition(position).toString().equals("All Status")) {

                    Log.e("DPSA","0");
                    Status="";

                } else {

                    Status=parent.getItemAtPosition(position).toString();
                    if(Status.equalsIgnoreCase("Approved"))
                    {
                        Status="Active";
                    }
                    //ProductID=transactionsspinner.get(position-1).getProductID();
                    //SiteID = transactionsspinner.get(position-1).getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        ArrayAdapter<String> countryAdapter;
        countryAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, StatusList);
        StatusSpinner.setAdapter(countryAdapter);

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