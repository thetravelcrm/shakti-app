package com.Shakti.Shakti.SubmitPurchase.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.Shakti.Shakti.ChangePasswordActivity;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.ReportAll.Activity.EditprofileActivity;
import com.Shakti.Shakti.ReportAll.Activity.KycUpdateActivity;
import com.Shakti.Shakti.ReportAll.Activity.ViewprofileActivity;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout changepassword,editprofile,kycupdate,ChangeMobileNumber,viewprofile;
    Loader loader;
    String MobileNo="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

Getid();
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.UserDetails(this,loader);
            //UtilMethods.INSTANCE.DealerList(this, loader);


        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }

    }

    private void Getid() {


        SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
           MobileNo = balanceCheckResponse.getMobileNo();


        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.MyAccount);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewprofile=findViewById(R.id.viewprofile);
        viewprofile.setOnClickListener(this);

        changepassword=findViewById(R.id.changepassword);
        changepassword.setOnClickListener(this);

        editprofile=findViewById(R.id.editprofile);
        editprofile.setOnClickListener(this);


        kycupdate=findViewById(R.id.kycupdate);
        kycupdate.setOnClickListener(this);

        ChangeMobileNumber=findViewById(R.id.ChangeMobileNumber);
        ChangeMobileNumber.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {


        if(view==viewprofile){
            startActivity(new Intent(this, ViewprofileActivity.class));
        }

        if(view==editprofile){


            startActivity(new Intent(this, EditprofileActivity.class));




        }

       if(view==kycupdate){



startActivity(new Intent(this, KycUpdateActivity.class));


        }




if(view==ChangeMobileNumber){


    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View vv = inflater.inflate(R.layout.otp_layout, null);

     final LinearLayout otpTextLayoutnumber = (LinearLayout) vv.findViewById(R.id.otpTextLayoutnumber);
     final AppCompatTextView title = (AppCompatTextView) vv.findViewById(R.id.title);
     final AppCompatTextView message = (AppCompatTextView) vv.findViewById(R.id.message);
     final AppCompatTextView messagebelow=(AppCompatTextView)vv.findViewById(R.id.mmmmmm);
     final EditText newnumberuser = (EditText) vv.findViewById(R.id.newnumberuser);
     final EditText numberuser = (EditText) vv.findViewById(R.id.numberuser);
    final AppCompatButton okButton = (AppCompatButton) vv.findViewById(R.id.okButton);
    final AppCompatButton cancelButton = (AppCompatButton) vv.findViewById(R.id.cancelButton);
    final Dialog dialog = new Dialog(this);

    dialog.setCancelable(false);
    dialog.setContentView(vv);

    otpTextLayoutnumber.setVisibility(View.VISIBLE);
    messagebelow.setText(R.string.SendOTPtochangepassword);
    numberuser.setText(""+MobileNo);
    message.setText(R.string.YourRegisterNumberis);
    message.setVisibility(View.GONE);
    messagebelow.setVisibility(View.GONE);
    numberuser.setVisibility(View.GONE);
    title.setText(R.string.ChangeMobileNumber);

    cancelButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    });

    okButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (UtilMethods.INSTANCE.isNetworkAvialable(MyAccountActivity.this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.GenerateOTPChangePassword(MyAccountActivity.this, ""+newnumberuser.getText().toString().trim(),loader,dialog,"1");

            } else {
                UtilMethods.INSTANCE.NetworkError(MyAccountActivity.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }

        }
    });
    dialog.show();


}


if(view==changepassword){


    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View vv = inflater.inflate(R.layout.otp_layout, null);

     final AppCompatTextView message = (AppCompatTextView) vv.findViewById(R.id.message);
     final EditText numberuser = (EditText) vv.findViewById(R.id.numberuser);
    final AppCompatButton okButton = (AppCompatButton) vv.findViewById(R.id.okButton);
    final AppCompatButton cancelButton = (AppCompatButton) vv.findViewById(R.id.cancelButton);
    final Dialog dialog = new Dialog(this);

    dialog.setCancelable(false);
    dialog.setContentView(vv);

    numberuser.setText(""+MobileNo);
    message.setText(R.string.YourRegisterNumberis);

    cancelButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    });

    okButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (UtilMethods.INSTANCE.isNetworkAvialable(MyAccountActivity.this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.GenerateOTPChangePassword(MyAccountActivity.this, ""+MobileNo,loader,dialog,"2");

            } else {
                UtilMethods.INSTANCE.NetworkError(MyAccountActivity.this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }

        }
    });
    dialog.show();


}






    }
}
