package com.Shakti.Shakti.ReportAll.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.Shakti.Shakti.Dashbord.ui.FeedBackFragment;
import com.Shakti.Shakti.Dashbord.ui.FeedBackFragmentReport;
import com.Shakti.Shakti.Dashbord.ui.RequesttechexpressActivity;
import com.Shakti.Shakti.Dashbord.ui.RequesttexkexpreeActivityhistroyFragment;
import com.Shakti.Shakti.Dashbord.ui.WishkaroreportFragmentReport;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.ReportAll.Activity.PointStatusActivity;
import com.Shakti.Shakti.Setting.WishkaroActivity;

public class PointStatesActivityBoth extends AppCompatActivity implements View.OnClickListener {

    TextView earn,redeem;
    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointstates_both);

        type=getIntent().getStringExtra("type");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.PointStatus);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        earn=findViewById(R.id.earn);
        redeem=findViewById(R.id.redeem);

        earn.setOnClickListener(this);
        redeem.setOnClickListener(this);

        if(type.equalsIgnoreCase("1")){

            loadFragment(new PointStatusActivity());


        }else {

            earn.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorBackground));
            redeem.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorPrimary));
            earn.setTextColor(this.getResources().getColorStateList(R.color.black));
            redeem.setTextColor(this.getResources().getColorStateList(R.color.white));

            loadFragment(new ReedemStatusActivity());

        }

            // loadFragment(new PointStatusActivity());


    }

    @Override
    public void onClick(View view) {



        if(view==earn){

            earn.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorPrimary));
            redeem.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorBackground));

            redeem.setTextColor(this.getResources().getColorStateList(R.color.black));
            earn.setTextColor(this.getResources().getColorStateList(R.color.white));


                loadFragment(new PointStatusActivity());



        }

     if(view==redeem){

         earn.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorBackground));
         redeem.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorPrimary));
         earn.setTextColor(this.getResources().getColorStateList(R.color.black));
         redeem.setTextColor(this.getResources().getColorStateList(R.color.white));




             loadFragment(new ReedemStatusActivity());



     }

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}