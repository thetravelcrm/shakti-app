package com.Shakti.Shakti.ReportAll.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.ProfileResponse;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Register.ui.SteatelistAdapter;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import static com.Shakti.Shakti.Dashbord.ui.VerifySiteActivity.getImageOrientation;

public class KycUpdateActivity extends AppCompatActivity implements View.OnClickListener {


    Uri picUri;
    String picturePath="",filename="",ext="";
    public static Bitmap bitmap;

    ProfileResponse profileResponses=new  ProfileResponse();
    int TAKE_PHOTO_CODE = 0;
    int PIC_CROP=2;
    private static final int SELECT_PICTURE = 1;

    Bitmap theImage;


    String encodedString="";
    String encodedStringgst="";
    String encodedStringadhar="";
    String encodedStringadharback="";
    String photo;
    String ID="0";
    private static final int MY_CAMERA_REQUEST_CODE = 100;


    Button btregister;
    Loader loader;
     EditText  Aadhaarcardnumber,GSTnumber,PANnumber;
     TextView chooseaadharcard,chooseaadharback;

     ImageView adharimageclick,adharimagebackclick;
     String count="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_update);

        adharimageclick=findViewById(R.id.adharimageclick);
        adharimageclick.setOnClickListener(this);
        adharimagebackclick=findViewById(R.id.adharimagebackclick);
        adharimagebackclick.setOnClickListener(this);
         chooseaadharcard=findViewById(R.id.chooseaadharcard);
        chooseaadharcard.setOnClickListener(this);
        chooseaadharback=findViewById(R.id.chooseaadharback);
        chooseaadharback.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.KYCUpdate);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }





        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        Aadhaarcardnumber=findViewById(R.id.Aadhaarcardnumber);
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        GSTnumber=findViewById(R.id.GSTnumber);
        PANnumber=findViewById(R.id.PANnumber);
        btregister=findViewById(R.id.bt_register);
        btregister.setOnClickListener(this);
        setValue();


    }
    private void setValue() {

        SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String PR=myPreferences.getString(ApplicationConstant.INSTANCE.setProfileDetails,null);
        profileResponses = new Gson().fromJson(PR, ProfileResponse.class);
        if(profileResponses!=null) {
            final ProfileResponse.ProfileList list = profileResponses.getList().get(0);
            ID=list.getID();
            PANnumber.setText(list.getpanNumber());
            GSTnumber.setText(list.getgstNumber());
            Aadhaarcardnumber.setText(list.getaadhaarNumber());
            //Glide.with(this).load(list.getaadhaarPhoto()).into(adharimageclick);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECT_PICTURE && resultCode==Activity.RESULT_OK)
        {
            picUri = data.getData();
            performCrop(picUri);
        }
        if(requestCode==TAKE_PHOTO_CODE && resultCode==Activity.RESULT_OK)
        {
            theImage = (Bitmap) data.getExtras().get("data");
            picUri=getImageUri(this, theImage);
            performCrop(picUri);
        }
        if(requestCode==PIC_CROP)
        {
            Bundle extras = data.getExtras();
            theImage = extras.getParcelable("data");
            encodedString=getEncoded64ImageStringFromBitmap(theImage);
            setPictures(theImage,encodedString,"");
        }

//        if (requestCode == TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK)
//        {
//            theImage = (Bitmap) data.getExtras().get("data");
//            //   photo=getEncodedString(theImage);
//
//            photo=getEncoded64ImageStringFromBitmap(theImage);
//
//            encodedString=photo;
//
//            setPictures(theImage,encodedString,"");
//
//
//            Log.v("encodedString","encodedString :    "+encodedString);
//
//            //  Toast.makeText(this, " photo  :  " +  photo, Toast.LENGTH_SHORT).show();
//
//        } else  if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
//
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
//
//
//        } else {
//
//            Toast.makeText(this, "Unable to Select the Image.", Toast.LENGTH_LONG).show();
//        }
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

    public void setPictures(Bitmap b,String base64,String fileName) {

        if(count=="1") {
            encodedStringadhar ="data:image/png;base64," +""+ base64;
            adharimageclick.setVisibility(View.VISIBLE);
            adharimageclick.setImageBitmap(b);
            ViewGroup.LayoutParams layoutParams = adharimageclick.getLayoutParams();
            layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height=ViewGroup.LayoutParams.MATCH_PARENT;
            adharimageclick.setBackground(null);
            Log.v("encodedString",encodedStringadhar);
        }
        else if(count=="2")
        {
            encodedStringadharback ="data:image/png;base64," +""+ base64;
            adharimagebackclick.setImageBitmap(b);
            ViewGroup.LayoutParams layoutParams = adharimagebackclick.getLayoutParams();
            layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height=ViewGroup.LayoutParams.MATCH_PARENT;
            adharimagebackclick.setBackground(null);
        }


    }

    private void performCrop(Uri picUri) {
        //Toast.makeText(this, "Crop Started", Toast.LENGTH_SHORT);
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 4);
            cropIntent.putExtra("aspectY", 3);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 400);
            cropIntent.putExtra("outputY", 300);
            cropIntent.putExtra("scale",true);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public Uri getImageUri(final Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onClick(View view) {


        if(view==adharimageclick){

            showCameraGalleryDialog();

            count="1";

        }
        if(view==adharimagebackclick){

            showCameraGalleryDialog();

            count="2";

        }






        if(view==btregister) {

                if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    // del 4 agent 5

                    UtilMethods.INSTANCE.UpdateKYC(this,ID,""+Aadhaarcardnumber.getText().toString().trim()
                            ,PANnumber.getText().toString().trim(),""+GSTnumber.getText().toString().trim(),loader,
                            this,encodedStringadhar, encodedStringadharback);



                } else {
                    UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message));
                }

            }

    }

}