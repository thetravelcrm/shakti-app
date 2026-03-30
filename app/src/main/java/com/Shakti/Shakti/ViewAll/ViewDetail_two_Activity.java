package com.Shakti.Shakti.ViewAll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.Shakti.Shakti.Dashbord.dto.DashBoardAgentApiRes;
import com.Shakti.Shakti.Dashbord.ui.MyAdapter;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.ReportAll.Activity.Helpernew;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class ViewDetail_two_Activity extends AppCompatActivity {

    Loader loader;
    MyAdapter adapter;
    String Type="SubAdmin";
    ListView Districtsub,Datesub,SiteNamesub,SiteOwnermobilenumb,SiteTypesub,AgentNamesub,AgentMobileNosub,verfy,li_Status;
    LinearLayout li_District,li_Date,li_SiteName,li_MobileNo,li_Sitetype;
    TextView TV_Date,TV_District,TV_SiteName,TV_MobileNo,TV_Sitetype,TV_AgentName,TV_AgentMobile;
    ArrayList<String> AgentNasub = new ArrayList<String>();
    ArrayList<String> siteNamesub = new ArrayList<String>();
    ArrayList<String> siteOwnersub = new ArrayList<String>();
    ArrayList<String> siteTypesub = new ArrayList<String>();
    ArrayList<String> DistrictNamesub = new ArrayList<String>();
    ArrayList<String> CreatedDatesub = new ArrayList<String>();
    ArrayList<String> MobileNosub = new ArrayList<String>();
    ArrayList<String> verfyid = new ArrayList<String>();
    ArrayList<String> Statuslist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_two_);
        Type=getIntent().getStringExtra("type");
        if(Type==null)
        {
            Type="SubAdmin";
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(Type!=null && Type.equalsIgnoreCase("Agent")){
            toolbar.setTitle(R.string.MySites);
        }
        else
        {toolbar.setTitle(R.string.VerifiedSites);}
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GetId();
    }

    private void GetId() {
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        Districtsub=findViewById(R.id.Districtsub);
        Datesub=findViewById(R.id.Datesub);
        SiteNamesub=findViewById(R.id.SiteNamesub);
        SiteOwnermobilenumb=findViewById(R.id.SiteOwnermobilenumb);
        SiteTypesub=findViewById(R.id.SiteTypesub);
        AgentNamesub=findViewById(R.id.AgentNamesub);
        AgentMobileNosub=findViewById(R.id.AgentMobileNosub);
        verfy=findViewById(R.id.verify);
        li_Status=findViewById(R.id.li_Status);
        li_District=findViewById(R.id.li_District);

        li_Date=findViewById(R.id.li_Date);
        li_SiteName=findViewById(R.id.li_SiteName);
        li_MobileNo=findViewById(R.id.li_MobileNo);
        li_Sitetype=findViewById(R.id.li_Sitetype);

        TV_Date=findViewById(R.id.TV_Date);
        TV_District=findViewById(R.id.TV_District);
        TV_SiteName=findViewById(R.id.TV_SiteName);
        TV_MobileNo=findViewById(R.id.TV_MobileNo);
        TV_Sitetype=findViewById(R.id.TV_Sitetype);
        TV_AgentName=findViewById(R.id.TV_AgentName);
        TV_AgentMobile=findViewById(R.id.TV_AgentMobile);
        HitAPI();
    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {

        if (activityFragmentMessage.getMessage().equalsIgnoreCase("SiteReport")) {

            String startelist=""+activityFragmentMessage.getFrom();

            dataParsepackageSite(startelist);
            //dataParsepackageSitepending(startelist);

        }

    }
    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    public void dataParsepackageSite(String response) {

        Gson gson = new Gson();
        RegisterResponse transactionspack = gson.fromJson(response, RegisterResponse.class);

        Log.e("SiteReportres", "is : " + response);
        if (transactionspack.getList() != null && transactionspack.getList().size() > 0) {



                for (int i = 0; i < transactionspack.getList().size(); i++) {

                    if(Type.equalsIgnoreCase("Agent"))
                    {
                        Statuslist.add(transactionspack.getList().get(i).getSiteStatus());
                        CreatedDatesub.add(transactionspack.getList().get(i).getDate());

                    }
                    else
                    {
                        Statuslist.add(transactionspack.getList().get(i).getSubAdminStatus());
                        CreatedDatesub.add(transactionspack.getList().get(i).getCreatedDate());

                    }
                    DistrictNamesub.add(transactionspack.getList().get(i).getDistrictName());
                    //AgentNasub.add(transactionspack.getList().get(i).getSiteName());
                    siteNamesub.add(transactionspack.getList().get(i).getSiteName());
                    siteTypesub.add(transactionspack.getList().get(i).getSiteType());

                    //MobileNosub.add(transactionspack.getList().get(i).getMobile());
                    verfyid.add(transactionspack.getList().get(i).getId());

                    siteOwnersub.add(transactionspack.getList().get(i).getMobile());

                }





            SiteNamesub.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, siteNamesub));
            Helpernew.getListViewSize(SiteNamesub);
            TV_SiteName.measure(0,0);
            Helpernew.setListViewWidth(SiteNamesub,li_SiteName,TV_SiteName.getMeasuredWidth());

            Districtsub.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, DistrictNamesub));
            TV_District.measure(0,0);
            Helpernew.getListViewSize(Districtsub);
            Helpernew.setListViewWidth(Districtsub,li_District,TV_District.getMeasuredWidth());

            Datesub.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, CreatedDatesub));
            TV_Date.measure(0,0);
            Helpernew.getListViewSize(Datesub);
            Helpernew.setListViewWidth(Datesub,li_Date,TV_Date.getMeasuredWidth());

            SiteTypesub.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, siteTypesub));
            TV_Sitetype.measure(0,0);
            Helpernew.getListViewSize(SiteTypesub);
            Helpernew.setListViewWidth(SiteTypesub,li_Sitetype,TV_Sitetype.getMeasuredWidth());
//
            SiteOwnermobilenumb.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, siteOwnersub));
            TV_MobileNo.measure(0,0);
            Helpernew.getListViewSize(SiteOwnermobilenumb);
            Helpernew.setListViewWidth(SiteOwnermobilenumb,li_MobileNo,TV_MobileNo.getMeasuredWidth());
//
//
//
//
//
////            AgentMobileNosub.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, MobileNosub));
////            Helpernew.getListViewSize(AgentMobileNosub);
////            Helpernew.setListViewWidth(AgentMobileNosub,li_SiteName,100);
//
            Log.e("SiteReportres", "Statuslist : " + Statuslist.size());
            if(Statuslist!=null){
            adapter = new MyAdapter(this, Statuslist,"5");
            li_Status.setAdapter(adapter);}
//
//
            adapter = new MyAdapter(this, verfyid,"1");
            verfy.setAdapter(adapter);



        }
        else {

            //LI_PENDINGSITES.setVisibility(View.GONE);


        }

    }

    public void HitAPI()
    {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);
            if(Type.equalsIgnoreCase("Agent"))
            {
                UtilMethods.INSTANCE.SiteReport(this, loader);
            }
            else {
                UtilMethods.INSTANCE.SubAdminSitesReport(this,  loader);
            }


        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }


    }
}
