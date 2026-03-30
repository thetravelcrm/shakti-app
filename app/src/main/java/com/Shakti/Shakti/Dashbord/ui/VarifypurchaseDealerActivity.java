package com.Shakti.Shakti.Dashbord.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.Shakti.Shakti.Dashbord.dto.DashBoardAgentApiRes;
import com.Shakti.Shakti.Dashbord.dto.ProductList;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.SubmitPurchase.ui.DealerpurchaseActivity;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import java.util.ArrayList;

public class VarifypurchaseDealerActivity extends AppCompatActivity implements View.OnClickListener{
    Loader loader;
    String id = "",position="",Status="";
    EditText txt_OrderNo,txt_SiteName,txt_Date,txt_Agent,txt_District,txt_Product,txt_Quantity,txt_Remark;
    Button bt_approve,bt_reject;
    ArrayList<ProductList> transactionsObjectspack = new ArrayList<>();
    DashBoardAgentApiRes transactionspack = new DashBoardAgentApiRes();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varifypurchase_dealer);

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.AgentPurchaseVarification);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        id = getIntent().getStringExtra("id");
        position=getIntent().getStringExtra("position");
        txt_OrderNo=findViewById(R.id.txt_OrderNo);
        txt_SiteName=findViewById(R.id.txt_SiteName);
        txt_Date=findViewById(R.id.txt_Date);
        txt_Agent=findViewById(R.id.txt_Agent);
        txt_District=findViewById(R.id.txt_District);
        txt_Product=findViewById(R.id.txt_Product);
        txt_Quantity=findViewById(R.id.txt_Quantity);
        txt_Remark=findViewById(R.id.txt_Remark);
        bt_approve=findViewById(R.id.bt_approve);
        bt_reject=findViewById(R.id.bt_reject);
        bt_approve.setOnClickListener(this);
        bt_reject.setOnClickListener(this);


        SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String DashBoardDealer = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDashBoardDealer, null);
        Gson gson = new Gson();
        transactionspack = gson.fromJson(DashBoardDealer, DashBoardAgentApiRes.class);
        transactionsObjectspack = transactionspack.getPurchaseList();

        txt_OrderNo.setText(transactionsObjectspack.get(Integer.parseInt(position)).getOrderNo());
        txt_SiteName.setText(transactionsObjectspack.get(Integer.parseInt(position)).getSiteName());
        txt_Date.setText(transactionsObjectspack.get(Integer.parseInt(position)).getCreatedDate());
        txt_Agent.setText(transactionsObjectspack.get(Integer.parseInt(position)).getAgentName());
        txt_District.setText(transactionsObjectspack.get(Integer.parseInt(position)).getDistrictName());
        txt_Product.setText(transactionsObjectspack.get(Integer.parseInt(position)).getProductName());
        txt_Quantity.setText(transactionsObjectspack.get(Integer.parseInt(position)).getQuantity());
    }


    @Override
    public void onClick(View view) {
        if(view==bt_approve)
        {
            Status="active";
            HitApproveAPI();
        }
        if(view==bt_reject)
        {
            if(txt_Remark.getText().toString().trim().equalsIgnoreCase("")) {
                txt_Remark.setError("Please Enter Reason of Rejection");
                txt_Remark.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
            else
            {
                Status="rejected";
                HitApproveAPI();
            }
        }

    }

    public void HitApproveAPI()
    {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.UpdateDealerStatus(this,id,Status,txt_Remark.getText().toString().trim(), loader,VarifypurchaseDealerActivity.this);

        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }
        //UtilMethods.INSTANCE.Successful(this, "Abhi API nhi bani hai sirf design check krni hai",5, VarifypurchaseDealerActivity.this);
    }
}

