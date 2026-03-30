package com.Shakti.Shakti.Dashbord.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Shakti.Shakti.Dashbord.ui.VerifySiteActivity;
import com.Shakti.Shakti.Map.MapResponse;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.ReportAll.Activity.PurchaseStatusActivity;
import com.Shakti.Shakti.SubmitPurchase.dto.SiteResponse;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ViewsitedetailsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener{
    Loader loader;
    Dialog dialog;
    String SiteID="0",Lat="",Long="",MapTitle,MapSnippt;
    EditText txtAgentMobile,txtAgentName,txtaddress,txtblock,txtStartDate,txtMobile,txtSiteName,txtSiteType,txtStatus,txtRemark;
    TextView txtdistrict,txtstate;
    ImageView btnCapture,imgSitePhoto;
    LinearLayout Li_Extra;
    SiteResponse siteResponse=new SiteResponse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewsitedetails);
        GetID();
    }

    public void GetID()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.SiteDetails);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtAgentMobile=findViewById(R.id.txtAgentMobile);
        txtAgentName=findViewById(R.id.txtAgentName);
        txtaddress=findViewById(R.id.txtaddress);
        txtblock=findViewById(R.id.txtblock);
        txtdistrict=findViewById(R.id.txtdistrict);
        txtstate=findViewById(R.id.txtstate);
        txtStartDate=findViewById(R.id.txtStartDate);
        txtMobile=findViewById(R.id.txtMobile);
        txtSiteName=findViewById(R.id.txtSiteName);
        txtSiteType=findViewById(R.id.txtSiteType);
        txtRemark=findViewById(R.id.txtRemark);
        txtStatus=findViewById(R.id.txtStatus);
        btnCapture=findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(this);
        imgSitePhoto=findViewById(R.id.imgSitePhoto);
        Li_Extra=findViewById(R.id.Li_Extra);

        SiteID=getIntent().getStringExtra("id");
        loader = new Loader(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        HitSiteAPI();

    }

    @Override
    public void onClick(View view) {
        if(view==btnCapture)
        {

            startActivity(new Intent(this, VerifySiteActivity.class).putExtra("id",SiteID));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        Log.i("Map","MapReady");
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.mapsite);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap siteMarker = Bitmap.createScaledBitmap(b, width, height, false);

        if(Lat==null || Lat.equalsIgnoreCase("")) {
            
        }
        else {
            LatLng ll = new LatLng(Double.parseDouble(Lat), Double.parseDouble(Long));
            googleMap.addMarker(new MarkerOptions()
                    .position(ll)
                    .anchor(0.5f, 0.5f)
                    .snippet(MapSnippt)
                    .icon(BitmapDescriptorFactory.fromBitmap(siteMarker))
                    .title(MapTitle));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
        }

    }
    public void HitSiteAPI()
    {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);
            UtilMethods.INSTANCE.SiteDetails(this, SiteID, loader, ViewsitedetailsActivity.this);


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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("SiteDetails")) {
            String startelist=""+activityFragmentMessage.getFrom();
            Log.i("Map","SiteDetails-"+startelist);
            if(!startelist.equalsIgnoreCase("")) {
                siteResponse = new Gson().fromJson(startelist, SiteResponse.class);

                if (siteResponse.getStatus().equalsIgnoreCase("1")) {
                    SiteResponse.SiteList list=siteResponse.getList().get(0);
                    Lat=list.getLatAgent();
                    Long= list.getLongAgent();
                    MapTitle="SITE - "+list.getOwnerName();
                    MapSnippt=list.getAddress();
                    txtSiteType.setText(list.getSiteType());
                    txtSiteName.setText(list.getOwnerName());
                    txtMobile.setText(list.getMobile());
                    txtStartDate.setText(list.getStartDate());
                    txtdistrict.setText(list.getDistrictName());
                    txtstate.setText(list.getStateName());
                    txtblock.setText(list.getBlockName());
                    txtaddress.setText(list.getAddress());
                    txtAgentName.setText(list.getAgentName());
                    txtAgentMobile.setText(list.getAgentMobile());
                    if(list.getSubAdminStatus().equalsIgnoreCase("active"))
                    {
                        txtStatus.setText("Approved");
                    }
                    else
                    {
                        txtStatus.setText("Rejected");
                    }
                    //txtStatus.setText(list.getSubAdminStatus());
                    txtRemark.setText(list.getSubAdminRemark());
                    Glide.with(this).load(list.getPhoto()).into(imgSitePhoto);
                    SetMapHeight();
                    if(list.getSubAdminStatus().equalsIgnoreCase("pending"))
                    {
                        Li_Extra.setVisibility(View.GONE);
                        btnCapture.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        btnCapture.setVisibility(View.GONE);
                        Li_Extra.setVisibility(View.VISIBLE);
                    }
                }
            }
        }


    }

    public void SetMapHeight()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        Log.e("ScreenSize","height : "+height);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = (height *30)/100;

        Log.e("ScreenSize","params.height : "+params.height);
        mapFragment.getView().setLayoutParams(params);
    }
}