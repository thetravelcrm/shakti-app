package com.Shakti.Shakti.SubmitPurchase.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Register.ui.SteatelistAdapter;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

public class DealerpurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    RegisterResponse transactions = new RegisterResponse();
    ArrayList<Datares> transactionsObjects = new ArrayList<>();
    SteatelistAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    RegisterResponse transactionspack = new RegisterResponse();
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();
    RecyclerView recycler_viewpackage;
    ProductAdapter mAdapterpackage;
    LinearLayoutManager mLayoutManagerpack;
    Loader loader;
    LinearLayout li_ProductBlock;
    EditText purchased,BrandList,purchaseDate;
    TextView Purchased_ytv;
    ImageView cutclera;
    Dialog dialog;
    String productid="0";
    String Brandid="0";
    private int mYear, mMonth, mDay;
    Button bt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealerpurchase);
        Getid();
    }


    private void Getid() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.SubmitPurchase);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        BrandList=findViewById(R.id.BrandList);
        BrandList.setOnClickListener(this);
        purchaseDate=findViewById(R.id.purchaseDate);
        purchaseDate.setOnClickListener(this);
        cutclera=findViewById(R.id.cutclera);
        cutclera.setOnClickListener(this);
        cutclera.setVisibility(View.GONE);
        purchased=findViewById(R.id.purchased);
        Purchased_ytv=findViewById(R.id.Purchased_ytv);
        li_ProductBlock=findViewById(R.id.li_ProductBlock);
        recycler_viewpackage=findViewById(R.id.recycler_viewpackage);
        bt_submit=findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        HitProductCategoryList();


    }

    public void HitProductCategoryList()
    {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.ProductCategoryList(this, loader);

        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }
    }

    @Override
    public void onClick(View view) {
        if(view==BrandList){
            SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String setDistrictListval = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setProductCategory, null);
            statepopup(setDistrictListval,"Brand");
        }
        if(view==purchaseDate)
        {
            Datepicker();
            cutclera.setVisibility(View.VISIBLE);
        }
        if(view==cutclera)
        {
            purchaseDate.setText("");
            cutclera.setVisibility(View.GONE);
        }
        if(view==bt_submit)
        {
            //UtilMethods.INSTANCE.Successful(this, "Abhi API nhi bani hai sirf design check krni hai",5,DealerpurchaseActivity.this);
            UtilMethods.INSTANCE.SubmitDealerPurchase(this,purchased.getText().toString(),productid,purchaseDate.getText().toString(),loader,DealerpurchaseActivity.this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    public void Datepicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                        int in=(monthOfYear + 1);

                        int dd=dayOfMonth;

                        int b=Integer.toString(in).length();
                        int ddd=Integer.toString(dd).length();


                        if(b==2){

                            if(ddd==2){
                              purchaseDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }else if(ddd==1){

                                purchaseDate.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }




                        }else if(b==1){
                            if(ddd==2){
                                purchaseDate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                            }else if(ddd==1){

                                purchaseDate.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                            }
                        }




                        //    startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }

    public void dataParsepackage(String response) {


        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, RegisterResponse.class);
        transactionsObjectspack = transactionspack.getData();

        if (transactionsObjectspack.size() > 0) {
            mAdapterpackage = new ProductAdapter(transactionsObjectspack, this,"3");
            mLayoutManagerpack = new LinearLayoutManager(this);
            recycler_viewpackage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recycler_viewpackage.setItemAnimator(new DefaultItemAnimator());
            recycler_viewpackage.setAdapter(mAdapterpackage);
            recycler_viewpackage.setVisibility(View.VISIBLE);
        } else {
            recycler_viewpackage.setVisibility(View.GONE);
        }
    }
    private void statepopup(final String startelist, final String type) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.startuppop, null);

        final RecyclerView recycleview = (RecyclerView) view.findViewById(R.id.recycler_view);
        final ImageView cut =  view.findViewById(R.id.cut);
        TextView name =  view.findViewById(R.id.name);
        EditText area_serch=view.findViewById(R.id.area_serch);
        name.setText(""+ type);


        dialog = new Dialog(this);

        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        area_serch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                String newText = "" + s;

                Log.e("query", newText);
                newText = newText.toLowerCase();
                ArrayList<Datares> newlist = new ArrayList<>();
                for (Datares op : transactionsObjects) {

                    String getName = "";


                    if (type.equalsIgnoreCase("Brand")) {

                        getName = op.getName().toLowerCase();

                    }
                    //else if (type.equalsIgnoreCase("Dealer")) {
//
//                        getName = op.getName().toLowerCase();
//
//                    }else if (type.equalsIgnoreCase("Site")) {
//
//                        getName = op.getName().toLowerCase();
//
//                    }


                    if (getName.contains(newText)) {

                        newlist.add(op);

                    }
                }
                mAdapter.filter(newlist);

            }
        });



        Gson gson = new Gson();
        transactions = gson.fromJson(startelist, RegisterResponse.class);
        transactionsObjects = transactions.getData();

        if (transactionsObjects.size() > 0) {
            mAdapter = new SteatelistAdapter(transactionsObjects, this,""+type,"4");
            mLayoutManager = new LinearLayoutManager(this);
            recycleview.setLayoutManager(mLayoutManager);
            recycleview.setItemAnimator(new DefaultItemAnimator());
            recycleview.setAdapter(mAdapter);

            recycleview.setVisibility(View.VISIBLE);
        } else {
            recycleview.setVisibility(View.GONE);
        }

        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();
    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("BrandList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"Brand");

        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("ProductList")) {
            String startelist=""+activityFragmentMessage.getFrom();

            dataParsepackage(startelist);
        }
    }

    public void ItemClickidBrandList(String id,String name) {

        dialog.dismiss();

        Brandid=id;
        BrandList.setText(""+ name);


        if(id.equalsIgnoreCase("-1")){
            li_ProductBlock.setVisibility(View.GONE);
            bt_submit.setVisibility(View.GONE);
        }
        else {
            UtilMethods.INSTANCE.ProductByCategoryList(this, id, loader);
            li_ProductBlock.setVisibility(View.VISIBLE);
            bt_submit.setVisibility(View.VISIBLE);
        }
    }

    public void ItemClickproduct(String id,String Uni) {
        productid=id;

        Purchased_ytv.setText(""+Uni+" Purchased");

    }
}