package com.Shakti.Shakti.ReportAll.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

public class RedemptionHistoryActivity extends AppCompatActivity {
    LinearLayout LI_RewardProduct,LI_OrderNo,LI_Date,LI_Status,LI_Quantity,LI_Points,LI_Remark;
    ListView li_Status,li_Date,li_OrderNo,li_RewardProduct,li_Quantity,li_RedeemedPoints,li_Remark;
    TextView TV_RewardProduct,TV_RedeemedPoints,TV_Remark,TV_Quantity;
    Loader loader;

    RegisterResponse transactionspack = new RegisterResponse();
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();

    ArrayList<String> rewardStatus = new ArrayList<String>();
    ArrayList<String> createdDate = new ArrayList<String>();
    ArrayList<String> rewardName = new ArrayList<String>();
    ArrayList<String> quantity = new ArrayList<String>();
    ArrayList<String> points = new ArrayList<String>();
    ArrayList<String> remark = new ArrayList<String>();
    ArrayList<String> orderNo = new ArrayList<String>();


    ArrayList<String> getProductName = new ArrayList<String>();

    Rect Bo_RedeemedPoints = new Rect();

    ImageView image;
    ScrollView horizantal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemption_history);

        horizantal=findViewById(R.id.horizantal);
        image=findViewById(R.id.image);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.RedemptionHistory);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

       // ListView li_Status,li_Date,li_OrderNo,li_RewardProduct,li_Quantity,li_RedeemedPoints,li_Remark ;


        li_Status=findViewById(R.id.li_Status);
        li_Date=findViewById(R.id.li_Date);
        li_OrderNo=findViewById(R.id.li_OrderNo);
        li_RewardProduct=findViewById(R.id.li_RewardProduct);
        li_Quantity=findViewById(R.id.li_Quantity);
        li_RedeemedPoints=findViewById(R.id.li_RedeemedPoints);
        li_Remark=findViewById(R.id.li_Remark);

        LI_RewardProduct=findViewById(R.id.LI_RewardProduct);
        LI_OrderNo=findViewById(R.id.LI_OrderNo);
        LI_Date=findViewById(R.id.LI_Date);
        LI_Status=findViewById(R.id.LI_Status);
        LI_Quantity=findViewById(R.id.LI_Quantity);
        LI_Points=findViewById(R.id.LI_Points);
        LI_Remark=findViewById(R.id.LI_Remark);

        TV_RewardProduct=findViewById(R.id.TV_RewardProduct);
        TV_RedeemedPoints=findViewById(R.id.TV_RedeemedPoints);
        TV_Remark=findViewById(R.id.TV_Remark);
        TV_Quantity=findViewById(R.id.TV_Quantity);
        HitApi();

    }

    private void HitApi() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);


            UtilMethods.INSTANCE.RedemptionReport(this, loader,horizantal,image);

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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("detaredemption")) {
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



                    rewardStatus.add(transactionsObjectspack.get(i).getRewardStatus());
                    createdDate.add(transactionsObjectspack.get(i).getCreatedDate());
                    rewardName.add(transactionsObjectspack.get(i).getRewardName());
                    quantity.add(transactionsObjectspack.get(i).getQuantity());
                    points.add(transactionsObjectspack.get(i).getPoints());

                    if(transactionsObjectspack.get(i).getRemark()!=null){

                        remark.add(transactionsObjectspack.get(i).getRemark());

                    }else {
                        remark.add(" ");
                    }

                    orderNo.add(transactionsObjectspack.get(i).getOrderNo());

                }
            }


            li_Quantity.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, quantity));
            Helpernew.getListViewSize(li_Quantity);
            TV_Quantity.measure(0,0);
            Helpernew.setListViewWidth(li_Quantity,LI_Quantity,TV_Quantity.getMeasuredWidth());

            li_RedeemedPoints.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, points));
            Helpernew.getListViewSize(li_RedeemedPoints);
            TV_RedeemedPoints.measure(0,0);
            Helpernew.setListViewWidth(li_RedeemedPoints,LI_Points,TV_RedeemedPoints.getMeasuredWidth());

            MyAdapter adapter;

            adapter = new MyAdapter(this, rewardStatus,"5");
            li_Status.setAdapter(adapter);



         /*   li_Status.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, rewardStatus));
            Helpernew.getListViewSize(li_Status);
*/

            li_Remark.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, remark));
            Helpernew.getListViewSize(li_Remark);
            TV_Remark.measure(0,0);
            Helpernew.setListViewWidth(li_Remark,LI_Remark,TV_Remark.getMeasuredWidth());

            li_Date.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, createdDate));
            Helpernew.getListViewSize(li_Date);
            Helpernew.setListViewWidth(li_Date,LI_Date,200);

            li_OrderNo.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, orderNo));
            Helpernew.getListViewSize(li_OrderNo);
            Helpernew.setListViewWidth(li_OrderNo,LI_OrderNo,200);

            li_RewardProduct.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_listview, rewardName));
            Helpernew.getListViewSize(li_RewardProduct);
            TV_RewardProduct.measure(0,0);
            Helpernew.setListViewWidth(li_RewardProduct,LI_RewardProduct,TV_RewardProduct.getMeasuredWidth());



        } else {

            image.setVisibility(View.VISIBLE);
            horizantal.setVisibility(View.GONE);


        }


    }





}
