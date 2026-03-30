package com.Shakti.Shakti.Dashbord.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.ReportAll.Activity.DealerAgentsActivity;
import com.Shakti.Shakti.ReportAll.Activity.DealerPurchaseStatusActivity;
import com.Shakti.Shakti.ReportAll.Activity.DealerReportActivity;
import com.Shakti.Shakti.ReportAll.Activity.DealerSaleStatusActivity;
import com.Shakti.Shakti.ReportAll.Activity.EarnPointsStatementActivity;
import com.Shakti.Shakti.ReportAll.Activity.MyDealerActivity;
import com.Shakti.Shakti.ReportAll.Activity.MySitesActivity;
import com.Shakti.Shakti.ReportAll.Activity.PointStatusActivity;
import com.Shakti.Shakti.ReportAll.Activity.PurchaseStatusActivity;
import com.Shakti.Shakti.ReportAll.Activity.RedemptionHistoryActivity;
import com.Shakti.Shakti.ReportAll.Fragment.PointStatesActivityBoth;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.Shakti.Shakti.ViewAll.ViewDetail_two_Activity;
import com.google.gson.Gson;

public class ReportAllActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout PurchaseStatus,RedemptionHistory,EarnPointsStatement,PointStatus,MySites,MyDealer,dealerlist;
    LinearLayout agentreport,dealerAgents, dealerPurchaseStatus,mySitesdealer,dealerSaleStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_all);

        Getid();


    }

    private void Getid() {

        mySitesdealer=findViewById(R.id.mySitesdealer);
        dealerAgents=findViewById(R.id.dealerAgents);
        dealerPurchaseStatus=findViewById(R.id.dealerPurchaseStatus);
        dealerSaleStatus=findViewById(R.id.dealerSaleStatus);
        agentreport=findViewById(R.id.agentreport);


        MyDealer=findViewById(R.id.MyDealer);
        MySites=findViewById(R.id.MySites);
        PointStatus=findViewById(R.id.PointStatus);
        EarnPointsStatement=findViewById(R.id.EarnPointsStatement);
        RedemptionHistory=findViewById(R.id.RedemptionHistory);
        PurchaseStatus=findViewById(R.id.PurchaseStatus);
        dealerlist=findViewById(R.id.dealerlist);
        dealerPurchaseStatus.setOnClickListener(this);
        dealerSaleStatus.setOnClickListener(this);
        dealerAgents.setOnClickListener(this);
        MyDealer.setOnClickListener(this);
        MySites.setOnClickListener(this);
        PointStatus.setOnClickListener(this);
        EarnPointsStatement.setOnClickListener(this);
        RedemptionHistory.setOnClickListener(this);
        PurchaseStatus.setOnClickListener(this);
        mySitesdealer.setOnClickListener(this);
        dealerlist.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.Reports);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setValue();
    }


    private void setValue() {

        SharedPreferences myPreferences =  getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String UserCode=""+balanceCheckResponse.getUserRole();

        Log.e("UserCode","UserCode :  "+ UserCode     );



        if(UserCode.equalsIgnoreCase("3")){


            ///subadmin//
             agentreport.setVisibility(View.GONE);
             dealerAgents.setVisibility(View.VISIBLE);
            mySitesdealer.setVisibility(View.VISIBLE);
            dealerPurchaseStatus.setVisibility(View.GONE);
            dealerSaleStatus.setVisibility(View.GONE);
            dealerlist.setVisibility(View.VISIBLE);


        }else  if(UserCode.equalsIgnoreCase("4")){


            ////////dEALER
            agentreport.setVisibility(View.GONE);
            mySitesdealer.setVisibility(View.GONE);
            dealerPurchaseStatus.setVisibility(View.VISIBLE);

            dealerAgents.setVisibility(View.GONE);
            dealerlist.setVisibility(View.GONE);

        }else  if(UserCode.equalsIgnoreCase("5")){

//  Agent
            agentreport.setVisibility(View.VISIBLE);
            dealerPurchaseStatus.setVisibility(View.GONE);
            mySitesdealer.setVisibility(View.GONE);
            dealerAgents.setVisibility(View.GONE);
            dealerSaleStatus.setVisibility(View.GONE);
            dealerlist.setVisibility(View.GONE);
            MyDealer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        if(view==mySitesdealer){

//            Intent i=new Intent(new Intent(this, MySitesActivity.class));
//            i.putExtra("type","subadmin");
//            startActivity(i);
            Intent i=new Intent(this, ViewDetail_two_Activity.class).putExtra("type","SubAdmin");;
            startActivity(i);
        }

      if(view==dealerSaleStatus){

            startActivity(new Intent(this, DealerPurchaseStatusActivity.class));
        }
      if(view==dealerPurchaseStatus)
      {
          startActivity(new Intent(this, DealerSaleStatusActivity.class));
      }

     if(view==dealerAgents){

            //startActivity(new Intent(this, DealerAgentsActivity.class));
         startActivity(new Intent(this, DealerReportActivity.class).putExtra("Type","Agent"));
        }
     if(view==dealerlist)
     {
         startActivity(new Intent(this, DealerReportActivity.class).putExtra("Type","Dealer"));
     }


  if(view==PurchaseStatus){

            startActivity(new Intent(this, PurchaseStatusActivity.class));
        }



        if(view==RedemptionHistory){

            startActivity(new Intent(this, RedemptionHistoryActivity.class));


        }

        if(view==EarnPointsStatement){


            startActivity(new Intent(this, EarnPointsStatementActivity.class));

        }

        if(view==PointStatus){

            Intent i=new Intent(new Intent(this, PointStatesActivityBoth.class));
            i.putExtra("type","1");
            startActivity(i);

  }

        if(view==MySites){

//            Intent i=new Intent(new Intent(this, MySitesActivity.class));
//            i.putExtra("type","agent");
//            startActivity(i);
            Intent i=new Intent(this, ViewDetail_two_Activity.class).putExtra("type","Agent");
            startActivity(i);
         //  // startActivity(new Intent(this, MySitesActivity.class));


        }

        if(view==MyDealer){

            //startActivity(new Intent(this, MyDealerActivity.class));
            startActivity(new Intent(this, DealerReportActivity.class).putExtra("Type","Dealer"));

        }


    }
}
