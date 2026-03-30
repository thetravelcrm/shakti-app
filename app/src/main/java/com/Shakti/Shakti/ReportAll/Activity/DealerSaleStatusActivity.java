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
import com.Shakti.Shakti.ReportAll.Adapter.ProductPurchaseDealerAdapter;
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

public class DealerSaleStatusActivity extends AppCompatActivity implements View.OnClickListener {
    Loader loader;
    RecyclerView recycler_view;
    HorizontalScrollView horizantal;
    ImageView image,closesearch,opensearch;
    ListView ListSno,ListEntryDate,ListPurchaseDate,ListQty,ListProduct,ListStatus,ListRemark;
    LinearLayout LIProduct,LIQty,LIPurchaseDate,LIEntryDate,LIStatus,LIRemark;
    TextView TV_Product,TV_Qty,TV_PurchaseDate,TV_EntryDate,TV_Remark;
    Button bt_search;
    Spinner ProductSpinner,StatusSpinner,AgentSpinner;
    ProductPurchaseDealerAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    LinearLayout LI_filter,LI_filterOpen;
    RegisterResponse transactionspack = new RegisterResponse();
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();

    RegisterResponse transactions = new RegisterResponse();
    ArrayList<Datares> transactionsspinner = new ArrayList<>();
    ArrayList<String> ProductList = new ArrayList<String>();
    
    
    ArrayList<String> SnoArray = new ArrayList<String>();
    ArrayList<String> StatusArray = new ArrayList<String>();
    ArrayList<String> EntryDateArray = new ArrayList<String>();
    ArrayList<String> PurchaseDateArray = new ArrayList<String>();
    ArrayList<String> QtyArray = new ArrayList<String>();
    ArrayList<String> ProductArray = new ArrayList<String>();
    ArrayList<String> RemarkArray = new ArrayList<String>();
    String ProductID="0",AgentID="0",Status="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_sale_status);

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
        
        ListSno=findViewById(R.id.ListSNo);
        ListStatus=findViewById(R.id.ListStatus);
        ListEntryDate=findViewById(R.id.ListEntryDate);
        ListPurchaseDate=findViewById(R.id.ListPurchaseDate);
        ListQty=findViewById(R.id.ListQty);
        ListProduct=findViewById(R.id.ListProduct);
        ListRemark=findViewById(R.id.ListRemark);
        LIProduct=findViewById(R.id.LIProduct);
        LIStatus=findViewById(R.id.LIStatus);
        LIQty=findViewById(R.id.LIQty);
        LIPurchaseDate=findViewById(R.id.LIPurchaseDate);
        LIEntryDate=findViewById(R.id.LIEntryDate);
        LIRemark=findViewById(R.id.LIRemark);

        TV_Product=findViewById(R.id.TV_Product);
        TV_Qty=findViewById(R.id.TV_Qty);
        TV_PurchaseDate=findViewById(R.id.TV_PurchaseDate);
        TV_EntryDate=findViewById(R.id.TV_EntryDate);
        TV_Remark=findViewById(R.id.TV_Remark);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.PurchaseReport);
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
    }
    private void HitApi() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.SaleDealerReport(this,ProductID, loader,horizantal,image);

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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("SaleDealerReport")) {
            String startelist=""+activityFragmentMessage.getFrom();

            dataParse(startelist);

        }else if(activityFragmentMessage.getMessage().equalsIgnoreCase("ProductList")){
            String productlist=""+activityFragmentMessage.getFrom();
            dataParsesProductList(productlist);

        }




    }


    public void dataParse(String response) {

        Gson gson = new Gson();
        RegisterResponse transactionspack = gson.fromJson(response, RegisterResponse.class);
        Log.i("DealerReport",response);
        Integer Sno=1;
        if (transactionspack.getList().size() > 0) {

            if (transactionspack.getList() != null && transactionspack.getList().size() > 0) {

                for (int i = 0; i < transactionspack.getList().size(); i++) {


                    SnoArray.add(Sno.toString());
                    StatusArray.add(transactionspack.getList().get(i).getPurchaseStatus());
                    ProductArray.add(transactionspack.getList().get(i).getProductName());
                    QtyArray.add(transactionspack.getList().get(i).getQuantity());
                    PurchaseDateArray.add(transactionspack.getList().get(i).getPurchaseDate());
                    EntryDateArray.add(transactionspack.getList().get(i).getCreatedDate());
                    RemarkArray.add(transactionspack.getList().get(i).getRemark());

                    Sno++;
                }

            }

            ListSno.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, SnoArray));
            MyAdapter adapter = new MyAdapter(this, StatusArray,"3");
            ListStatus.setAdapter(adapter);

            ListProduct.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, ProductArray));
            TV_Product.measure(0,0);
            Helpernew.getListViewSize(ListProduct);
            Helpernew.setListViewWidth(ListProduct,LIProduct,TV_Product.getMeasuredWidth());

            ListQty.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, QtyArray));
            TV_Qty.measure(0,0);
            Helpernew.getListViewSize(ListQty);
            Helpernew.setListViewWidth(ListQty,LIQty,TV_Qty.getMeasuredWidth());

            ListPurchaseDate.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, PurchaseDateArray));
            TV_PurchaseDate.measure(0,0);
            Helpernew.getListViewSize(ListPurchaseDate);
            Helpernew.setListViewWidth(ListPurchaseDate,LIPurchaseDate,TV_PurchaseDate.getMeasuredWidth());

            ListEntryDate.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, EntryDateArray));
            TV_EntryDate.measure(0,0);
            Helpernew.getListViewSize(ListEntryDate);
            Helpernew.setListViewWidth(ListEntryDate,LIEntryDate,TV_EntryDate.getMeasuredWidth());

            ListRemark.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, RemarkArray));
            TV_Remark.measure(0,0);
            Helpernew.getListViewSize(ListRemark);
            Helpernew.setListViewWidth(ListRemark,LIRemark,TV_Remark.getMeasuredWidth());

        } else {

            //LI_PENDINGSITES.setVisibility(View.GONE);


        }

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