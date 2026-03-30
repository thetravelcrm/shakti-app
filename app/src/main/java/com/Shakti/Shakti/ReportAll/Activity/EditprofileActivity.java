package com.Shakti.Shakti.ReportAll.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Shakti.Shakti.ApisetRespose.ApiBodyParam;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.ProfileResponse;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Register.ui.RegisterActivity;
import com.Shakti.Shakti.Register.ui.SteatelistAdapter;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import static com.Shakti.Shakti.Dashbord.ui.VerifySiteActivity.getImageOrientation;

public class EditprofileActivity extends AppCompatActivity implements View.OnClickListener {


    Uri picUri;
    String picturePath="",filename="",ext="";
    public static Bitmap bitmap;


    int TAKE_PHOTO_CODE = 0;
    private static final int SELECT_PICTURE = 1;

    Bitmap theImage;


    String encodedString="";
    String photo;
    private static final int MY_CAMERA_REQUEST_CODE = 100;



    Button btregister;
    Loader loader;
    EditText username,lastname;
    EditText contact,email,whatsappcontact,dob,Aadhaarcardnumber,Officeaddress,officepincode,dateofbirth,Anniversarydate,GSTnumber,txtCompanyName;
    TextView workingstate,office_statelist,Workingdistrict,officeDistrict,AgentType,BlockList;;
    Spinner Dealer1,Dealer2,Dealer3;

    RegisterResponse transactions = new RegisterResponse();
    ProfileResponse profileResponses=new  ProfileResponse();

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

    String id="0";
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
ImageView cutclera1,cutclera;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        cutclera1=findViewById(R.id.cutclera1);
        cutclera=findViewById(R.id.cutclera);

        cutclera1.setOnClickListener(this);
        cutclera.setOnClickListener(this);



        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();


        chooseaadharcard=findViewById(R.id.chooseaadharcard);

        imageclick=findViewById(R.id.imageclick);
        chooseaadharcard.setOnClickListener(this);

        rb_agent=findViewById(R.id.rb_agent);
        rb_dealer=findViewById(R.id.rb_dealer);
        li_Dealerlist=findViewById(R.id.li_Dealerlist);
        li_CompanyName=findViewById(R.id.li_CompanyName);
        li_Dealerlist.setVisibility(View.GONE);
        li_Agenttype=findViewById(R.id.li_Agenttype);
        li_Anniversary=findViewById(R.id.li_Anniversary);
        li_Gstno=findViewById(R.id.li_Gstno);
        li_Agenttype.setVisibility(View.GONE);
        li_Dealerlist.setVisibility(View.GONE);
        li_Gstno.setVisibility(View.GONE);
        office_statelist=findViewById(R.id.office_statelist);
//        Dealer1=findViewById(R.id.Dealer1);
//        Dealer2=findViewById(R.id.Dealer2);
//        Dealer3=findViewById(R.id.Dealer3);
        dob=findViewById(R.id.dateofbirth);


        workingstate=findViewById(R.id.workingstate);
        Workingdistrict=findViewById(R.id.Workingdistrict);
        officeDistrict=findViewById(R.id.officeDistrict);
        Aadhaarcardnumber=findViewById(R.id.Aadhaarcardnumber);
        BlockList=findViewById(R.id.blocklist);
        BlockList.setOnClickListener(this);
        AgentType=findViewById(R.id.AgentList);
        AgentType.setOnClickListener(this);
        Workingdistrict.setOnClickListener(this);
        officeDistrict.setOnClickListener(this);



        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

        username=findViewById(R.id.fname);
        lastname=findViewById(R.id.lastname);
        contact=findViewById(R.id.contact);
        email=findViewById(R.id.email);
        whatsappcontact=findViewById(R.id.whatsappcontact);
        txtCompanyName=findViewById(R.id.txtCompanyName);
        Officeaddress=findViewById(R.id.Officeaddress);
        officepincode=findViewById(R.id.officepincode);
        dateofbirth=findViewById(R.id.dateofbirth);
        Anniversarydate=findViewById(R.id.Anniversarydate);
        GSTnumber=findViewById(R.id.GSTnumber);

