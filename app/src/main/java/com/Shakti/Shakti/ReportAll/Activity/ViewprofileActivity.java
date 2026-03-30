package com.Shakti.Shakti.ReportAll.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.ProfileResponse;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import static com.Shakti.Shakti.Dashbord.ui.VerifySiteActivity.getImageOrientation;

public class ViewprofileActivity extends AppCompatActivity implements View.OnClickListener{

    Loader loader;
    EditText username,lastname;
    EditText contact,email,whatsappcontact,dob,Aadhaarcardnumber,Officeaddress,officepincode,dateofbirth,Anniversarydate,GSTnumber,PanNumber,txtCompanyName;
    TextView workingstate,office_statelist,Workingdistrict,officeDistrict,AgentType,BlockList;;
    LinearLayout li_Dealerlist,li_Agenttype,li_Anniversary,li_Gstno,li_CompanyName;
    ImageView editprofile,editkyc,imgProfilepic,imgAadharFront,imgAadharBack;
    String usertype= "4";
    String id="0";
    int TAKE_PHOTO_CODE = 0;
    private static final int SELECT_PICTURE = 1;
    private static final int PIC_CROP = 2;
    String count="";
    Bitmap theImage;
    String encodedString="";
    String photo;
    Uri picUri;
    String picturePath="",filename="",ext="";
    public static Bitmap bitmap;
    ProfileResponse profileResponses=new  ProfileResponse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);

        li_Dealerlist=findViewById(R.id.li_Dealerlist);
        li_Dealerlist.setVisibility(View.GONE);
        li_Agenttype=findViewById(R.id.li_Agenttype);
        li_Anniversary=findViewById(R.id.li_Anniversary);
        li_Gstno=findViewById(R.id.li_Gstno);
        li_CompanyName=findViewById(R.id.li_CompanyName);
        li_Agenttype.setVisibility(View.GONE);
        li_Dealerlist.setVisibility(View.GONE);
        li_Gstno.setVisibility(View.VISIBLE);
        office_statelist=findViewById(R.id.office_statelist);

        dob=findViewById(R.id.dateofbirth);


        workingstate=findViewById(R.id.workingstate);
        Workingdistrict=findViewById(R.id.Workingdistrict);
        officeDistrict=findViewById(R.id.officeDistrict);
        Aadhaarcardnumber=findViewById(R.id.Aadhaarcardnumber);
        BlockList=findViewById(R.id.blocklist);
        AgentType=findViewById(R.id.AgentList);

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
        PanNumber=findViewById(R.id.PANnumber);
        imgProfilepic=findViewById(R.id.imgProfilepic);
        imgProfilepic.setOnClickListener(this);
        imgAadharFront=findViewById(R.id.imgAadharFront);
        imgAadharBack=findViewById(R.id.imgAadharBack);
        editprofile=findViewById(R.id.editprofile);
        editkyc=findViewById(R.id.editkyc);
        editprofile.setOnClickListener(this);
        editkyc.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.ViewProfile);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setValue();
    }
    private void setValue() {

        SharedPreferences myPreferences = getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);

        String PR=myPreferences.getString(ApplicationConstant.INSTANCE.setProfileDetails,null);

        profileResponses = new Gson().fromJson(PR, ProfileResponse.class);
        if(profileResponses!=null) {
            final ProfileResponse.ProfileList list = profileResponses.getList().get(0);
            Glide.with(this).load(list.getaadhaarBackPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgAadharBack);
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
            GSTnumber.setText(list.getgstNumber());
            Aadhaarcardnumber.setText(list.getaadhaarNumber());
            PanNumber.setText(list.getpanNumber());
            txtCompanyName.setText(list.getcompanyContactName());
            Glide.with(this).load(list.getPhoto()).into(imgProfilepic);
            Glide.with(this).load(list.getaadhaarPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgAadharFront);

            usertype = list.getuserType();

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

                AgentType.setText(list.getagentType());
                li_Agenttype.setVisibility(View.VISIBLE);
                li_Anniversary.setVisibility(View.GONE);
                li_CompanyName.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onClick(View view) {
        if(view==editprofile)
        {
            startActivity(new Intent(this, EditprofileActivity.class));
        }
        if(view==editkyc)
        {
            startActivity(new Intent(this,KycUpdateActivity.class));
        }
        if(view==imgProfilepic)
        {
            showCameraGalleryDialog();
            count="1";
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
    private void performCrop(Uri picUri) {
        Toast.makeText(this, "Crop Started", Toast.LENGTH_SHORT);
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
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
//        }
//        else  if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
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

        base64="data:image/png;base64," +""+base64;
        imgProfilepic.setImageBitmap(b);
        //adharimageclick.setVisibility(View.VISIBLE);
        //adharimageclick.setImageBitmap(b);

        UtilMethods.INSTANCE.UpdatePhoto(this,id,base64,loader,ViewprofileActivity.this);



    }
}