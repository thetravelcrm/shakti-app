package com.Shakti.Shakti.SubmitPurchase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Setting.WishkaroActivity;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;

public class ReemallActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout wishkaro,RewardList,PurchaseStatus;

    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reedem);

Getid();



    }

    private void Getid() {

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);


        wishkaro=findViewById(R.id.wishkaro);
        wishkaro.setOnClickListener(this);

        RewardList=findViewById(R.id.RewardList);
        RewardList.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Redemption");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onClick(View view) {

 if(view==wishkaro){

startActivity(new Intent(this, WishkaroActivity.class));

 }


if(view==RewardList){

    if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

         UtilMethods.INSTANCE.RewardList(this,  loader);

    } else {
        UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                getResources().getString(R.string.network_error_message));
    }
 }








    }
}
