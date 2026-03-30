package com.Shakti.Shakti.Register.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Shakti.Shakti.Dashbord.ui.GetLocation;
import com.Shakti.Shakti.Map.MapActivity;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Utils.ApplicationConstant;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.Shakti.Shakti.Dashbord.ui.VerifySiteActivity.getImageOrientation;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Uri picUri;
    String picturePath="",filename="",ext="";
    public static Bitmap bitmap;
    GetLocation Locationn;

    int TAKE_PHOTO_CODE = 0;
    private static final int SELECT_PICTURE = 1;

    Bitmap theImage;


    String encodedString="";
    String photo;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_LOCATION_REQUEST_CODE = 101;

ImageView cutclera,cutclera1;

    Button btregister;
     Loader loader;
    EditText username,lastname;
     EditText contact,email,whatsappcontact,dob,Aadhaarcardnumber,Officeaddress,officepincode,dateofbirth,Anniversarydate,GSTnumber,txtCompanyName;
    TextView workingstate,office_statelist,Workingdistrict,officeDistrict,AgentType,BlockList;
    Spinner Dealer1,Dealer2,Dealer3;

    RegisterResponse transactions = new RegisterResponse();
    ArrayList<Datares> transactionsObjects = new ArrayList<>();
    ArrayList<Datares> transactionsspinner = new ArrayList<>();

    SteatelistAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

      Dialog dialog;
      String count="";

    String Dealerid1= "0";
    String Dealerid2= "0";
    String Dealerid3= "0";

    ArrayList<String> Dealerlist = new ArrayList<String>();
    ArrayList<String> Dealerlist2 = new ArrayList<String>();
    ArrayList<String> Dealerlist3 = new ArrayList<String>();


    String workstartid= "0";
    String workingDistrictListid= "0";
    String OfficeDistrictlistid= "0";
    String officestartid= "0";
    String blockid="0";
    String usertype= "4";
    String agenttype="";
    RadioButton rb_agent,rb_dealer;

    LinearLayout li_Dealerlist,li_Agenttype,li_Anniversary,li_Gstno,li_CompanyName;

    TextView chooseaadharcard;
    ImageView imageclick;
    Double latitude, longitude;
    boolean IsLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        displayLocationSettingsRequest(this);
        GetId();

    }



     private void GetId() {

         cutclera=findViewById(R.id.cutclera);
         cutclera1=findViewById(R.id.cutclera1);

         cutclera1.setOnClickListener(this);
         cutclera.setOnClickListener(this);
         cutclera.setVisibility(View.GONE);
        cutclera1.setVisibility(View.GONE);

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();


        chooseaadharcard=findViewById(R.id.chooseaadharcard);

        imageclick=findViewById(R.id.imageclick);
        chooseaadharcard.setOnClickListener(this);

        rb_agent=findViewById(R.id.rb_agent);
        rb_dealer=findViewById(R.id.rb_dealer);
        li_Dealerlist=findViewById(R.id.li_Dealerlist);
        li_Agenttype=findViewById(R.id.li_Agenttype);
        li_Anniversary=findViewById(R.id.li_Anniversary);
        li_Gstno=findViewById(R.id.li_Gstno);
        li_Agenttype.setVisibility(View.GONE);
        li_Dealerlist.setVisibility(View.GONE);
        li_CompanyName=findViewById(R.id.li_CompanyName);
        office_statelist=findViewById(R.id.office_statelist);
        AgentType=findViewById(R.id.AgentList);
        //Dealer1=findViewById(R.id.Dealer1);
        Dealer2=findViewById(R.id.Dealer2);
        Dealer3=findViewById(R.id.Dealer3);
        dob=findViewById(R.id.dateofbirth);


        workingstate=findViewById(R.id.workingstate);
        Workingdistrict=findViewById(R.id.Workingdistrict);
        officeDistrict=findViewById(R.id.officeDistrict);
        Aadhaarcardnumber=findViewById(R.id.Aadhaarcardnumber);
        BlockList=findViewById(R.id.blocklist);
         Workingdistrict.setOnClickListener(this);
         officeDistrict.setOnClickListener(this);
        BlockList.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Register");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

         username=findViewById(R.id.fname);
         lastname=findViewById(R.id.lastname);
         contact=findViewById(R.id.contact);
         email=findViewById(R.id.email);
         whatsappcontact=findViewById(R.id.whatsappcontact);

        Officeaddress=findViewById(R.id.Officeaddress);
        officepincode=findViewById(R.id.officepincode);
        dateofbirth=findViewById(R.id.dateofbirth);
        Anniversarydate=findViewById(R.id.Anniversarydate);
        GSTnumber=findViewById(R.id.GSTnumber);
        txtCompanyName=findViewById(R.id.txtCompanyName);
        btregister=findViewById(R.id.bt_register);

        btregister.setOnClickListener(this);
        rb_agent.setOnClickListener(this);
        rb_dealer.setOnClickListener(this);
        AgentType.setOnClickListener(this);
        workingstate.setOnClickListener(this);
        office_statelist.setOnClickListener(this);
        dob.setOnClickListener(this);
        Anniversarydate.setOnClickListener(this);


        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            //loader.show();
            //loader.setCancelable(false);
            //loader.setCanceledOnTouchOutside(false);


            //UtilMethods.INSTANCE.DealerList(this, loader);

        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }
         //HitStartApi();
         //count="1";
        HitAgentTypeList();
    }

    private void HitStartApi() {


        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.StateList(this, loader);


        } else {
            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }


    }

    public void HitBlockList(String id) {

        if (UtilMethods.INSTANCE.isNetworkAvialable(RegisterActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.BlockList(RegisterActivity.this,id+"", loader,dialog);

        } else {
            UtilMethods.INSTANCE.NetworkError(RegisterActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }




    }

    public void HitAgentTypeList() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(RegisterActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.AgentTypeList(RegisterActivity.this, loader);

        } else {
            UtilMethods.INSTANCE.NetworkError(RegisterActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }




    }

    public void showCameraGalleryDialog() {

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.camera_gallery_popup);

        dialog.show();

        RelativeLayout rrCancel = (RelativeLayout) dialog.findViewById(R.id.rl_close);
        LinearLayout llCamera = (LinearLayout) dialog.findViewById(R.id.ll_camera);
        LinearLayout llGallery = (LinearLayout) dialog.findViewById(R.id.ll_gallery);

        rrCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

                dialog.dismiss();
            }
        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, SELECT_PICTURE);
                dialog.dismiss();
            }
        });

    }



    private int mYear, mMonth, mDay, mHour, mMinute;

    public void Datepicker(final String count) {


        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        if(count.equalsIgnoreCase("1")){



                            int in=(monthOfYear + 1);

                            int dd=dayOfMonth;

                            int b=Integer.toString(in).length();
                            int ddd=Integer.toString(dd).length();


                            if(b==2){

                                if(ddd==2){
                                    dateofbirth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }else if(ddd==1){

                                    dateofbirth.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }




                            }else if(b==1){
                                if(ddd==2){
                                    dateofbirth.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                                }else if(ddd==1){

                                    dateofbirth.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                                }
                            }




                        }else if(count.equalsIgnoreCase("2")){


                            int in=(monthOfYear + 1);

                            int dd=dayOfMonth;

                            int b=Integer.toString(in).length();
                            int ddd=Integer.toString(dd).length();


                            if(b==2){

                                if(ddd==2){
                                    Anniversarydate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }else if(ddd==1){

                                    Anniversarydate.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }




                            }else if(b==1){
                                if(ddd==2){
                                    Anniversarydate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                                }else if(ddd==1){

                                    Anniversarydate.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                                }
                            }

                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this,"requestCode -"+requestCode,Toast.LENGTH_LONG).show();

        if (requestCode == TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK)
        {
            theImage = (Bitmap) data.getExtras().get("data");
            //   photo=getEncodedString(theImage);
            setPictures(theImage,"","");

            photo=getEncoded64ImageStringFromBitmap(theImage);

            encodedString=photo;

            Log.v("encodedString","encodedString :    "+encodedString);

            //  Toast.makeText(this, " photo  :  " +  photo, Toast.LENGTH_SHORT).show();

        }
//        else  if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
//
//            if (data != null) {
//                Uri contentURI = data.getData();
//                //get the Uri for the captured image
//                picUri = data.getData();
//
//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                Cursor cursor = this.getContentResolver().query(contentURI,filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                Log.v("piccc","pic");
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                picturePath = cursor.getString(columnIndex);
//                Log.v("path",picturePath);
//                System.out.println("Image Path : " + picturePath);
//                cursor.close();
//                filename=picturePath.substring(picturePath.lastIndexOf("/")+1);
//                Log.e("fileName",filename);
//
//                ext = getFileType(picturePath);
//
//                String selectedImagePath = picturePath;
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(selectedImagePath, options);
//                final int REQUIRED_SIZE = 500;
//                int scale = 1;
//                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) scale *= 2;
//                options.inSampleSize = scale;
//                options.inJustDecodeBounds = false;
//                bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
//
//                Matrix matrix = new Matrix();
//                matrix.postRotate(getImageOrientation(picturePath));
//                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                ByteArrayOutputStream bao = new ByteArrayOutputStream();
//                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bao);
//                byte[] ba = bao.toByteArray();
//
//                encodedString = getEncoded64ImageStringFromBitmap(bitmap);
//
//                Log.v("encodedstring",encodedString);
//
//                setPictures(bitmap,  encodedString,filename);
//            }
//            else
//            {
//                Toast.makeText(this,"Unable to Select the Image.",Toast.LENGTH_LONG).show();
//            }
//
//        }






        else {

            //Toast.makeText(this, "Unable to Select the Image.", Toast.LENGTH_LONG).show();
         }
    }

    public static String getFileType(String path){
        String fileType = null;
        fileType = path.substring(path.indexOf('.',path.lastIndexOf('/'))+1).toLowerCase();
        return fileType;
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;


    }

    public String getEncoded64ImageStringFromBitmapGalery(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;


    }

    public void setPictures(Bitmap b,String base64,String fileName) {

        imageclick.setVisibility(View.VISIBLE);
        imageclick.setImageBitmap(b);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "requestCode- "+requestCode, Toast.LENGTH_LONG).show();
        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();


            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
        if (requestCode == MY_LOCATION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
        else
        {
            displayNeverAskAgainDialog();
        }

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
                            status.startResolutionForResult(RegisterActivity.this, 1);
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
    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Shakti app wants to change your device settings "
                + "\n\nSettings -> Select Shakti App Permissions -> Allow location permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
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
        //builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public Boolean CheckLocation(){
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
            displayNeverAskAgainDialog();
            return false;
        }
        else if(!isLocationEnabled(this))
        {
            displayLocationSettingsRequest(this);
            return  false;
        }
        Locationn = new GetLocation(this);
        latitude = Locationn.getLatitude();
        longitude = Locationn.getLongitude();
        if(latitude!=0.0){
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public void onClick(View view) {


        if(view==cutclera){

            Anniversarydate.setText("");
            cutclera.setVisibility(View.GONE);


        }

        if(view==cutclera1){

            dateofbirth.setText("");
            cutclera1.setVisibility(View.GONE);

        }


        if(view==Workingdistrict){

            SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String startelist = myPreferences.getString(ApplicationConstant.INSTANCE.setDistrictList, null);
            //statepopup(startelist,"District");
            if(startelist=="" || startelist==null){
                HitStartApi();
            }
            else{
                startelist = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDistrictList, null);
                statepopup(startelist,"District");
            }
           count="1";


        }


  if(view==officeDistrict){

            SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
            String startelist = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDistrictList, null);
            statepopup(startelist,"District");
            BlockList.setText("");
           count="2";


        }
        if(view==AgentType){
            SharedPreferences sp=getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,MODE_PRIVATE);
            String at=""+sp.getString(ApplicationConstant.INSTANCE.setAgentTypeList,null);
            statepopup(at,"AgentType");

        }
        if(view==BlockList){
            SharedPreferences sp=getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,MODE_PRIVATE);
            String at=sp.getString(ApplicationConstant.INSTANCE.setBlockList,null);
            //Toast.makeText(this, at, Toast.LENGTH_SHORT).show();
            if(at=="" || at==null){
                HitBlockList(workingDistrictListid);
            }
            else{
                at=""+sp.getString(ApplicationConstant.INSTANCE.setBlockList,null);
                statepopup(at,"Block");
            }
        }


        if(view==chooseaadharcard){

            showCameraGalleryDialog();

        }




        if(view==dateofbirth){

            Datepicker("1");
            cutclera1.setVisibility(View.VISIBLE);
        }




       if(view==Anniversarydate){

           Datepicker("2");
           cutclera.setVisibility(View.VISIBLE);

        }

       if(view==rb_agent){
            usertype="5";
            li_Agenttype.setVisibility(View.VISIBLE);
            li_CompanyName.setVisibility(View.GONE);
            li_Anniversary.setVisibility(View.GONE);
            li_Gstno.setVisibility(View.GONE);
            rb_agent.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorPrimary));
            rb_dealer.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorBackground));
           rb_agent.setTextColor(this.getResources().getColorStateList(R.color.white));
           rb_dealer.setTextColor(this.getResources().getColorStateList(R.color.black));
            //li_Dealerlist.setVisibility(View.VISIBLE);

        }



        
        
        
         if(view==rb_dealer){

            usertype="4";
             li_Agenttype.setVisibility(View.GONE);
             li_Dealerlist.setVisibility(View.GONE);
             li_Anniversary.setVisibility(View.VISIBLE);
             li_Gstno.setVisibility(View.VISIBLE);
             li_CompanyName.setVisibility(View.VISIBLE);
             rb_agent.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorBackground));
             rb_dealer.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorPrimary));
             rb_agent.setTextColor(this.getResources().getColorStateList(R.color.black));
             rb_dealer.setTextColor(this.getResources().getColorStateList(R.color.white));

        }
        
  if(view==workingstate){
      SharedPreferences sp=getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,MODE_PRIVATE);
      String at=sp.getString(ApplicationConstant.INSTANCE.setStateList,null);
      if(at==null){
          HitStartApi();
      }
      else{
          at=""+sp.getString(ApplicationConstant.INSTANCE.setStateList,null);
          statepopup(at,"State");
      }
      //HitStartApi();

      count="1";


        }





 if(view==office_statelist){


            HitStartApi();

     count="2";
        }


        if(view==btregister) {


             if (validateForm() == 0) {
                 if(CheckLocation()) {

                     if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

                         loader.show();
                         loader.setCancelable(false);
                         loader.setCanceledOnTouchOutside(false);

                         // del 4 agent 5
                         //Toast.makeText(this, "Before Method", Toast.LENGTH_SHORT).show();
                         UtilMethods.INSTANCE.UserRegistration(this, "" + workstartid, "" + usertype, "", username.getText().toString().trim() + "", lastname.getText().toString().trim() + "", "" + contact.getText().toString().trim(), "" + workstartid,
                                 "" + workingDistrictListid, blockid, Dealerid1, Dealerid2, Dealerid3, "" + Officeaddress.getText().toString().trim(), "" + officepincode.getText().toString().trim(), "" + officestartid, "" + OfficeDistrictlistid, "" + dob.getText().toString().trim(),
                                 "" + whatsappcontact.getText().toString().trim(), "" + Aadhaarcardnumber.getText().toString().trim()
                                 , "" + GSTnumber.getText().toString().trim(), loader, RegisterActivity.this, "data:image/png;base64," +
                                         "" + encodedString, latitude, longitude, agenttype,txtCompanyName.getText().toString().trim());


                     } else {
                         UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                                 getResources().getString(R.string.network_error_message));

                     }
                 }
             }
