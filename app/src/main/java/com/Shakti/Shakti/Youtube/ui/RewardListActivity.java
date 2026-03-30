package com.Shakti.Shakti.Youtube.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.Adapter.EventlistAdapter;
import com.Shakti.Shakti.Adapter.FaqAdapter;
import com.Shakti.Shakti.Adapter.RewaedAdapter;
import com.Shakti.Shakti.Adapter.WishlistlistAdapter;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Register.ui.SteatelistAdapter;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class RewardListActivity extends AppCompatActivity {

    RecyclerView recycler_view;
    Loader loader;

    VideosListAdapter mAdapter;
    EventlistAdapter eventmAdapter;
    RewaedAdapter RewardlistAdapter;
    FaqAdapter faqAdapter;
    WishlistlistAdapter wishlistmAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<Datares> transactionsObjects = new ArrayList<>();
    RegisterResponse transactions = new RegisterResponse();
    String response="";
    String type="";
    Dialog dialog;

          EditText Statelist,district;
          String districtIDval;


    LinearLayout li_distic;
     RegisterResponse transactionsss = new RegisterResponse();
    ArrayList<Datares> transactionsObjectsss = new ArrayList<>();

    SteatelistAdapter mAdapterss;
    LinearLayoutManager mLayoutManagerss;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_list);

        Getid();

    }

    private void Getid() {

        li_distic=findViewById(R.id.li_distic);



        response=getIntent().getStringExtra("response");
        type=getIntent().getStringExtra("type");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(""+type);
        if(type.equalsIgnoreCase("Reward"))
        {
            toolbar.setTitle(R.string.Reward);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recycler_view = (RecyclerView)  findViewById(R.id.recycler_view);
        loader = new Loader(this,android.R.style.Theme_Translucent_NoTitleBar);

          dataParseRewardlist(""+response);

    }




    public void dataParseRewardlist(String response) {

        Gson gson = new Gson();
        transactions = gson.fromJson(response, RegisterResponse.class);
        transactionsObjects = transactions.getData();

        if (transactionsObjects.size() > 0) {
            RewardlistAdapter = new RewaedAdapter(transactionsObjects, this);
            mLayoutManager = new GridLayoutManager(this,1);
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(RewardlistAdapter);

            recycler_view.setVisibility(View.VISIBLE);
        } else {
            recycler_view.setVisibility(View.GONE);
        }

    }




  /*  @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }*/

  /*  @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("StateList")) {
            String startelist=""+activityFragmentMessage.getFrom();

            statepopup(startelist,"State List");
            li_distic.setVisibility(View.VISIBLE);

        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("DistrictList")) {

            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"District List");

        }

    }*/

    private void statepopup(String startelist, final String type) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.startuppop, null);


        RecyclerView recycleview = (RecyclerView) view.findViewById(R.id.recycler_view);
        ImageView cut =  view.findViewById(R.id.cut);
        TextView name =  view.findViewById(R.id.name);
        EditText area_serch =  view.findViewById(R.id.area_serch);
        name.setText(""+ type);


        dialog = new Dialog(RewardListActivity.this);

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
                for(Datares op:transactionsObjectsss)
                {

                    String getName="";

                    if(type.equalsIgnoreCase("State List")){

                        getName=op.getName().toLowerCase();

                    }else if(type.equalsIgnoreCase("District List")){

                        getName=op.getDistrictName().toLowerCase();

                    }

                    if(getName.contains(newText)){

                        newlist.add(op);
                    }
                }
                mAdapterss.filter(newlist);

            }
        });

        Gson gson = new Gson();
        transactionsss = gson.fromJson(startelist, RegisterResponse.class);
        transactionsObjectsss = transactionsss.getData();

        if (transactionsObjectsss.size() > 0) {
            mAdapterss = new SteatelistAdapter(transactionsObjectsss, this,""+type,"44");
            mLayoutManagerss = new LinearLayoutManager(this);
            recycleview.setLayoutManager(mLayoutManagerss);
            recycleview.setItemAnimator(new DefaultItemAnimator());
            recycleview.setAdapter(mAdapterss);

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

}

