package com.Shakti.Shakti.Dashbord.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.vincent.filepicker.FileUtils;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.Shakti.Shakti.R;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.vincent.filepicker.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class VerifySiteActivity extends AppCompatActivity  implements View.OnClickListener {

    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    int PIC_CROP=2;
    Uri picUri;
    Bitmap theImage;
    String photo;
    TextView chooseaadharcard, sitepic;
    String id = "";
    String aadaherimage = "";
    Button btsubmit,bt_approve,bt_reject;;
    Loader loader;
    Double latitude, longitude;
    String encodedString;
    String active="active";
    GetLocation Locationn;
    EditText SubAdminRemark;
    ImageView imageclick;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    RadioButton rb_Reject,rb_Approve;
    String mCurrentPhotoPath;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_site);


        rb_Reject=findViewById(R.id.rb_Reject);
        rb_Approve=findViewById(R.id.rb_Approve);

        rb_Approve.setOnClickListener(this);
        rb_Reject.setOnClickListener(this);
        bt_approve=findViewById(R.id.bt_approve);
        bt_reject=findViewById(R.id.bt_reject);
        bt_approve.setOnClickListener(this);
        bt_reject.setOnClickListener(this);


        Locationn = new GetLocation(this);
        latitude = Locationn.getLatitude();
        longitude = Locationn.getLongitude();
        SubAdminRemark=findViewById(R.id.SubAdminRemark);
        imageclick=findViewById(R.id.imageclick);


        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Site Varification");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        OpenCamera(TAKE_PHOTO_CODE);
        ////////////////////////////new cap //////////

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();


        ///////////////////


        btsubmit = findViewById(R.id.btsubmit);

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






            }
        });


        sitepic = findViewById(R.id.sitepic);
        chooseaadharcard = findViewById(R.id.chooseaadharcard);

        id = getIntent().getStringExtra("id");

//
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);


//        chooseaadharcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
//
//            }
//        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK)
        {
//            theImage = (Bitmap) data.getExtras().get("data");
////            picUri=getImageUri(this, theImage);
////            performCrop(picUri);

//            theImage = (Bitmap) data.getExtras().get("data");
//          //   photo=getEncodedString(theImage);
//            setPictures(theImage,"","");
//
//            photo=getEncoded64ImageStringFromBitmap(theImage);
//
//            encodedString=photo;
            //Log.v("encodedString","data:image/png;base64,"+ getBase64Image(theImage));
            //Log.v("encodedString","data:image/png;base64,"+encodedString);

         //  Toast.makeText(this, " photo  :  " +  photo, Toast.LENGTH_SHORT).show();
            try {
                File imgFile = new  File(mCurrentPhotoPath);
                if(imgFile.exists()){
                    loader.show();

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    Bitmap bitmap = rotate(myBitmap, getImageOrientation(mCurrentPhotoPath));

                    encodedString = getEncoded64ImageStringFromBitmap(bitmap);
                    imageclick.setImageBitmap(bitmap);

                    setPictures(bitmap,encodedString,"");
                    Log.e("SiteImg",encodedString);
                    loader.dismiss();

                }
                //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                Uri uri = data.getData();
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                ImgSitePic1.setImageBitmap(bitmap);


            }
            catch (Exception ex){
                Log.d("mylog", "Exception"+ex.getMessage());
            }
         }
        else if(requestCode==PIC_CROP)
        {
            Bundle extras = data.getExtras();
            theImage = extras.getParcelable("data");
            encodedString=getEncoded64ImageStringFromBitmap(theImage);
            setPictures(theImage,encodedString,"");

        }
        else {


            Toast.makeText(this, "Unable to Select the Image.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
    public Uri getImageUri(final Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);

        return imgString;

}

    public void setPictures(Bitmap b,String base64,String fileName) {
        encodedString ="data:image/png;base64," +""+ base64;
        imageclick.setImageBitmap(b);
        Log.v("encodedString",encodedString);
    }

    public static int getImageOrientation(String imagePath) {
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
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

        if(view==bt_approve)
        {
            active="active";
            HitApproveAPI();
        }
        if(view==bt_reject)
        {
            if(SubAdminRemark.getText().toString().trim().equalsIgnoreCase("")) {
                SubAdminRemark.setError("Please Enter Reason of Rejection");
                SubAdminRemark.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
            else
            {
                active="rejected";
                HitApproveAPI();
            }
        }





    }

    private void HitApproveAPI()
    {
        if (UtilMethods.INSTANCE.isNetworkAvialable(VerifySiteActivity.this)) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.SiteVarification(VerifySiteActivity.this, id, latitude, longitude,""+encodedString, ""+SubAdminRemark.getText().toString().trim(),active,loader, VerifySiteActivity.this);

        } else {
            UtilMethods.INSTANCE.NetworkError(VerifySiteActivity.this, getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
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
            cropIntent.putExtra("aspectX", 3);
            cropIntent.putExtra("aspectY", 4);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 400);
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

    private void OpenCamera(int PHOTOCODE){

        File photoFile = null;
        try {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("mylog","Permission is granted");
                //File write logic here
                photoFile = createImageFile();

            }else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.d("mylog", "Exception while creating file: " + ex.toString());
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            try {
                Log.d("mylog", "Photofile not null");
                //Uri photoURI = FileProvider.getUriForFile(Newsubmission.this,"com.vysh.fullsizeimage.fileprovider", photoFile);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Uri photoURI = FileProvider.getUriForFile(VerifySiteActivity.this, getPackageName()+ ".provider", photoFile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, PHOTOCODE);
            }catch (Exception ex)
            {
                Log.d("mylog", "Exception"+ex.getMessage());
            }
        }
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, PHOTOCODE);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        Log.v("mylog","Enter");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.v("mylog","Enter1");
        //Log.v("mylog","Enter1");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.v("mylog","Enter2");
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("mylog", "Path: " + mCurrentPhotoPath);
        return image;
    }
}