else {
    Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
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

        } else if (activityFragmentMessage.getMessage().equalsIgnoreCase("DistrictList")) {
            String startelist=""+activityFragmentMessage.getFrom();
            statepopup(startelist,"District");

        } else if(activityFragmentMessage.getMessage().equalsIgnoreCase("BlockList")){
            String blocklist=""+activityFragmentMessage.getFrom();
            statepopup(blocklist,"Block");
        }
        else if(activityFragmentMessage.getMessage().equalsIgnoreCase("AgentType")){
            String AgentType=""+activityFragmentMessage.getMessage();
            statepopup(AgentType,"AgentType");
        }
        else if (activityFragmentMessage.getMessage().equalsIgnoreCase("DealerListnew")) {
            String startelist=""+activityFragmentMessage.getFrom();

            dataParseDealerList(startelist);
            dataParseDealerList2(startelist);
            dataParseDealerList3(startelist);

        }

    }


    public void dataParseDealerList(String response) {

        Dealerlist.add("Dealer 1");
        Gson gson = new Gson();
        transactions = gson.fromJson(response, RegisterResponse.class);
        transactionsspinner = transactions.getData();

        if (transactionsspinner.size() > 0) {

            if (transactionsspinner != null && transactionsspinner.size() > 0) {
                for (int i = 0; i < transactionsspinner.size(); i++) {
                    Dealerlist.add(transactionsspinner.get(i).getName());
                }
            }

            Dealer1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Log.e("spinner","  position   "+ position + "  ,  id  "+  id);

                    if (parent.getItemAtPosition(position).toString().equals("Dealer 1")) {
                        Dealerid1="";
                    } else {
                        Dealerid1 = transactionsspinner.get(position-1).getId();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            ArrayAdapter<String> Dealerlist1Adapter;
            Dealerlist1Adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, Dealerlist);
            Dealer1.setAdapter(Dealerlist1Adapter);


        } else {

        }

    }

    public void dataParseDealerList2(String response) {

        Dealerlist2.add("Dealer-2");
        Gson gson = new Gson();
        transactions = gson.fromJson(response, RegisterResponse.class);
        transactionsspinner = transactions.getData();

        if (transactionsspinner.size() > 0) {

            if (transactionsspinner != null && transactionsspinner.size() > 0) {
                for (int i = 0; i < transactionsspinner.size(); i++) {
                    Dealerlist2.add(transactionsspinner.get(i).getName());
                }
            }

            Dealer2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Log.e("spinner","  position   "+ position + "  ,  id  "+  id);

                    if (parent.getItemAtPosition(position).toString().equals("Dealer-2")) {
                        Dealerid2="";
                    } else {
                        Dealerid2 = transactionsspinner.get(position-1).getId();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            ArrayAdapter<String> Dealerlist2Adapter;
            Dealerlist2Adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, Dealerlist2);
            Dealer2.setAdapter(Dealerlist2Adapter);

        } else {


        }

    }

    public void dataParseDealerList3(String response) {

        Dealerlist3.add("Dealer-3");
        Gson gson = new Gson();
        transactions = gson.fromJson(response, RegisterResponse.class);
        transactionsspinner = transactions.getData();

        if (transactionsspinner.size() > 0) {

            if (transactionsspinner != null && transactionsspinner.size() > 0) {
                for (int i = 0; i < transactionsspinner.size(); i++) {

                    Dealerlist3.add(transactionsspinner.get(i).getName());

                }
            }

            Dealer3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Log.e("spinner","  position   "+ position + "  ,  id  "+  id);

                    if (parent.getItemAtPosition(position).toString().equals("Dealer-3")) {
                        Dealerid3="";
                    } else {

                        Dealerid3 = transactionsspinner.get(position-1).getId();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            ArrayAdapter<String> Dealerlist3Adapter;
            Dealerlist3Adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, Dealerlist3);
            Dealer3.setAdapter(Dealerlist3Adapter);

        } else {

        }

    }






    private void statepopup(String startelist,final String type) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.startuppop, null);

       RecyclerView recycleview = (RecyclerView) view.findViewById(R.id.recycler_view);
       ImageView cut =  view.findViewById(R.id.cut);
        EditText area_serch =  view.findViewById(R.id.area_serch);
       TextView name =  view.findViewById(R.id.name);
       name.setText(""+ type);


        dialog = new Dialog(this);

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
                for(Datares op:transactionsObjects)
                {

                    String getName="";

                    if(type.equalsIgnoreCase("State")){

                        getName=op.getName().toLowerCase();

                    }else if(type.equalsIgnoreCase("District")){

                        getName=op.getDistrictName().toLowerCase();

                    }
                    else if(type.equalsIgnoreCase("Block")){
                        getName=op.getBlockName().toLowerCase();

                    }



                    if(getName.contains(newText)){

                        newlist.add(op);

                    }
                }
                mAdapter.filter(newlist);



            }
        });


        Gson gson = new Gson();
        transactions = gson.fromJson(startelist, RegisterResponse.class);
        transactionsObjects = transactions.getData();

        if (transactionsObjects.size() > 0) {
            mAdapter = new SteatelistAdapter(transactionsObjects, this,""+type,"1");
            mLayoutManager = new LinearLayoutManager(this);
            recycleview.setLayoutManager(mLayoutManager);
            recycleview.setItemAnimator(new DefaultItemAnimator());
            recycleview.setAdapter(mAdapter);

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


    public int validateForm() {


        int flag = 0;



if (username.getText() != null && username.getText().toString().trim().length() > 0) {
    //emailLayout.setErrorEnabled(false);
    // userEmail = email.getText().toString().trim();

} else {
    username.setError(getResources().getString(R.string.err_msg_username));
    username.requestFocus();
    flag++;
}





if (contact.getText() != null && contact.getText().toString().trim().length() > 0) {

        } else {
    contact.setError(getResources().getString(R.string.err_msg_contact));
    contact.requestFocus();
            flag++;
        }

if (lastname.getText() != null && lastname.getText().toString().trim().length() > 0) {

        } else {
    lastname.setError(getResources().getString(R.string.err_msg_lastname));
    lastname.requestFocus();
            flag++;
        }


if ( !workstartid.equalsIgnoreCase("0")) {

        } else {

    Toast.makeText(this, "Please Select Working State", Toast.LENGTH_SHORT).show();
            flag++;
        }



if ( !workingDistrictListid.equalsIgnoreCase("0")) {

}
else {
    Toast.makeText(this, "Please Select Working District", Toast.LENGTH_SHORT).show();
    flag++;
}



/*
if (!officestartid.equalsIgnoreCase("0")) {

        } else {

    Toast.makeText(this, "Please Select Office State", Toast.LENGTH_SHORT).show();
            flag++;
        }




if ( !OfficeDistrictlistid.equalsIgnoreCase("0")) {

        } else {

    Toast.makeText(this, "Please Select Office District", Toast.LENGTH_SHORT).show();
            flag++;

        }



if ( Officeaddress.getText().toString().trim().length() > 0) {

        } else {

    Toast.makeText(this, "Please Enter  Office Address", Toast.LENGTH_SHORT).show();
            flag++;
        }


if ( officepincode.getText().toString().trim().length() > 0) {

        } else {

    Toast.makeText(this, "Please Enter  Office PinCode", Toast.LENGTH_SHORT).show();
            flag++;
        }*/

/*

if ( Dealerid1.equalsIgnoreCase("")) {

        } else {

    Toast.makeText(this, "Please Select Dealerid-1", Toast.LENGTH_SHORT).show();
            flag++;
        }



if ( Dealerid2.equalsIgnoreCase("")) {

        } else {

    Toast.makeText(this, "Please Select Dealerid-2", Toast.LENGTH_SHORT).show();
            flag++;
        }



if ( Dealerid3.equalsIgnoreCase("")) {

        } else {

    Toast.makeText(this, "Please Select Dealerid-3", Toast.LENGTH_SHORT).show();
            flag++;
        }
*/


        return flag;
    }

    public void ItemClick(String id) {

        if (UtilMethods.INSTANCE.isNetworkAvialable(RegisterActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.DistrictList(RegisterActivity.this,id+"", loader,dialog);

        } else {
            UtilMethods.INSTANCE.NetworkError(RegisterActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }




    }

    public void ItemClickid(String districtID, String stateID, String districtName, String stateName) {

        dialog.dismiss();
        BlockList.setText("");
        HitBlockList(districtID);
        //BlockList.setText("");
    //    Toast.makeText(this, "stateID  "  + stateID  +"districtID  "+  districtID   , Toast.LENGTH_SHORT).show();



        if(count.equalsIgnoreCase("1")){



    workingstate.setText(""+stateName);
    Workingdistrict.setText(""+districtName);

    workstartid= ""+stateID;
      workingDistrictListid= ""+districtID;

}else if(count.equalsIgnoreCase("2")){

    office_statelist.setText(""+stateName);
    officeDistrict.setText(""+districtName);
      OfficeDistrictlistid= ""+districtID;
      officestartid= ""+stateID;


}

    }

    public void ItemClickidBlockList(String id,String name) {

        dialog.dismiss();
        blockid=id;
        BlockList.setText(""+name);


    }

    public void ItemClickidAgentTypeList(String name) {

        dialog.dismiss();
        agenttype=name;
        AgentType.setText(""+name);


    }
}

