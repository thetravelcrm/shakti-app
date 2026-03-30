package com.Shakti.Shakti.SubmitPurchase.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Register.ui.RegisterActivity;
import com.Shakti.Shakti.Register.ui.SteatelistAdapter;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class ExistingActivity extends AppCompatActivity implements View.OnClickListener {


    RecyclerView recycler_viewpackage;
    Loader loader;

    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();
    RegisterResponse transactionspack = new RegisterResponse();
    ProductAdapter mAdapterpackage;
    LinearLayoutManager mLayoutManagerpack;
    LinearLayout li_ProductBlock;
    EditText SitetypeList,Constructionstage,DealerList,purchased,BrandList;

    RegisterResponse transactions = new RegisterResponse();
    ArrayList<Datares> transactionsObjects = new ArrayList<>();

    SteatelistAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    Dialog dialog;

    String siteTypeID="";
    String Constructionid="";
    String productid="";
    String DealerListid="";
    String Brandid="0";
    LinearLayout Constructionlin;
    Button bt_submit;
    TextView Purchased_ytv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing);

        Getid();

    }


    private void Getid() {

        bt_submit=findViewById(R.id.bt_submit);
        Purchased_ytv=findViewById(R.id.Purchased_ytv);
        bt_submit.setOnClickListener(this);

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

        recycler_viewpackage=findViewById(R.id.recycler_viewpackage);

        SitetypeList=findViewById(R.id.SitetypeList);
        Constructionstage=findViewById(R.id.Constructionstage);
        DealerList=findViewById(R.id.DealerList);
        purchased=findViewById(R.id.purchased);
        Constructionlin=findViewById(R.id.Constructionlin);
        li_ProductBlock=findViewById(R.id.li_ProductBlock);
        BrandList=findViewById(R.id.BrandList);
        SitetypeList.setOnClickListener(this);
        Constructionstage.setOnClickListener(this);
        DealerList.setOnClickListener(this);
        purchased.setOnClickListener(this);
        BrandList.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.ExistingSitePurchase);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
    public void HitDealerList()
    {
        SharedPreferences sp=getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,MODE_PRIVATE);
        String at=sp.getString(ApplicationConstant.INSTANCE.setDealer,null);
        //Toast.makeText(this, at, Toast.LENGTH_SHORT).show();
        if(at=="" || at==null){
            if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);
                UtilMethods.INSTANCE.DealerList(this, loader);
            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }
        }
        else{
            at=""+sp.getString(ApplicationConstant.INSTANCE.setDealer,null);
            statepopup(at,"Dealer");

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
         if (activityFragmentMessage.getMessage().equalsIgnoreCase("ProductList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            dataParsepackage(startelist);

        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("SiteList")) {
             String startelist=""+activityFragmentMessage.getFrom();
             statepopup(startelist,"Site");

         }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("ConstructionStageList")) {
             String startelist=""+activityFragmentMessage.getFrom();
             statepopup(startelist,"Construction Stage");

         }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("DealerList")) {
             String startelist=""+activityFragmentMessage.getFrom();
             statepopup(startelist,"Dealer");

         }

    }
    
    public void dataParsepackage(String response) {

        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, RegisterResponse.class);
        transactionsObjectspack = transactionspack.getData();

        if (transactionsObjectspack.size() > 0) {
            mAdapterpackage = new ProductAdapter(transactionsObjectspack, this,"1");
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


                   if (type.equalsIgnoreCase("Construction Stage")) {

                        getName = op.getName().toLowerCase();

                    } else if (type.equalsIgnoreCase("Dealer")) {

                        getName = op.getName().toLowerCase();

                    }else if (type.equalsIgnoreCase("Site")) {

                        getName = op.getName().toLowerCase();

                    }


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
            mAdapter = new SteatelistAdapter(transactionsObjects, this,""+type,"21");
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

    public void ItemClickproduct(String id,String Uni) {
        productid=id;

        Purchased_ytv.setText(""+Uni+" Purchased");

    }


    @Override
    public void onClick(View view) {


        if(view==Constructionstage){

            SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String setConstructionval = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setConstruction, null);
             statepopup(setConstructionval,"Construction Stage");

        }

        if(view==SitetypeList){


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

  if(view==bt_submit){



          if(siteTypeID.equalsIgnoreCase("")){

              Toast.makeText(this, "please Select Site  ", Toast.LENGTH_SHORT).show();
              
          }else if(DealerListid.equalsIgnoreCase("")){

              Toast.makeText(this, "please Select Dealer  ", Toast.LENGTH_SHORT).show();

          }else if(productid.equalsIgnoreCase("")){

              Toast.makeText(this, "please Select Product ", Toast.LENGTH_SHORT).show();

          }else {

              if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                  loader.show();
                  loader.setCancelable(false);
                  loader.setCanceledOnTouchOutside(false);


                  UtilMethods.INSTANCE.SubmitExistingPurchase(this,""+Constructionid,""+DealerListid,""+purchased.getText().toString().trim(),""+productid,""+siteTypeID, loader,this);

              } else {
                  UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                          getResources().getString(R.string.network_error_message));
              }


          }



        }


       if(view==DealerList){


           HitDealerList();

         /*  if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

               loader.show();
               loader.setCancelable(false);
               loader.setCanceledOnTouchOutside(false);

               UtilMethods.INSTANCE.DealerList(this, loader);

           } else {
               UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                       getResources().getString(R.string.network_error_message));
           }*/





        }

        if(view==BrandList){


            SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String setDistrictListval = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setProductCategory, null);
            statepopup(setDistrictListval,"Brand");


        }

    }

    public void ItemClickSite(String id,String finalsiteid,String name) {

        siteTypeID=finalsiteid;
         SitetypeList.setText(""+name);

        Constructionlin.setVisibility(View.VISIBLE);


        if (UtilMethods.INSTANCE.isNetworkAvialable(ExistingActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.Constructionstage(ExistingActivity.this,id+"", loader,dialog);

        } else {
            UtilMethods.INSTANCE.NetworkError(ExistingActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }

    }

    public void ItemClickidConstructionside(String siteTypeIDval, String sitetype, String Constructionidval, String Constructionname) {

        dialog.dismiss();


      //  SitetypeList.setText(""+sitetype);
        Constructionstage.setText(""+Constructionname);

      //  siteTypeID= ""+siteTypeIDval;
        Constructionid= ""+Constructionidval;

    }

    public void ItemClickidDealer(String id, String name) {
        dialog.dismiss();


        DealerList.setText(""+name);

        DealerListid=id;


    }

    public void ItemClickidBrandList(String id,String name) {

        dialog.dismiss();

        Brandid=id;
        BrandList.setText(""+ name);


        if(id.equalsIgnoreCase("-1")){
            li_ProductBlock.setVisibility(View.GONE);

        }
        else {
            UtilMethods.INSTANCE.ProductByCategoryList(this, id, loader);
            li_ProductBlock.setVisibility(View.VISIBLE);
        }
    }
}
