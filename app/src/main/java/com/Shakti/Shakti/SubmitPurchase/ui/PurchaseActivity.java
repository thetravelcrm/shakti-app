package com.Shakti.Shakti.SubmitPurchase.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.Shakti.Shakti.R;

public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout newside,exitside,PurchaseStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        Getid();
    }

    private void Getid() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.SubmitPurchase);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        newside=findViewById(R.id.newside);
        newside.setOnClickListener(this);

        exitside=findViewById(R.id.exitside);
        exitside.setOnClickListener(this);


        PurchaseStatus=findViewById(R.id.PurchaseStatus);
        PurchaseStatus.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

if(view==newside){

    Intent i=new Intent(this, SubmitPurchaseActivity.class);
    startActivity(i);

}



if(view==exitside){

    Intent i=new Intent(this, ExistingActivity.class);
    startActivity(i);

}




if(view==PurchaseStatus){

   // Intent i=new Intent(this, ExistingActivity.class);
  //  startActivity(i);

}







    }
}
