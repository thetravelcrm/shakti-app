package com.Shakti.Shakti.Dashbord.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.Shakti.Shakti.BuildConfig;
import com.Shakti.Shakti.MultiLang.LanguageManager;
import com.Shakti.Shakti.MultiLang.LocaleHelper;
import com.Shakti.Shakti.Register.dto.ProfileResponse;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Setting.NotificationActivity;
import com.Shakti.Shakti.Setting.WishkaroActivity;
import com.Shakti.Shakti.SubmitPurchase.ui.MyAccountActivity;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.rampo.updatechecker.UpdateChecker;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Utils.GooglePlayStoreAppVersionNameLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener {

    NavigationView navigationView;
      TextView userName,number,email,count;
      ImageView imageView1;
    String Email="",UserName="",Photo="";

    Loader loader;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FrameLayout main_container;
    DrawerLayout drawerLayout;

     String version="";
    String versionName="";
    int versionCode;
        CircleImageView userpic;

    private static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};

    protected static final String TAG = "LocationOnOff";
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;



     private static final int REQUEST_PERMISSIONS = 1;
     private static final int READ_EXTRENAL_MEDIA_PERMISSIONS_REQUEST = 1;
     ImageView notification_bell;
     ImageView wishlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        notification_bell=findViewById(R.id.notification_bell);
        notification_bell.setOnClickListener(this);

        wishlist=findViewById(R.id.wishlist);
        wishlist.setOnClickListener(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant

                return;
            }
            else
            {
                getPermissionToReadUserLocation();
            }
        }
        else
        {
            getPermissionToReadUserLocation();
        }


        loader = new Loader(this,android.R.style.Theme_Translucent_NoTitleBar);

        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        main_container = (FrameLayout) findViewById(R.id.main_container);
        View header=navigationView.getHeaderView(0);

        userName=header.findViewById(R.id.userName);
        imageView1=header.findViewById(R.id.imageView1);
        number=header.findViewById(R.id.number);
        email=header.findViewById(R.id.email);


        SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);

        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        /// Edited by farhan
        String profileResponse=myPreferences.getString(ApplicationConstant.INSTANCE.setProfile,null);
        if(profileResponse!=null) {
            final ProfileResponse.ProfileList PR = new Gson().fromJson(profileResponse, ProfileResponse.class).getList().get(0);
            UserName=PR.getfName()+" "+PR.getlName();
            Photo=PR.getPhoto();
        }
        else{
            Photo=balanceCheckResponse.getPhoto();
            UserName=balanceCheckResponse.getName();
        }
        ///////////////////
        userName.setText(""+UserName +" - "+balanceCheckResponse.getUserCode()+"");



        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.customer_support);
        requestOptions.error(R.drawable.customer_support);


        Log.e("getPhoto","getPhoto :   "+balanceCheckResponse.getPhoto());
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(Photo)
                .into(imageView1);
//        Glide.with(this)
//                .setDefaultRequestOptions(requestOptions)
//                .load(balanceCheckResponse.getPhoto())
//                .into(imageView1);



        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open, R.string.drawer_close);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));


        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        version= GooglePlayStoreAppVersionNameLoader.newVersion;


        getVersionInfo();

        PopUpdate();

        loadFragment(new HomeFragment());


    }

    public void getPermissionToReadUserLocation() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.d("Permission for contacts", "Displaying contacts permission rationale to provide additional context.");

            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, REQUEST_PERMISSIONS);
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS);
        }
        // END_INCLUDE(contacts_permission_request)
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                 //   HitApi();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        //fragmentManagerHelper.setFragmentManager(fm);
        if (id == R.id.Requesttechexpress){

            Intent i=new Intent(this,RequesttechexpressActivityBoth.class);
            i.putExtra("type","Request Tech Express");
             startActivity(i);



        }else  if (id == R.id.myaccount){


            startActivity(new Intent(this, MyAccountActivity.class));


        }else  if (id == R.id.Feedback){


            Intent i=new Intent(this,RequesttechexpressActivityBoth.class);
            i.putExtra("type","Feedback");
            startActivity(i);


        }else  if (id == R.id.knowmore){


            if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.KnowMore(this,  loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }


        }else  if (id == R.id.wishkaro){

            Intent i=new Intent(this,RequesttechexpressActivityBoth.class);
            i.putExtra("type","Wishes");
            startActivity(i);


       //   startActivity(new Intent(this, WishkaroActivity.class));


        }else  if (id == R.id.faq){

            if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.FAQ(this,  loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }



        }else if (id == R.id.Language){


            OpenDialogLanguage();

           ////


        }else  if (id == R.id.PrivacyPolicy){

            if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.PrivacyPolicy(this,  loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }


        }else  if (id == R.id.ContactUS){

            if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.ContactUS(this,  loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }


        }else  if (id == R.id.share){

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, " ");
                String shareMessage= " ";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }


        }else  if (id == R.id.TermsCondition){

            if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.TermsCondition(this,  loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }


        }else if (id == R.id.Events){

            if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.EventsList(this,  loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }


        }else if (id == R.id.video){

            if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.VideosList(this,  loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }


        }else if (id == R.id.logout){

            final SweetAlertDialog alertDialog = new SweetAlertDialog(this);
            alertDialog.setTitle(R.string.Doyouwanttologout);
            alertDialog.setContentText("");

            alertDialog.setCancelButton(this.getString(R.string.Cancel), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.setConfirmButton(this.getString(R.string.Yes), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    UtilMethods.INSTANCE.logout(MainActivity.this);



                }
            });

            alertDialog.show();

        } else {
            loadFragment(new HomeFragment());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OpenDialogLanguage() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.languagepop, null);

       RadioButton rb_english =  view.findViewById(R.id.rb_english);
       RadioButton rb_hindi =  view.findViewById(R.id.rb_hindi);
       TextView cancelButton =  view.findViewById(R.id.cancelButton);

       String currentlang=LocaleHelper.getLanguage(this);
        if(currentlang.equalsIgnoreCase(LanguageManager.LANGUAGE_KEY_HINDI))
        {
            rb_hindi.setChecked(true);
            rb_english.setChecked(false);
        }
        else {
            rb_hindi.setChecked(false);
            rb_english.setChecked(true);
        }
        final Dialog dialog = new Dialog(this);

        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        rb_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocaleHelper.setLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
                recreate();
//                LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_ENGLISH);
//
//                reLaunchApp();


                dialog.dismiss();
            }
        });


        rb_hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocaleHelper.setLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_HINDI);
                recreate();
//                LanguageManager.setNewLocale(MainActivity.this, LanguageManager.LANGUAGE_KEY_HINDI);
//
//                reLaunchApp();

                dialog.dismiss();
            }
        });


  cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();
    }


    protected void reLaunchApp(){

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    private  void PopUpdate() {

        Log.e("version","    versionName    "+versionName +"  version    "+version );

        if(version!=null && !version.equalsIgnoreCase("")){

            if(!versionName.equalsIgnoreCase(version)){

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
                goToMarket(MainActivity.this);
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

            Log.e("versionnn","   versionName   "+versionName+"   versionCode  "+  versionCode+"   version  "+  version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
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


    @Override
    public void onClick(View view) {
        if(view==notification_bell){


            if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.Notificationlist(this,  loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }


        }
    if(view==wishlist){


        if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.WishList(this,  loader);

        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }


        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, this.getString(R.string.PleaseclickBACKagaintoexit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}


