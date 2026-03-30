package com.Shakti.Shakti.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Register.ui.RewardSteatelistAdapter;
import com.Shakti.Shakti.Register.ui.SteatelistAdapter;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class RewardRedeemActivity extends AppCompatActivity implements View.OnClickListener {

    String redeempayid;
    String Description,Title,image,pointsreedval;

    EditText quantity,city,address,pinCode,Statelist,district;
    LinearLayout li_distic;
    Button FwdokButton;
    Loader loader;
    Dialog dialog;
    String districtIDval;
     RegisterResponse transactionsss = new RegisterResponse();
    ArrayList<Datares> transactionsObjectsss = new ArrayList<>();

    RewardSteatelistAdapter mAdapterss;
    LinearLayoutManager mLayoutManagerss;
    ImageView imagevideo;
    TextView title,discription,pointsreed;

ImageView less,increment;
TextView lunchdeta,youtpoiny;
int some=1;
    public int count = 0;
    public int redempiny ;
    int int_setremaining;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_redeem);



        GetId();
    }


    private void GetId() {

        increment=findViewById(R.id.increment);
        less=findViewById(R.id.less);
        lunchdeta=findViewById(R.id.lunchdeta);
        youtpoiny=findViewById(R.id.youtpoiny);

        increment.setOnClickListener(this);
        less.setOnClickListener(this);


        imagevideo=findViewById(R.id.imagevideo);
        title=findViewById(R.id.title);
        discription=findViewById(R.id.discription);
        pointsreed=findViewById(R.id.pointsreed);

        loader = new Loader(this,android.R.style.Theme_Translucent_NoTitleBar);


        quantity = (EditText) findViewById(R.id.quantity);
       city = (EditText) findViewById(R.id.city);
       address = (EditText) findViewById(R.id.address);
       pinCode = (EditText) findViewById(R.id.pinCode);
       FwdokButton = (Button) findViewById(R.id.okButton);

        Statelist=findViewById(R.id.Statelist);
        district=findViewById(R.id.Districtlist);
        li_distic=findViewById(R.id.li_distic);
        Statelist.setOnClickListener(this);
        FwdokButton.setOnClickListener(this);

        district.setOnClickListener(this);

        SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String setremaining = ""+myPreferences.getString(ApplicationConstant.INSTANCE.remaining, null);

        redempiny= Integer.parseInt(setremaining);

          int_setremaining= Integer.parseInt(setremaining);
        String rem= this.getString(R.string.Remaining);
        youtpoiny.setText(rem+ " : "+setremaining);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.RewardRedeem);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        redeempayid=getIntent().getStringExtra("redeempayid");
        Description=getIntent().getStringExtra("Description");
        Title=getIntent().getStringExtra("Title");
        image=getIntent().getStringExtra("image");
        pointsreedval=getIntent().getStringExtra("pointsreed");


       discription.setText("" + Description);
       title.setText("" + Title);
        pointsreed.setText(this.getString(R.string.Points)+" - " + pointsreedval);

        RequestOptions options = new RequestOptions();
        Glide.with(this)
                .load(image)
                .into(imagevideo);

     }

    @Override
    public void onClick(View view) {


        if(view==increment){

            count = Integer.parseInt((String)lunchdeta.getText());
            count++;



            int kjj= Integer.parseInt(pointsreedval);
            int iii=count*kjj;


if(int_setremaining>iii){

    lunchdeta.setText("" + count);

   // youtpoiny.setText("Remaining : "+iii);



}else {
    Toast.makeText(this, "Check Remaining Point", Toast.LENGTH_SHORT).show();

}
        }

        if(view==less){

            if(!lunchdeta.getText().toString().trim().equalsIgnoreCase("1")){

                count = Integer.parseInt((String)lunchdeta.getText());
                count--;
                lunchdeta.setText("" + count);


                int kjj= Integer.parseInt(pointsreedval);
                int iii=kjj/count;



            }else {
                Toast.makeText(this, "Check Number of Quantity", Toast.LENGTH_SHORT).show();
            }
        }

        if(view==district){

            SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String startelist = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDistrictList, null);
            statepopup(startelist,"District");
        }


        if(view==FwdokButton){
            if (UtilMethods.INSTANCE.isNetworkAvialable( RewardRedeemActivity.this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.RewardRedeem(RewardRedeemActivity.this,redeempayid,""+lunchdeta.getText().toString().trim(),
                        ""+districtIDval,""+city.getText().toString().trim(),""+address.getText().toString().trim(),
                        ""+pinCode.getText().toString().trim(),loader, this);

            } else {

                UtilMethods.INSTANCE.NetworkError(RewardRedeemActivity.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }
//         if(city.getText().toString().trim().equalsIgnoreCase("")){
//
//                Toast.makeText(RewardRedeemActivity.this, "Please Enter City", Toast.LENGTH_SHORT).show();
//
//            }
//         else if(address.getText().toString().trim().equalsIgnoreCase(""))
//         {
//             Toast.makeText(RewardRedeemActivity.this, "Please Enter Address", Toast.LENGTH_SHORT).show();
//         }
//         else if(pinCode.getText().toString().trim().equalsIgnoreCase("")) {
//             Toast.makeText(RewardRedeemActivity.this, "Please Enter PinCode", Toast.LENGTH_SHORT).show();
//         }else {}

        }

        if(view==Statelist){

            if (UtilMethods.INSTANCE.isNetworkAvialable(RewardRedeemActivity.this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.StateList(RewardRedeemActivity.this, loader);

            } else {

                UtilMethods.INSTANCE.NetworkError(RewardRedeemActivity.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }

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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("StateList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"State");
            li_distic.setVisibility(View.VISIBLE);

        }else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("DistrictList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"District");
        }
    }

    private void statepopup(String startelist, final String type) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.startuppop, null);

        RecyclerView recycleview = (RecyclerView) view.findViewById(R.id.recycler_view);
        ImageView cut =  view.findViewById(R.id.cut);
        TextView name =  view.findViewById(R.id.name);
        EditText area_serch =  view.findViewById(R.id.area_serch);
        name.setText(""+ type);


        dialog = new Dialog(RewardRedeemActivity.this);

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

                    if(type.equalsIgnoreCase("State")){

                        getName=op.getName().toLowerCase();

                    }else if(type.equalsIgnoreCase("District")){

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
            mAdapterss = new RewardSteatelistAdapter(transactionsObjectsss, this,""+type);
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

    public void ItemClicksts(String id) {

        dialog.dismiss();

        if (UtilMethods.INSTANCE.isNetworkAvialable(RewardRedeemActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.DistrictList(RewardRedeemActivity.this,id+"", loader,dialog);

        } else {
            UtilMethods.INSTANCE.NetworkError(RewardRedeemActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }




    }

    public void ItemClickid(String districtID, String stateID, String districtName, String stateName) {

        dialog.dismiss();


       Statelist.setText(""+stateName);
        district.setText(""+districtName);

        //  workstartid= ""+stateID;
       districtIDval= ""+districtID;


    }




}
