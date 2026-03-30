package com.Shakti.Shakti.ReportAll.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Shakti.Shakti.Dashbord.ui.MyAdapter;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class DealerReportActivity extends AppCompatActivity {
    Loader loader;
    MyAdapter adapter;
    ImageView ImgNoData;
    HorizontalScrollView scrollView;
    ListView ListSno,ListAgentType,ListAgent,ListMobile,ListDistrict,ListBlock,ListCompanyName;
    LinearLayout lIBlock,lIDistrict,lIMobile,lIAgent,LIAgentType,lICompanyName;
    TextView txtAgent;
    ArrayList<String> SnoArray = new ArrayList<String>();
    ArrayList<String> AgentTypeArray = new ArrayList<String>();
    ArrayList<String> AgentArray = new ArrayList<String>();
    ArrayList<String> MobileArray = new ArrayList<String>();
    ArrayList<String> DistrictArray = new ArrayList<String>();
    ArrayList<String> BlockArray = new ArrayList<String>();
    ArrayList<String> CompanyNameArray = new ArrayList<String>();
    String Type="Agent";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Type= getIntent().getStringExtra("Type");
        if(Type.equalsIgnoreCase("Dealer")){
            toolbar.setTitle(R.string.DealerList);
        }
        else{toolbar.setTitle(R.string.AgentList);}
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
        ListSno=findViewById(R.id.ListSno);
        ListAgentType=findViewById(R.id.ListAgentType);
        ListAgent=findViewById(R.id.ListAgent);
        ListMobile=findViewById(R.id.ListMobile);
        ListDistrict=findViewById(R.id.ListDistrict);
        ListBlock=findViewById(R.id.ListBlock);
        ListCompanyName=findViewById(R.id.ListCompanyName);
        lIBlock=findViewById(R.id.lIBlock);
        lIDistrict=findViewById(R.id.lIDistrict);
        lIMobile=findViewById(R.id.lIMobile);
        lIAgent=findViewById(R.id.lIAgent);
        LIAgentType=findViewById(R.id.LIAgentType);
        lICompanyName=findViewById(R.id.lICompanyName);
        scrollView=findViewById(R.id.scrollView);
        txtAgent=findViewById(R.id.txtAgent);
        if(Type.equalsIgnoreCase("Dealer")){
            txtAgent.setText(R.string.Dealer);
        }
        else{txtAgent.setText(R.string.Agent);}
        HitAPI();
    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {

        if (activityFragmentMessage.getMessage().equalsIgnoreCase("DealerReport")) {

            String startelist=""+activityFragmentMessage.getFrom();

            dataParsepackageSite(startelist);
            //dataParsepackageSitepending(startelist);

        }
        else if (activityFragmentMessage.getMessage().equalsIgnoreCase("AgentReport")) {

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
        Log.i("DealerReport",response);
        Integer Sno=1;
        if (transactionspack.getList().size() > 0) {

            if (transactionspack.getList() != null && transactionspack.getList().size() > 0) {

                for (int i = 0; i < transactionspack.getList().size(); i++) {


                    SnoArray.add(Sno.toString());
                    if(Type.equalsIgnoreCase("Agent")) {
                        AgentTypeArray.add(transactionspack.getList().get(i).getagentType());
                    }
                    else
                    {
                        CompanyNameArray.add(transactionspack.getList().get(i).getagentType());
                    }
                    AgentArray.add(transactionspack.getList().get(i).getDealerName());
                    MobileArray.add(transactionspack.getList().get(i).getMobile());
                    DistrictArray.add(transactionspack.getList().get(i).getDistrictName());
                    BlockArray.add(transactionspack.getList().get(i).getBlockName());

                    Sno++;
                }

            }

            ListSno.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, SnoArray));

            if(Type.equalsIgnoreCase("Agent")) {
                ListAgentType.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_listview, AgentTypeArray));
                Helpernew.getListViewSize(ListAgentType);
                Helpernew.setListViewWidth(ListAgentType, LIAgentType, 100);
                LIAgentType.setVisibility(View.VISIBLE);
                lICompanyName.setVisibility(View.GONE);
            }
            else
            {
                ListCompanyName.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, CompanyNameArray));
                Helpernew.getListViewSize(ListCompanyName);
                Helpernew.setListViewWidth(ListCompanyName,lICompanyName,250);
                lICompanyName.setVisibility(View.VISIBLE);
                LIAgentType.setVisibility(View.GONE);
            }
            ListAgent.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, AgentArray));
            Helpernew.getListViewSize(ListAgent);
            Helpernew.setListViewWidth(ListAgent,lIAgent,100);

            ListMobile.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, MobileArray));
            Helpernew.getListViewSize(ListMobile);
            Helpernew.setListViewWidth(ListMobile,lIMobile,100);

            ListDistrict.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, DistrictArray));
            Helpernew.getListViewSize(ListDistrict);
            Helpernew.setListViewWidth(ListDistrict,lIDistrict,200);

            ListBlock.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, BlockArray));
            Helpernew.getListViewSize(ListBlock);
            Helpernew.setListViewWidth(ListBlock,lIBlock,250);



        } else {

            //LI_PENDINGSITES.setVisibility(View.GONE);


        }

    }

    public void HitAPI()
    {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);
            if(Type.equalsIgnoreCase("Dealer")){
                UtilMethods.INSTANCE.DealerReport(this, loader,scrollView,ImgNoData);
            }
            else{
                UtilMethods.INSTANCE.AgentReport(this, loader);
            }



        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }


    }
}