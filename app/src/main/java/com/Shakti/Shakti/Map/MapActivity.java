package com.Shakti.Shakti.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Shakti.Shakti.Dashbord.ui.GPSTracker;
import com.Shakti.Shakti.Dashbord.ui.GetLocation;
import com.Shakti.Shakti.Dashbord.ui.MainActivity;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.ProfileResponse;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Splash.ui.Splash;
import com.Shakti.Shakti.SubmitPurchase.ui.SubmitPurchaseActivity;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1000;
    private static final String TAG = "TEST";
    GoogleMap mGoogleMap;
    MapResponse mapResponse=new MapResponse();
    ArrayList<MapResponse.MapList> mapList=new ArrayList<>();
    Loader loader;
    GetLocation Locationn;
    String DistrictID="0";
    Dialog dialog;
    SupportMapFragment mapFragment;
    ImageView image,closesearch,opensearch;
    Button btn_retry;
    LinearLayout LI_filter,LI_filterOpen,LI_MapLayout,LI_ButtonLayout;
    Spinner DistrictSpinner;
    RegisterResponse transactions = new RegisterResponse();
    ArrayList<Datares> transactionsspinner = new ArrayList<>();
    ArrayList<String> DistrictList = new ArrayList<String>();
    TextView txt_Sites,txt_Dealers,txt_Agent;
    Boolean IsLocationPermissionGranted=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        CheckLocation();
        displayLocationSettingsRequest(this);
        GetID();



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        Log.i("Map","MapReady");
        Log.i("Map","Location : "+isLocationEnabled(this));
        int height = 100;
        int width = 100;
        Bitmap icon;

        Locationn = new GetLocation(this);
        Log.i("Map","Location : "+Locationn.getLatitude());
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.mapsite);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap siteMarker = Bitmap.createScaledBitmap(b, width, height, false);

        BitmapDrawable bitmapdrawagent = (BitmapDrawable)getResources().getDrawable(R.drawable.mapagent);
        Bitmap b1 = bitmapdrawagent.getBitmap();
        Bitmap agentMarker = Bitmap.createScaledBitmap(b1, width, height, false);

        BitmapDrawable bitmapdrawdealer = (BitmapDrawable)getResources().getDrawable(R.drawable.mapdealer);
        Bitmap b2 = bitmapdrawdealer.getBitmap();
        Bitmap dealerMarker = Bitmap.createScaledBitmap(b2, width, height, false);


        Log.i("Map","maplist-"+mapList.size());
        try {
            if (mapList != null && mapList.size() > 0) {
                for (int i = 0; i < mapList.size(); i++) {
                    if (mapList.get(i).getMapType().equalsIgnoreCase("A")) {
                        icon = agentMarker;
                    } else if (mapList.get(i).getMapType().equalsIgnoreCase("D")) {
                        icon = dealerMarker;
                    } else {
                        icon = siteMarker;
                    }
                    if(!(mapList.get(i).getLat().equalsIgnoreCase("")))
                    {
                        LatLng ll = new LatLng(Double.parseDouble(mapList.get(i).getLat()), Double.parseDouble(mapList.get(i).getLong()));
                        googleMap.addMarker(new MarkerOptions()
                                .position(ll)
                                .anchor(0.5f, 0.5f)
                                .snippet(mapList.get(i).getSnippet())
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                                .title(mapList.get(i).getTitle()));
                    }
                }
                LatLng currentMarker;
                if(DistrictID.equalsIgnoreCase("0"))
                {
                    currentMarker = new LatLng(Locationn.getLatitude(), Locationn.getLongitude());
                }
                else {
                    currentMarker = new LatLng(Double.parseDouble(mapList.get(0).getLat()), Double.parseDouble(mapList.get(0).getLong()));
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMarker, 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

            }
        }
        catch(Exception ex) {
            Log.i("Map","maplist-"+ex.getMessage());
        }
//        LatLng sydney = new LatLng(26.8807606, 80.9976396);
//        googleMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .anchor(0.5f, 0.5f)
//                .snippet("Manish")
//                .icon(BitmapDescriptorFactory.fromBitmap(agentMarker))
//                .title("Agent")).showInfoWindow();
//
//        LatLng sydney1 = new LatLng(26.8802291, 80.987904);
//        googleMap.addMarker(new MarkerOptions()
//                .position(sydney1)
//                .anchor(0.5f, 0.5f)
//                .snippet("Khanna Construction")
//                .icon(BitmapDescriptorFactory.fromBitmap(siteMarker))
//                .title("Site")).showInfoWindow();



    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    public void GetID()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Map View");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        closesearch=findViewById(R.id.closesearch);
        closesearch.setOnClickListener(this);
        opensearch=findViewById(R.id.opensearch);
        opensearch.setOnClickListener(this);
        LI_filter=findViewById(R.id.LI_filter);
        LI_filterOpen=findViewById(R.id.LI_filterOpen);
        image=findViewById(R.id.image);
        DistrictSpinner=findViewById(R.id.DistrictSpinner);
        txt_Agent=findViewById(R.id.txt_Agent);
        txt_Dealers=findViewById(R.id.txt_Dealers);
        txt_Sites=findViewById(R.id.txt_Sites);
        loader = new Loader(this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        Log.i("Map","Hit API");
        LI_ButtonLayout=findViewById(R.id.LI_ButtonLayout);
        LI_MapLayout=findViewById(R.id.LI_MapLayout);
        btn_retry=findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(this);
        if(!isLocationEnabled(this) || IsLocationPermissionGranted==false) {
            LI_MapLayout.setVisibility(View.GONE);
            LI_ButtonLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            LI_MapLayout.setVisibility(View.VISIBLE);
            LI_ButtonLayout.setVisibility(View.GONE);
            HitMapAPI();
            HitDistrict();
        }



    }

    public void SetMapHeight()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        Log.e("ScreenSize","height : "+height);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = (height *65)/100;
//        if(height>1200)
//        {
//
//        }
//        else if(height<1200 && height >800)
//        {
//            params.height = 400;
//        }
//        else if(height<800 && height >600)
//        {
//            params.height = 300;
//        }
        Log.e("ScreenSize","params.height : "+params.height);
        mapFragment.getView().setLayoutParams(params);
    }
    public void HitMapAPI()
    {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);
            UtilMethods.INSTANCE.MapReport(this, DistrictID, loader);


        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }


    }

    public void HitDistrict()
    {
        if (UtilMethods.INSTANCE.isNetworkAvialable(MapActivity.this)) {

        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        UtilMethods.INSTANCE.DistrictList(MapActivity.this,"1", loader,dialog);

    } else {
        UtilMethods.INSTANCE.NetworkError(MapActivity.this, getResources().getString(R.string.network_error_title),
                getResources().getString(R.string.network_error_message));
    }}

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("MapReport")) {
            String startelist=""+activityFragmentMessage.getFrom();
            Log.i("Map","startelist-"+startelist);
            if(!startelist.equalsIgnoreCase("")) {
                mapResponse = new Gson().fromJson(startelist, MapResponse.class);
                if (mapResponse.getStatus().equalsIgnoreCase("1")) {
                    mapList=mapResponse.getList();
                    txt_Sites.setText(mapResponse.getSites());
                    txt_Dealers.setText(mapResponse.getDealers());
                    txt_Agent.setText(mapResponse.getAgents());
                    Log.i("Map","maplistsize-"+mapList.size());
                    SetMapHeight();
                }
            }
        }
        else  if (activityFragmentMessage.getMessage().equalsIgnoreCase("DistrictList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            dataParsesDistrictList(startelist);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if(view==closesearch)
        {
            LI_filter.setVisibility(View.GONE);
            LI_filterOpen.setVisibility(View.VISIBLE);
        }
        if(view==opensearch)
        {
            LI_filter.setVisibility(View.VISIBLE);
            LI_filterOpen.setVisibility(View.GONE);
        }
        if(view==btn_retry)
        {
            CheckLocation();
            if(!isLocationEnabled(this))
            {
                displayLocationSettingsRequest(this);
                LI_MapLayout.setVisibility(View.GONE);
                return;
            }
            else if(IsLocationPermissionGranted==false)
            {
                displayNeverAskAgainDialog();
                LI_MapLayout.setVisibility(View.GONE);
            }
            else
            {
                LI_MapLayout.setVisibility(View.VISIBLE);
                LI_ButtonLayout.setVisibility(View.GONE);
                HitMapAPI();
                HitDistrict();
            }
//            Toast.makeText(this,"IsLocationPermissionGranted "+IsLocationPermissionGranted, Toast.LENGTH_LONG).show();
//
//            if(!isLocationEnabled(this) || IsLocationPermissionGranted==false) {
//                LI_MapLayout.setVisibility(View.GONE);
//                LI_ButtonLayout.setVisibility(View.VISIBLE);
//                //SendtoPermission();
//            }
//            else
//            {
//                LI_MapLayout.setVisibility(View.VISIBLE);
//                LI_ButtonLayout.setVisibility(View.GONE);
//                HitMapAPI();
//                HitDistrict();
//            }

        }
    }

    public void dataParsesDistrictList(String response) {
        Log.e("DPSA","Enter dataParsesProductList");
        DistrictList.add("All District");

        Gson gson = new Gson();
        transactions = gson.fromJson(response, RegisterResponse.class);
        transactionsspinner = transactions.getData();
        Log.e("DPSA","transactionsspinner");
        if (transactionsspinner.size() > 0) {
            Log.e("DPSA","transactionsspinner >0");
            if (transactionsspinner != null && transactionsspinner.size() > 0) {
                Log.e("DPSA",""+transactionsspinner.size());
                for (int i = 0; i < transactionsspinner.size(); i++) {
                    Log.e("DPSA",transactionsspinner.get(i).getDistrictName());
                    DistrictList.add(transactionsspinner.get(i).getDistrictName());

                }
            }


            DistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Log.e("onItemSelected","  position   "+ position + "  ,  id  "+  id +"  ,  cou   ");

                    if (parent.getItemAtPosition(position).toString().equals("All District")) {

                        Log.e("DPSA","0");
                        DistrictID="0";

                    } else {

                        DistrictID=transactionsspinner.get(position-1).getDistrictID();
                        //SiteID = transactionsspinner.get(position-1).getId();

                    }
                    HitMapAPI();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            ArrayAdapter<String> countryAdapter;
            countryAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, DistrictList);
            DistrictSpinner.setAdapter(countryAdapter);
            //Log.e("DPSA",transactionsspinner.get(i).getProductName());
            //image.setVisibility(View.GONE);
            //horizantal.setVisibility(View.VISIBLE);
        } else {

            //image.setVisibility(View.VISIBLE);
            //horizantal.setVisibility(View.GONE);

        }

    }


    public static Boolean isLocationEnabled(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void CheckLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            if (neverAskAgainSelected(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                displayNeverAskAgainDialog();
//            }

            IsLocationPermissionGranted=false;
            return;
        }else{
            IsLocationPermissionGranted=true;
            // Write you code here if permission already given.
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean neverAskAgainSelected(final Activity activity, final String permission) {
        final boolean prevShouldShowStatus = getRatinaleDisplayStatus(activity,permission);
        final boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);

        return prevShouldShowStatus != currShouldShowStatus;
    }

    public static boolean getRatinaleDisplayStatus(final Context context, final String permission) {
        SharedPreferences genPrefs =     context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        return genPrefs.getBoolean(permission, false);
    }
    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Shakti app wants to change your device settings "
                + "\n\nSettings -> Select Shakti App Permissions -> Allow location permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]  grantResults) {
        Log.i(TAG, "requestCode - "+requestCode);
        if (SEND_SMS_PERMISSION_REQUEST_CODE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted successfully");
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_LONG).show();
            } else {
                setShouldShowStatus(this, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
    }
    public static void setShouldShowStatus(final Context context, final String permission) {
        SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = genPrefs.edit();
        editor.putBoolean(permission, true);
        editor.commit();
    }
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i(TAG, "All location settings are satisfied.");
                        IsLocationPermissionGranted=true;
                        //Toast.makeText(this, "Please Enable Location", Toast.LENGTH_LONG).show();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            IsLocationPermissionGranted=false;
                            status.startResolutionForResult(MapActivity.this, 1);
                        } catch (IntentSender.SendIntentException e) {
                            //Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    public  void SendtoPermission()
    {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

}