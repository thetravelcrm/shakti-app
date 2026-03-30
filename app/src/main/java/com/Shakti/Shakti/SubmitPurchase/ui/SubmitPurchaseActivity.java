package com.Shakti.Shakti.SubmitPurchase.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Shakti.Shakti.Dashbord.ui.GetLocation;
import com.Shakti.Shakti.Dashbord.ui.MainActivity;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SubmitPurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    String Districtlistid,Statelistid,Blocklistid="0",siteTypeID,Constructionid="0",Dealerid="0",Brandid="0",productid="0",IsPurchase="0";

    EditText Statelist,Districtlist,Blocklist,Constructionstage,Dealerlist,SitetypeList,BrandList;

    EditText ownerName,mobile,address,startDate,bagRequired,purchased,otherDealer,otherBrand;

    Loader loader;

    RegisterResponse transactions = new RegisterResponse();
    ArrayList<Datares> transactionsObjects = new ArrayList<>();

    SteatelistAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    Dialog dialog;
    RecyclerView recycler_viewpackage;
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();
    RegisterResponse transactionspack = new RegisterResponse();
    ProductAdapter mAdapterpackage;
    LinearLayoutManager mLayoutManagerpack;
    Button bt_submit;
    LinearLayout li_distic,Construction_li,product_li,li_block, li_DealerOther,li_BrandOther,li_PurchaseBlock,li_BrandBlock,li_ProductBlock;
    TextView tv_bagRequired;
    TextView tv_purchased;
    TextView txtAddPurchase;
    ImageView cutclera,cutclera1,closePurchase;
    GetLocation Locationn;
    Double latitude, longitude;
    boolean IsConfirm=false;
    private boolean IsLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_purchase);
        Locationn = new GetLocation(this);
        latitude = Locationn.getLatitude();
        longitude = Locationn.getLongitude();
        Getid();

    }

    private void Getid() {

        cutclera=findViewById(R.id.cutclera);
        cutclera.setOnClickListener(this);
        closePurchase=findViewById(R.id.closepurchase);
        closePurchase.setOnClickListener(this);
        cutclera1=findViewById(R.id.cutclera1);
        cutclera1.setOnClickListener(this);
        li_block=findViewById(R.id.li_block);
        li_distic=findViewById(R.id.li_distic);
        li_BrandOther=findViewById(R.id.li_BrandOther);
        li_DealerOther=findViewById(R.id.li_DealerOther);
        li_PurchaseBlock=findViewById(R.id.li_PurchaseBlock);
        li_BrandBlock=findViewById(R.id.li_BrandBlock);
        li_ProductBlock=findViewById(R.id.li_ProductBlock);
        tv_purchased=findViewById(R.id.tv_purchased);
        tv_bagRequired=findViewById(R.id.tv_bagRequired);
        Construction_li=findViewById(R.id.Construction_li);
        product_li=findViewById(R.id.product_li);

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

        recycler_viewpackage=findViewById(R.id.recycler_viewpackage);
        Statelist=findViewById(R.id.Statelist);
        Districtlist=findViewById(R.id.Districtlist);
        Blocklist=findViewById(R.id.Blocklist);

        Dealerlist=findViewById(R.id.Dealerlist);
        BrandList=findViewById(R.id.BrandList);
        Constructionstage=findViewById(R.id.Constructionstage);
        SitetypeList=findViewById(R.id.SitetypeList);
        bt_submit=findViewById(R.id.bt_submit);

        ownerName=findViewById(R.id.ownerName);
        mobile=findViewById(R.id.mobile);
        address=findViewById(R.id.address);
        startDate=findViewById(R.id.startDate);
        bagRequired=findViewById(R.id.bagRequired);
        purchased=findViewById(R.id.purchased);
        otherDealer=findViewById(R.id.otherDealer);
        otherBrand=findViewById(R.id.otherBrand);
        txtAddPurchase=findViewById(R.id.txtAddPurchase);

        Statelist.setOnClickListener(this);
        Districtlist.setOnClickListener(this);
        Blocklist.setOnClickListener(this);

        Constructionstage.setOnClickListener(this);
        Dealerlist.setOnClickListener(this);
        BrandList.setOnClickListener(this);
        SitetypeList.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        startDate.setOnClickListener(this);
        txtAddPurchase.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.NewSitePurchase);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });








    }

    private int mYear, mMonth, mDay;

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
                                startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }else if(ddd==1){

                                startDate.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }




                        }else if(b==1){
                            if(ddd==2){
                                startDate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                            }else if(ddd==1){

                                startDate.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                            }
                        }




                    //    startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();



       /* LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.datepicker_pop, null);

        Button tvLater =  view.findViewById(R.id.tv_later);
        Button tv_ok =  view.findViewById(R.id.tv_ok);
        final DatePicker  datePicker = (DatePicker) view.findViewById(R.id.date_picker);




        final Dialog dialog = new Dialog(this);

        dialog.setCancelable(false);
        dialog.setContentView(view);
        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Calendar today = Calendar.getInstance();
        long now = today.getTimeInMillis();
        datePicker.setMinDate(now);


        Date currentTime = Calendar.getInstance().getTime();


        String timewah=currentTime.toString().replace(" ",",");

        String[] recent;
        recent = timewah.split(",");


        Log.e("currentTime","currentTime :   "+ recent[3] );

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();


                String finaldate=(datePicker.getDayOfMonth()   + "-" + datePicker.getMonth())+ "-" + datePicker.getYear();

                String[] arrfinaldate;
                arrfinaldate = finaldate.split("-");


                if(arrfinaldate[1].length()==1){


                     startDate.setText( (datePicker.getDayOfMonth()   + "-0" + datePicker.getMonth())+ "-" + datePicker.getYear());


                }else if(arrfinaldate[1].length()==2){


                     startDate.setText( (datePicker.getDayOfMonth()   + "-" + datePicker.getMonth())+ "-" + datePicker.getYear());


                }



            }
        });



        tvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog.dismiss();
            }
        });



        dialog.show();*/
    }
    public void HitBlockList(String id) {

        if (UtilMethods.INSTANCE.isNetworkAvialable(SubmitPurchaseActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.BlockList(SubmitPurchaseActivity.this,id+"", loader,dialog);


        } else {
            UtilMethods.INSTANCE.NetworkError(SubmitPurchaseActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }
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
    public void HitSiteTypeList()
    {
        SharedPreferences sp=getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,MODE_PRIVATE);
        String at=sp.getString(ApplicationConstant.INSTANCE.setSiteTList,null);

        if(at=="" || at==null){
            if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.SitetypeList(this, loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }
        }
        else{
            at=""+sp.getString(ApplicationConstant.INSTANCE.setSiteTList,null);
            statepopup(at,"Site");

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
    public void HitStateList()
    {
        SharedPreferences sp=getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,MODE_PRIVATE);
        String at=sp.getString(ApplicationConstant.INSTANCE.setStateList,null);
        //Toast.makeText(this, at, Toast.LENGTH_SHORT).show();
        if(at=="" || at==null){
            if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.StateList(this, loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }
        }
        else{
            at=""+sp.getString(ApplicationConstant.INSTANCE.setStateList,null);
            statepopup(at,"State");
            li_distic.setVisibility(View.VISIBLE);
        }

    }
    public void ConfirmWithoutPurchase()
    {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(this);
        alertDialog.setTitle("Alert!");
        alertDialog.setContentText("Do you want to save site without purchase?");
        alertDialog.setCancelButton("Add Purchase", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismiss();
                IsPurchase="1";
                li_PurchaseBlock.setVisibility(View.VISIBLE);
                txtAddPurchase.setVisibility(View.GONE);
                HitDealerList();
                IsConfirm=false;
            }
        });

        alertDialog.setConfirmButton("Save Site", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                IsConfirm=true;
                SaveSite();
                alertDialog.dismiss();

            }
        });

        alertDialog.show();
    }
    public void SaveSite() {
        if (CheckLocation()) {
            if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.SubmitNewPurchase(this, "" + Districtlistid, Blocklistid, "" + siteTypeID, "" + Constructionid,
                        "" + Dealerid, "" + ownerName.getText().toString().trim(), "" + mobile.getText().toString().trim(),
                        "" + address.getText().toString().trim(), "" + startDate.getText().toString().trim(), "" + Brandid, "" +
                                bagRequired.getText().toString().trim(), "" + purchased.getText().toString().trim(), "" + productid, latitude.toString(), longitude.toString(), otherBrand.getText().toString().trim(), otherDealer.getText().toString().trim(), loader, this);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }
        }
    }
    @Override
    public void onClick(View view) {


        if(view==Districtlist){

            SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String setDistrictListval = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDistrictList, null);
            statepopup(setDistrictListval,"District");


        }
        if(view==Blocklist)
        {

            HitBlockList(Districtlistid);
        }
        if(view==cutclera){

            startDate.setText("");
            cutclera.setVisibility(View.GONE);

                }


        if(view==cutclera1){

            Brandid="0";
            li_BrandOther.setVisibility(View.GONE);
            BrandList.setText("Select Brand");


                }

          if(view==Constructionstage){

                    SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
                    String setConstructionval = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setConstruction, null);
                statepopup(setConstructionval,"Construction Stage");

                }

           if(view==startDate){



                    Datepicker();
            cutclera.setVisibility(View.VISIBLE);



                }


                if(view==bt_submit)
                {
                    if(ValidateData()) {


                            if(IsPurchase.trim()!="1") {
                                ConfirmWithoutPurchase();
                            }
                            else{
                        SaveSite();}
                    }
                }



                if(view==Statelist){

                HitStateList();


                }


                if(view==SitetypeList){


                    HitSiteTypeList();

                }


         if(view==Dealerlist) {
             HitDealerList();
         }



         if(view==BrandList){


             SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
             String setDistrictListval = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setProductCategory, null);
             statepopup(setDistrictListval,"Brand");


                }

         if(view==closePurchase)
         {
             IsPurchase="0";
             li_PurchaseBlock.setVisibility(View.GONE);
             txtAddPurchase.setVisibility(View.VISIBLE);
         }
         if(view==txtAddPurchase){
             IsPurchase="1";
             li_PurchaseBlock.setVisibility(View.VISIBLE);
             txtAddPurchase.setVisibility(View.GONE);
             HitDealerList();
        //     String heading=txtAddPurchase.getText().toString();
        //     if(heading=="ADD PURCHASE DETAILS"){
        //
        //         txtAddPurchase.setText("PURCHASE DETAILS");
        //     }
        //     else{
        //         li_PurchaseBlock.setVisibility(View.GONE);
        //         txtAddPurchase.setText("ADD PURCHASE DETAILS");
        // }
        }
    }

    public boolean ValidateData()
    {


        boolean i=true;
        if(Statelistid==null){

            Toast.makeText(this, "Please Select State", Toast.LENGTH_SHORT).show();

            HitStateList();
            i=false;

        } else if(Districtlistid==null){

            Toast.makeText(this, "Please Select District", Toast.LENGTH_SHORT).show();
            i=false;
        }
        else if(siteTypeID==null || siteTypeID.trim()=="0")
        {
            HitSiteTypeList();
            Toast.makeText(this, "Please Select Site Type", Toast.LENGTH_SHORT).show();
            i=false;
        }
        else if(Constructionid==null && Construction_li.getVisibility()==View.VISIBLE){

            Toast.makeText(this, "Please Select Construction Stage", Toast.LENGTH_SHORT).show();
            i=false;

        }
        else if(ownerName.getText().toString().trim().equalsIgnoreCase("")){
            ownerName.setError("Please Enter Site Owner Name");
            ownerName.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.showSoftInput(otherBrand, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            i=false;

        }
        else if(mobile.getText().toString().trim().equalsIgnoreCase("")){
            mobile.setError("Please Enter Site Owner's Mobile Number");
            mobile.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.showSoftInput(otherBrand, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            i=false;

        }
        else if(address.getText().toString().trim().equalsIgnoreCase("")){
            address.setError("Please Enter Site Address");
            address.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.showSoftInput(otherBrand, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            i=false;
        }
        else if(IsPurchase.trim()=="1")
        {

            if(Dealerid.trim()==null|| Dealerid.trim()=="0")
            {
                HitDealerList();
                Toast.makeText(this, "Please Select Dealer", Toast.LENGTH_SHORT).show();
                i=false;
            }
            else if(Dealerid.equalsIgnoreCase("-1") && otherDealer.getText().toString().trim().equalsIgnoreCase(""))
            {
                otherDealer.setError("Please Enter Other Dealer");
                otherDealer.requestFocus();

                i=false;
            }
            else
            {
                if(!Dealerid.equalsIgnoreCase("-1"))
                {
                    if (Brandid.trim() == "0" || Brandid.trim() == null)
                    {
                        Toast.makeText(this, "Please Select Product Type", Toast.LENGTH_SHORT).show();
                        HitProductCategoryList();
                        i = false;
                    }
                    else if (Brandid.equalsIgnoreCase("-1") && otherBrand.getText().toString().trim().equalsIgnoreCase("")) {
                        otherBrand.setError("Please Enter  Other Product");
                        otherBrand.requestFocus();
                        i = false;
                    }
                    else if (!Brandid.equalsIgnoreCase("-1"))
                    {
                        if (productid == null || productid == "0")
                        {
                            Toast.makeText(this, "Please Select Product", Toast.LENGTH_SHORT).show();
                            i = false;
                        }
                        else if (purchased.getText().toString().trim().equalsIgnoreCase("") || purchased.getText().toString().trim().equalsIgnoreCase("0")) {
                            purchased.setError(getResources().getString(R.string.err_msg_bagpurchased));
                            purchased.setText("");
                            purchased.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            //imm.showSoftInput(otherBrand, InputMethodManager.SHOW_IMPLICIT);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            i = false;
                        }
                    }
                }
            }

        }



        return i;

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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("StateList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"State");

            li_distic.setVisibility(View.VISIBLE);

        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("DistrictList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"District");
            li_block.setVisibility(View.VISIBLE);

        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("BlockList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"Block");

        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("SitetypeList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"Site");


        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("ConstructionStageList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"Construction Stage");

        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("DealerList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"Dealer");

        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("BrandList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"Brand");

        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("ProductList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            dataParsepackage(startelist);
        }

    }

    public void dataParsepackage(String response) {


        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, RegisterResponse.class);
        transactionsObjectspack = transactionspack.getData();

        if (transactionsObjectspack.size() > 0) {
            mAdapterpackage = new ProductAdapter(transactionsObjectspack, this,"2");
            mLayoutManagerpack = new LinearLayoutManager(this);
            recycler_viewpackage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
             recycler_viewpackage.setItemAnimator(new DefaultItemAnimator());
            recycler_viewpackage.setAdapter(mAdapterpackage);
            recycler_viewpackage.setVisibility(View.VISIBLE);
        } else {
            recycler_viewpackage.setVisibility(View.GONE);
        }
    }

    private void statepopup(String startelist, final String type) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.startuppop, null);



        RecyclerView recycleview = (RecyclerView) view.findViewById(R.id.recycler_view);
        ImageView cut =  view.findViewById(R.id.cut);
        TextView name =  view.findViewById(R.id.name);
        EditText area_serch =  view.findViewById(R.id.area_serch);
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

                String newText=""+s;

                Log.e("query",newText);
                newText=newText.toLowerCase();
                ArrayList<Datares> newlist=new ArrayList<>();
                for(Datares op:transactionsObjects)
                {

                    String getName="";

                    if(type.equalsIgnoreCase("State")){

                        getName=op.getName().toLowerCase();

                    }else if(type.equalsIgnoreCase("District")){

                        getName=op.getDistrictName().toLowerCase();

                    }else if(type.equalsIgnoreCase("Block")){

                        getName=op.getBlockName().toLowerCase();

                    }else if(type.equalsIgnoreCase("Construction Stage")){

                        getName=op.getName().toLowerCase();

                    }else if(type.equalsIgnoreCase("Dealer")){

                        getName=op.getName().toLowerCase();

                    }else if(type.equalsIgnoreCase("BrandList")){

                        getName=op.getName().toLowerCase();

                    }else if(type.equalsIgnoreCase("Brand")){

                        getName=op.getName().toLowerCase();

                    }else if(type.equalsIgnoreCase("Site")){

                        getName=op.getSitetype().toLowerCase();

                    }
                    else if(type.equalsIgnoreCase("Block")){
                        getName=op.getBlockName().toLowerCase();

                    }



                    if(getName.contains(newText)){

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
            mAdapter = new SteatelistAdapter(transactionsObjects, this,""+type,"2");
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

    public void ItemClickPurchase(String id) {

        if (UtilMethods.INSTANCE.isNetworkAvialable(SubmitPurchaseActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.DistrictList(SubmitPurchaseActivity.this,id+"", loader,dialog);

        } else {
            UtilMethods.INSTANCE.NetworkError(SubmitPurchaseActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }

    }

    public void ItemClickidSite(String id,String sitetype) {

        siteTypeID= ""+id;
        SitetypeList.setText(""+sitetype);
        Construction_li.setVisibility(View.GONE);
        if (UtilMethods.INSTANCE.isNetworkAvialable(SubmitPurchaseActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.Constructionstage(SubmitPurchaseActivity.this,id+"", loader,dialog);

        } else {
            UtilMethods.INSTANCE.NetworkError(SubmitPurchaseActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }

    }

    public void ItemClickidDealer(String id ,String name) {
        dialog.dismiss();
        Dealerid=id;
        Dealerlist.setText(""+ name);

        li_DealerOther.setVisibility(View.GONE);
        li_BrandBlock.setVisibility(View.GONE);
        if(id.equalsIgnoreCase("-1")){
            li_DealerOther.setVisibility(View.VISIBLE);
            otherDealer.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        else {

            SharedPreferences sp=getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,MODE_PRIVATE);
            String at=sp.getString(ApplicationConstant.INSTANCE.setProductCategory,null);
            //Toast.makeText(this, at, Toast.LENGTH_SHORT).show();
            if(at=="" || at==null){
                HitProductCategoryList();
            }
            else{
                at=""+sp.getString(ApplicationConstant.INSTANCE.setProductCategory,null);
                statepopup(at,"Brand");
            }
            li_BrandBlock.setVisibility(View.VISIBLE);
        }

    }

    public void ItemClickidBrandList(String id,String name) {

        dialog.dismiss();

        Brandid=id;
        BrandList.setText(""+ name);


        if(id.equalsIgnoreCase("-1")){
            li_ProductBlock.setVisibility(View.GONE);
            li_BrandOther.setVisibility(View.VISIBLE);
            otherBrand.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.showSoftInput(otherBrand, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        else
        {
            UtilMethods.INSTANCE.ProductByCategoryList(this,id, loader);
            li_ProductBlock.setVisibility(View.VISIBLE);
            li_BrandOther.setVisibility(View.GONE);}
    }

    public void ItemClickidPurchase(String districtID, String stateID, String districtName, String stateName) {

        dialog.dismiss();

        Blocklist.setText("");
        li_block.setVisibility(View.GONE);
        HitBlockList(districtID);

        Statelist.setText(""+stateName);
        Districtlist.setText(""+districtName);


        Districtlistid= ""+districtID;
        Statelistid= ""+stateID;

    }

    public void ItemClickidConstructionside(String siteTypeIDval, String sitetype, String Constructionidval, String Constructionname) {



        dialog.dismiss();


        //SitetypeList.setText(""+sitetype);
        Constructionstage.setText(""+Constructionname);

        //siteTypeID= ""+siteTypeIDval;
        Constructionid= ""+Constructionidval;
        Construction_li.setVisibility(View.VISIBLE);
    }

    public void ItemClickproduct(String id ,String unit) {

       //  Toast.makeText(this, "id  : "+  id  +"    unit   "  +  unit  , Toast.LENGTH_SHORT).show();

        product_li.setVisibility(View.VISIBLE);
        productid=id;

        tv_bagRequired.setText("Total "+unit+" Required");
        tv_purchased.setText(""+unit+" Purchased *");


    }

    public void ItemClickidBlockList(String id,String name) {

        dialog.dismiss();
        li_block.setVisibility(View.VISIBLE);
        Blocklistid=id;
        Blocklist.setText(""+ name);

    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i(TAG, "All location settings are satisfied.");
                        IsLocationPermissionGranted=true;
                        //Toast.makeText(this, "Please Enable Location", Toast.LENGTH_LONG).show();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            IsLocationPermissionGranted=false;
                            status.startResolutionForResult(SubmitPurchaseActivity.this, 1);
                        } catch (IntentSender.SendIntentException e) {
                            //Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
    public static Boolean isLocationEnabled(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }
    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Shakti app wants to change your device settings "
                + "\n\nSettings -> Select Shakti App Permissions -> Allow location permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        //builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Boolean CheckLocation()
    {
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
            displayNeverAskAgainDialog();
            return false;
        }
        else if(!isLocationEnabled(this))
        {
            displayLocationSettingsRequest(this);
            return  false;
        }
        Locationn = new GetLocation(this);
        latitude = Locationn.getLatitude();
        longitude = Locationn.getLongitude();
        if(latitude!=0.0){
            return true;
        }
        else {
            return false;
        }
    }
}
