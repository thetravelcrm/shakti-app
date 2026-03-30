package com.Shakti.Shakti.ReportAll.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.Shakti.Shakti.ReportAll.Adapter.ProductSubAdapter;
import com.Shakti.Shakti.ReportAll.Adapter.SiteAgentAdapter;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MySitesActivity extends AppCompatActivity {

    Loader loader;
    RegisterResponse transactionspack = new RegisterResponse();
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();

    RecyclerView recycler_view;
    SiteAgentAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    String type="";

    ImageView image;
    ScrollView horizantal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sites);

        type=getIntent().getStringExtra("type");

        horizantal=findViewById(R.id.horizantal);
        image=findViewById(R.id.image);
        recycler_view=findViewById(R.id.recycler_view);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(type.equalsIgnoreCase("agent")){
            toolbar.setTitle(R.string.MySites);
        }
        else
        {toolbar.setTitle("Varified Sites");}
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);



        HitApi();

    }

    private void HitApi() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            if(type.equalsIgnoreCase("agent")){

                //UtilMethods.INSTANCE.SiteReport(this, loader,horizantal,image);


            }else {

                UtilMethods.INSTANCE.SubAdminSitesReport(this, loader);


            }


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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("SiteReport")) {
            String startelist=""+activityFragmentMessage.getFrom();

            dataParse(startelist);

        }

    }

    public void dataParse(String response) {




        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, RegisterResponse.class);
        transactionsObjectspack = transactionspack.getList();

        if (transactionsObjectspack.size() > 0) {
            mAdapter = new SiteAgentAdapter(transactionsObjectspack, this);
            mLayoutManager = new GridLayoutManager(this,1);
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(mAdapter);

            recycler_view.setVisibility(View.VISIBLE);
        } else {
            recycler_view.setVisibility(View.GONE);

            horizantal.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
        }

    }


}
