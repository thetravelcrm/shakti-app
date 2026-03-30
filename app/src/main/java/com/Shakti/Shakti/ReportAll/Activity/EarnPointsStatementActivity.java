package com.Shakti.Shakti.ReportAll.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

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

public class EarnPointsStatementActivity extends AppCompatActivity {

    ListView li_Product,li_January,li_February,li_March,li_April,Mayli,li_Jun,li_July,li_August,li_September,li_October,li_November,li_December;
    LinearLayout LI_Product,LI_January,LI_February,LI_March,LI_April,LI_May,LI_Jun,LI_July,LI_August,LI_September,LI_October,LI_November,LI_December;
    TextView TV_Product,TV_January,TV_February,TV_March,TV_April,TV_May,TV_Jun,TV_July,TV_August,TV_September,TV_October,TV_November,TV_December;
    Loader loader;

    ImageView image;
    ScrollView horizantal;


    RegisterResponse transactionspack = new RegisterResponse();
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();



     ArrayList<String> getProductName = new ArrayList<String>();
    ArrayList<String> getJan = new ArrayList<String>();
    ArrayList<String> getFeb = new ArrayList<String>();
    ArrayList<String> getMar = new ArrayList<String>();
    ArrayList<String> getApr = new ArrayList<String>();
    ArrayList<String> getMay = new ArrayList<String>();
    ArrayList<String> getJun = new ArrayList<String>();
    ArrayList<String> getJuly = new ArrayList<String>();
    ArrayList<String> getAug = new ArrayList<String>();
    ArrayList<String> getSep = new ArrayList<String>();
    ArrayList<String> getOct = new ArrayList<String>();
    ArrayList<String> getNov = new ArrayList<String>();
    ArrayList<String> getDec = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_points_statement);

        image=findViewById(R.id.image);
        horizantal=findViewById(R.id.horizantal);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.EarnPointsStatement);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);



        li_Product=findViewById(R.id.li_Product);
        li_January=findViewById(R.id.li_January);
        li_February=findViewById(R.id.li_February);
        li_March=findViewById(R.id.li_March);
        li_April=findViewById(R.id.li_April);
        Mayli=findViewById(R.id.Mayli);
        li_Jun=findViewById(R.id.li_Jun);
        li_July=findViewById(R.id.li_July);
        li_August=findViewById(R.id.li_August);
        li_September=findViewById(R.id.li_September);
        li_October=findViewById(R.id.li_October);
        li_November=findViewById(R.id.li_November);
        li_December=findViewById(R.id.li_December);

        LI_Product=findViewById(R.id.LI_Product);
        LI_January=findViewById(R.id.LI_January);
        LI_February=findViewById(R.id.LI_February);
        LI_March=findViewById(R.id.LI_March);
        LI_April=findViewById(R.id.LI_April);
        LI_May=findViewById(R.id.LI_May);
        LI_Jun=findViewById(R.id.LI_Jun);
        LI_July=findViewById(R.id.LI_July);
        LI_August=findViewById(R.id.LI_August);
        LI_September=findViewById(R.id.LI_September);
        LI_October=findViewById(R.id.LI_October);
        LI_November=findViewById(R.id.LI_November);
        LI_December=findViewById(R.id.LI_December);

        TV_Product=findViewById(R.id.TV_Product);
        TV_January=findViewById(R.id.TV_January);
        TV_February=findViewById(R.id.TV_February);
        TV_March=findViewById(R.id.TV_March);
        TV_April=findViewById(R.id.TV_April);
        TV_May=findViewById(R.id.TV_May);
        TV_Jun=findViewById(R.id.TV_Jun);
        TV_July=findViewById(R.id.TV_July);
        TV_August=findViewById(R.id.TV_August);
        TV_September=findViewById(R.id.TV_September);
        TV_October=findViewById(R.id.TV_October);
        TV_November=findViewById(R.id.TV_November);
        TV_December=findViewById(R.id.TV_December);
        HitApi();
    }

    private void HitApi() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.EarnPointReport(this, loader,horizantal,image);

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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("EarnPointReport")) {
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


                    getProductName.add(transactionsObjectspack.get(i).getProductName());
                    getJan.add(transactionsObjectspack.get(i).getJan());
                    getFeb.add(transactionsObjectspack.get(i).getFeb());
                    getMar.add(transactionsObjectspack.get(i).getMar());
                    getApr.add(transactionsObjectspack.get(i).getApr());
                    getMay.add(transactionsObjectspack.get(i).getMay());
                    getJun.add(transactionsObjectspack.get(i).getJun());
                    getJuly.add(transactionsObjectspack.get(i).getJuly());
                    getAug.add(transactionsObjectspack.get(i).getAug());
                    getSep.add(transactionsObjectspack.get(i).getSep());
                    getOct.add(transactionsObjectspack.get(i).getOct());
                    getNov.add(transactionsObjectspack.get(i).getNov());
                    getDec.add(transactionsObjectspack.get(i).getDec());

                }
            }

            li_Product.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getProductName));
            TV_Product.measure(0,0);
            Helpernew.getListViewSize(li_Product);
            Helpernew.setListViewWidth(li_Product,LI_Product,TV_Product.getMeasuredWidth());

            li_January.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getJan));
            TV_January.measure(0,0);
            Helpernew.getListViewSize(li_January);
            Helpernew.setListViewWidth(li_January,LI_January,TV_January.getMeasuredWidth());

            li_February.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getFeb));
            TV_February.measure(0,0);
            Helpernew.getListViewSize(li_February);
            Helpernew.setListViewWidth(li_February,LI_February,TV_February.getMeasuredWidth());

            li_March.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getMar));
            TV_March.measure(0,0);
            Helpernew.getListViewSize(li_March);
            Helpernew.setListViewWidth(li_March,LI_March,TV_March.getMeasuredWidth());

            li_April.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getApr));
            TV_April.measure(0,0);
            Helpernew.getListViewSize(li_April);
            Helpernew.setListViewWidth(li_April,LI_April,TV_April.getMeasuredWidth());
            Mayli.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getMay));
            TV_May.measure(0,0);
            Helpernew.getListViewSize(Mayli);
            Helpernew.setListViewWidth(Mayli,LI_May,TV_May.getMeasuredWidth());
            li_Jun.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getJun));
            TV_Jun.measure(0,0);
            Helpernew.getListViewSize(li_Jun);
            Helpernew.setListViewWidth(li_Jun,LI_Jun,TV_Jun.getMeasuredWidth());
            li_July.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getJuly));
            TV_July.measure(0,0);
            Helpernew.getListViewSize(li_July);
            Helpernew.setListViewWidth(li_July,LI_July,TV_July.getMeasuredWidth());
            li_August.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getAug));
            TV_August.measure(0,0);
            Helpernew.getListViewSize(li_August);
            Helpernew.setListViewWidth(li_August,LI_August,TV_August.getMeasuredWidth());
            li_September.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getSep));
            TV_September.measure(0,0);
            Helpernew.getListViewSize(li_September);
            Helpernew.setListViewWidth(li_September,LI_September,TV_September.getMeasuredWidth());
            li_October.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getOct));
            TV_October.measure(0,0);
            Helpernew.getListViewSize(li_October);
            Helpernew.setListViewWidth(li_October,LI_October,TV_October.getMeasuredWidth());
            li_November.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getNov));
            TV_November.measure(0,0);
            Helpernew.getListViewSize(li_November);
            Helpernew.setListViewWidth(li_November,LI_November,TV_November.getMeasuredWidth());

            li_December.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, getDec));
            TV_December.measure(0,0);
            Helpernew.getListViewSize(li_December);
            Helpernew.setListViewWidth(li_December,LI_December,TV_December.getMeasuredWidth());




        } else {

            horizantal.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);


        }


    }





}