        btregister=findViewById(R.id.bt_register);

        btregister.setOnClickListener(this);
        rb_agent.setOnClickListener(this);
        rb_dealer.setOnClickListener(this);

        workingstate.setOnClickListener(this);
        office_statelist.setOnClickListener(this);
        dob.setOnClickListener(this);
        Anniversarydate.setOnClickListener(this);


//        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
//
//            loader.show();
//            loader.setCancelable(false);
//            loader.setCanceledOnTouchOutside(false);
//
//            UtilMethods.INSTANCE.UserDetails(this,loader);
//            //UtilMethods.INSTANCE.DealerList(this, loader);
//
//
//        } else {
//            UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
//                    getResources().getString(R.string.network_error_message));
//        }




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.EditProfile);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        li_Dealerlist=findViewById(R.id.li_Dealerlist);
        li_Dealerlist.setOnClickListener(this);

        setValue();
    }


    private void setValue() {
        Log.e("InSetValuebegin","Enter SetValue");
        SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        Log.e("InSetValuebegin","Enter SetValue1");
        String PR=myPreferences.getString(ApplicationConstant.INSTANCE.setProfileDetails,null);
        Log.e("InSetValue",PR);
        profileResponses = new Gson().fromJson(PR, ProfileResponse.class);
        if(profileResponses!=null) {
            final ProfileResponse.ProfileList list = profileResponses.getList().get(0);

            username.setText(list.getfName());
            lastname.setText(list.getlName());
            contact.setText(list.getMobileNumber());
            email.setText(list.getMobileNumber());
            Officeaddress.setText(list.getofficeAddress());
            dob.setText(list.getdob());
            Anniversarydate.setText(list.getanniversary());
            workingstate.setText(list.getworkingState());
            Workingdistrict.setText(list.getworkingDistrict());
            BlockList.setText(list.getworkingBlock());
            whatsappcontact.setText(list.getwhatsappNo());
            workstartid=list.getworkingStateID();
            workingDistrictListid = list.getworkingDistrictID();
            blockid=list.getworkingBlockID();
            usertype = list.getuserType();
            txtCompanyName.setText(list.getcompanyContactName());
            id=list.getID();

        if (usertype.equalsIgnoreCase("3")) {
            // sunadmin
            li_Agenttype.setVisibility(View.GONE);
            li_CompanyName.setVisibility(View.GONE);
        } else if (usertype.equalsIgnoreCase("4")) {
            ///Dealer
            li_Agenttype.setVisibility(View.GONE);
            li_Anniversary.setVisibility(View.VISIBLE);
            li_CompanyName.setVisibility(View.VISIBLE);

        } else if (usertype.equalsIgnoreCase("5")) {
            HitAgentTypeList();
            AgentType.setText(list.getagentType());
            agenttype=list.getagentType();
            li_Agenttype.setVisibility(View.VISIBLE);
            li_Anniversary.setVisibility(View.GONE);
            li_CompanyName.setVisibility(View.GONE);
        }
        }
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

        if (UtilMethods.INSTANCE.isNetworkAvialable(EditprofileActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.BlockList(EditprofileActivity.this,id+"", loader,dialog);

        } else {
            UtilMethods.INSTANCE.NetworkError(EditprofileActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }




    }

    public void HitAgentTypeList() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(EditprofileActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.AgentTypeList(EditprofileActivity.this, loader);

        } else {
            UtilMethods.INSTANCE.NetworkError(EditprofileActivity.this, getResources().getString(R.string.network_error_title),
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


        if (requestCode == TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK)
        {
            theImage = (Bitmap) data.getExtras().get("data");
            //   photo=getEncodedString(theImage);
            setPictures(theImage,"","");

            photo=getEncoded64ImageStringFromBitmap(theImage);

            encodedString=photo;

            Log.v("encodedString","encodedString :    "+encodedString);

            //  Toast.makeText(this, " photo  :  " +  photo, Toast.LENGTH_SHORT).show();

        } else  if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
           /* theImage = (Bitmap) data.getExtras().get("data");
             setPictures(theImage,"","");

             Log.e("theImage","theImage  :  "+  theImage);

            photo=getEncoded64ImageStringFromBitmapGalery(theImage);

            encodedString=photo;

            Log.v("encodedString","encodedString :    "+encodedString);

            //  Toast.makeText(this, " photo  :  " +  photo, Toast.LENGTH_SHORT).show();*/


            if (data != null) {
                Uri contentURI = data.getData();
                //get the Uri for the captured image
                picUri = data.getData();

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = this.getContentResolver().query(contentURI,filePathColumn, null, null, null);
                cursor.moveToFirst();
                Log.v("piccc","pic");
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                Log.v("path",picturePath);
                System.out.println("Image Path : " + picturePath);
                cursor.close();
                filename=picturePath.substring(picturePath.lastIndexOf("/")+1);
                Log.e("fileName",filename);

                ext = getFileType(picturePath);

                String selectedImagePath = picturePath;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 500;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options);

                Matrix matrix = new Matrix();
                matrix.postRotate(getImageOrientation(picturePath));
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bao);
                byte[] ba = bao.toByteArray();

                encodedString = getEncoded64ImageStringFromBitmap(bitmap);

                Log.v("encodedstring",encodedString);

                setPictures(bitmap,  encodedString,filename);
            }
            else
            {
                Toast.makeText(this,"Unable to Select the Image.",Toast.LENGTH_LONG).show();
            }






        } else {

            Toast.makeText(this, "Unable to Select the Image.", Toast.LENGTH_LONG).show();
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

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();



            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

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
            li_Anniversary.setVisibility(View.GONE);


        }






        if(view==rb_dealer){

            usertype="4";

            li_Agenttype.setVisibility(View.GONE);
            li_Anniversary.setVisibility(View.VISIBLE);




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

            count="1";

        }



        if(view==office_statelist){


            HitStartApi();

            count="2";
        }


        if(view==btregister) {


            if (validateForm() == 0) {

                if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);


//                    UtilMethods.INSTANCE.UpdateProfie(this,""+workstartid,""+usertype,"",username.getText().toString().trim()+"",lastname.getText().toString().trim()+"",""+contact.getText().toString().trim(),""+workstartid,
//                            ""+workingDistrictListid,Dealerid1,Dealerid2,Dealerid3,""+Officeaddress.getText().toString().trim(),""+officepincode.getText().toString().trim(),""+officestartid,""+OfficeDistrictlistid,""+dob.getText().toString().trim(),
//                            ""+whatsappcontact.getText().toString().trim(),""+Aadhaarcardnumber.getText().toString().trim()
//                            ,""+GSTnumber.getText().toString().trim(),loader, EditprofileActivity.this,"data:image/png;base64," +
//                                    ""+encodedString);
                    UtilMethods.INSTANCE.UpdateProfie(this,id,workstartid,usertype,username.getText().toString().trim(),
                            lastname.getText().toString().trim(),contact.getText().toString().trim(),workstartid,
                            workingDistrictListid,blockid,Officeaddress.getText().toString().trim(),"",
                            dob.getText().toString().trim(),Anniversarydate.getText().toString().trim(),
                            whatsappcontact.getText().toString().trim(),agenttype,txtCompanyName.getText().toString().trim(),loader,EditprofileActivity.this);

                } else {
                    UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message));
                }

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
        } else if (activityFragmentMessage.getMessage().equalsIgnoreCase("DealerListnew")) {
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
            mAdapter = new SteatelistAdapter(transactionsObjects, this,""+type,"3");
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

        if (UtilMethods.INSTANCE.isNetworkAvialable(EditprofileActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.DistrictList(EditprofileActivity.this,id+"", loader,dialog);

        } else {
            UtilMethods.INSTANCE.NetworkError(EditprofileActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }




    }

    public void ItemClickid(String districtID, String stateID, String districtName, String stateName) {

        dialog.dismiss();


        BlockList.setText("");
        HitBlockList(districtID);


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