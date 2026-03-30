package com.Shakti.Shakti.ReportAll.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.Shakti.Shakti.Dashbord.ui.MyAdapter;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MyDealerActivity extends AppCompatActivity {

    ListView li_DealerName,li_MobileNo,li_District,li_Dealerpic;
    Loader loader;
    MyAdapterPic adapter;


    RegisterResponse transactionspack = new RegisterResponse();
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();



    ArrayList<String> getMobile = new ArrayList<String>();
    ArrayList<String> getDealerName = new ArrayList<String>();
    ArrayList<String> getDistrict = new ArrayList<String>();
    ArrayList<String> getDealerpic = new ArrayList<String>();

    ImageView image;
    ScrollView horizantal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dealer);

        horizantal=findViewById(R.id.horizantal);
        image=findViewById(R.id.image);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Dealer");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);


        li_DealerName=findViewById(R.id.li_DealerName);
        li_MobileNo=findViewById(R.id.li_MobileNo);
        li_District=findViewById(R.id.li_District);
        li_Dealerpic=findViewById(R.id.li_Dealerpic);



        HitApi();


    }


    private void HitApi() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

           // UtilMethods.INSTANCE.DealerReport(this, loader,horizantal,image);

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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("DealerReport")) {
            String startelist=""+activityFragmentMessage.getFrom();

            dataParseRequestTechReport(startelist);

        }

    }

    public void dataParseRequestTechReport(String response) {

        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, RegisterResponse.class);
        transactionsObjectspack = transactionspack.getList();

        if (transactionsObjectspack.size() > 0) {


            if (transactionsObjectspack != null && transactionsObjectspack.size() > 0) {

                for (int i = 0; i < transactionsObjectspack.size(); i++) {


                    getDistrict.add(transactionsObjectspack.get(i).getDistrict());
                    getMobile.add(transactionsObjectspack.get(i).getMobile());
                    getDealerName.add(transactionsObjectspack.get(i).getDealerName());
                    getDealerpic.add(transactionsObjectspack.get(i).getPhoto());


                }
            }




            li_DealerName.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getDealerName));
            Helpernew.getListViewSize(li_DealerName);

            li_MobileNo.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getMobile));
            Helpernew.getListViewSize(li_MobileNo);

            li_District.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getDistrict));
            Helpernew.getListViewSize(li_District);


           /* li_Dealerpic.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getDealerpic));
            Helpernew.getListViewSize(li_Dealerpic);*/

            adapter = new MyAdapterPic(this, getDealerpic,"3");
            li_Dealerpic.setAdapter(adapter);




        } else {

            horizantal.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);

        }


    }




}
