package com.Shakti.Shakti.Dashbord.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Setting.WishkaroActivity;

public class RequesttechexpressActivityBoth extends AppCompatActivity implements View.OnClickListener {

    TextView Requesttexkexpree,report;
    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesttechexpress_both);

        type=getIntent().getStringExtra("type");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(""+type);
        if(type.equalsIgnoreCase("Feedback"))
        {
            toolbar.setTitle(R.string.Feedback);
        }
        else if(type.equalsIgnoreCase("Request Tech Express"))
        {
            toolbar.setTitle(R.string.RequestTechExpress);
        }
        else if(type.equalsIgnoreCase("Wishes"))
        {
            toolbar.setTitle(R.string.WishKaro);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        Requesttexkexpree=findViewById(R.id.Requesttexkexpree);
        report=findViewById(R.id.report);

        Requesttexkexpree.setOnClickListener(this);
        report.setOnClickListener(this);


        if(type.equalsIgnoreCase("Request Tech Express")){

            loadFragment(new RequesttechexpressActivity());

        }else if(type.equalsIgnoreCase("Wishes")){

            loadFragment(new WishkaroActivity());

        }else {

              loadFragment(new FeedBackFragment());

        }


    }

    @Override
    public void onClick(View view) {



        if(view==Requesttexkexpree){

            Requesttexkexpree.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorPrimary));
            report.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorBackground));

            report.setTextColor(this.getResources().getColorStateList(R.color.black));
            Requesttexkexpree.setTextColor(this.getResources().getColorStateList(R.color.white));


            if(type.equalsIgnoreCase("Request Tech Express")){

                loadFragment(new RequesttechexpressActivity());

            }else if(type.equalsIgnoreCase("Wishes")){

                loadFragment(new WishkaroActivity());

            }else {

                loadFragment(new FeedBackFragment());



            }


        }

     if(view==report){

         Requesttexkexpree.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorBackground));
         report.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorPrimary));
         Requesttexkexpree.setTextColor(this.getResources().getColorStateList(R.color.black));
         report.setTextColor(this.getResources().getColorStateList(R.color.white));


         if(type.equalsIgnoreCase("Request Tech Express")){

             loadFragment(new RequesttexkexpreeActivityhistroyFragment());

         }else  if(type.equalsIgnoreCase("Wishes")){

             loadFragment(new WishkaroreportFragmentReport());

         }else {

             loadFragment(new FeedBackFragmentReport());

         }

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