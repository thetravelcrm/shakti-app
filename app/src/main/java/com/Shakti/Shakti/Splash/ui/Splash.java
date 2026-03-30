package com.Shakti.Shakti.Splash.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.Shakti.Shakti.BuildConfig;
 import com.Shakti.Shakti.Dashbord.ui.MainActivity;
import com.Shakti.Shakti.Login.ui.LoginActivity;
import com.Shakti.Shakti.MultiLang.LocaleHelper;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.GooglePlayStoreAppVersionNameLoader;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.rampo.updatechecker.UpdateChecker;


public class Splash extends AppCompatActivity implements Animation.AnimationListener {

    String Email="";
    TextView version;
    Loader loader;
  //  Animation slideRight;
    private static final int REQUEST_PERMISSIONS = 1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION };
    private static final int READ_EXTRENAL_MEDIA_PERMISSIONS_REQUEST = 1;
    String appversion="";
    String versionName="";
    int versionCode;

    @Override
    protected void onPause() {
         super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();


        ImageView image=findViewById(R.id.img);

       // image.setVisibility(View.VISIBLE);
        //image.startAnimation(slideRight);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

       // slideRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
      //  slideRight.setAnimationListener(this);

        new GooglePlayStoreAppVersionNameLoader().execute();

        version=findViewById(R.id.version);
        version.setText("V : "+ BuildConfig.VERSION_NAME);
        appversion= GooglePlayStoreAppVersionNameLoader.newVersion;
        getVersionInfo();

        PopUpdate();
        ReadPhoneStatePermission();

        LocaleHelper.setLocale(Splash.this, LocaleHelper.getLanguage(this));

    }

    private void Selectpagr() {





        SharedPreferences myPreferences =  getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = ""+myPreferences.getString(ApplicationConstant.INSTANCE.one, null);
        Email = ""+balanceResponse;

       Log.e("Email","  Email"+   Email +"    "+  Email.length() );

        if ( Email.equalsIgnoreCase("1")){

            DashBord();

        }else{

            loginpage();

        }

    }

    public void DashBord() {

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    DashBordLogin();
                }
            }
        };
        timerThread.start();
    }

    public void DashBordLogin() {
        Intent intent = new Intent(Splash.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginpage() {

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    startLogin();
                }
            }
        };
        timerThread.start();
    }

    public void startLogin() {
        Intent intent = new Intent(Splash.this, LoginActivity.class);
      //  Intent intent = new Intent(Splash.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    public void ReadPhoneStatePermission() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(Splash.this, PERMISSIONS, REQUEST_PERMISSIONS);
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS);
        }
        // END_INCLUDE(contacts_permission_request)
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == 5 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

              // Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();



                Selectpagr();


            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == READ_EXTRENAL_MEDIA_PERMISSIONS_REQUEST) {
            if (grantResults.length == 4 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Splash.this, "EXTRENAL_MEDIA Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Splash.this, "EXTRENAL_MEDIA Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private  void PopUpdate() {

        Log.e("version","    versionName    "+versionName +"  version    "+version );

        if(appversion!=null && !appversion.equalsIgnoreCase("")){

            if(!versionName.equalsIgnoreCase(appversion)){

                OpenUpdateDialog();

            }

        }
    }
    public void OpenUpdateDialog() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.update_available_pop, null);

        TextView tvLater = (TextView) view.findViewById(R.id.tv_later);
        TextView tvOk=(TextView)view.findViewById(R.id.tv_ok);

        final Dialog dialog = new Dialog(this);

        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMarket(Splash.this);
                dialog.dismiss();
                //UtilMethods.INSTANCE.goAnotherActivity((Activity) context,Splash.class);
            }
        });

        dialog.show();
    }
    private static void goToMarket(Context mContext) {
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UpdateChecker.ROOT_PLAY_STORE_DEVICE + mContext.getPackageName())));
    }
    private void getVersionInfo() {

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;

            Log.e("versionnn","   versionName   "+versionName+"   versionCode  "+  versionCode+"   version  "+  appversion);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
