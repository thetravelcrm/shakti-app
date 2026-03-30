package com.Shakti.Shakti.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.Shakti.Shakti.ApisetRespose.ApiBodyParam;
import com.Shakti.Shakti.BuildConfig;
import com.Shakti.Shakti.ChangePasswordActivity;
import com.Shakti.Shakti.Dashbord.dto.DashBoardAgentApiRes;
import com.Shakti.Shakti.Dashbord.ui.MainActivity;
import com.Shakti.Shakti.Dashbord.ui.TermsconditionActivity;
import com.Shakti.Shakti.Map.MapResponse;
import com.Shakti.Shakti.Register.dto.ProfileResponse;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.SubmitPurchase.dto.SiteResponse;
import com.Shakti.Shakti.SubmitPurchase.ui.MyAccountActivity;
import com.Shakti.Shakti.Youtube.ui.RewardListActivity;
import com.Shakti.Shakti.Youtube.ui.VideosListActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.Shakti.Shakti.Login.ui.LoginActivity;
 import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Splash.ui.Splash;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public enum UtilMethods {

    INSTANCE;
    Boolean IsVersionValid,IsSessionValid,IsAppValid;
    public String getDeviceId(Context context) {


        String deviceId = "252525252525253";
        return deviceId;
    }

    public Boolean CheckAppDefault(Context context)
    {
        if(!IsVersionValid || !IsSessionValid || !IsAppValid)
        {

            return true;
        }
        else {
            //logout(context);
            return true;
        }
    }
    public void setgetKeyId(Context context, String token) {


        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.token, token);
        editor.commit();



    }



    public void PointStatusReport(final Context context, final Loader loader, final HorizontalScrollView horizantal, final ImageView image) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
           String Sessiondeta = balanceCheckResponse.getSession();
        //String Sessiondeta = "0319c914fa8e5dc677a11ff7d24c7ca6";
        String userid = balanceCheckResponse.getUserID();
        //String userid = "2";


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("RoleID","0");

        Log.e("SiteReport",""+jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.PointStatusReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("SiteReportres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {

                                IsAppValid=Boolean.parseBoolean(response.body().getIsAppValid());
                                IsSessionValid=Boolean.parseBoolean(response.body().getIsSessionVaild());
                                IsVersionValid=Boolean.parseBoolean(response.body().getIsVersionValid());
                                if(!CheckAppDefault(context))
                                {
                                    return;
                                }
                                else if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    UtilMethods.INSTANCE.setPointStatusReport(context,new Gson().toJson(response.body()).toString());

                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("PointStatusReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                }
                                else {

                                    horizantal.setVisibility(View.GONE);
                                    image.setVisibility(View.VISIBLE);

                                    //  UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }







                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }



    public String getRecentLogin(Context context) {
        SharedPreferences myPrefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String key = myPrefs.getString(ApplicationConstant.INSTANCE.regRecentLoginPref, null);
        return key;
    }

    public void setRecentLogin(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.regRecentLoginPref,key);
        editor.commit();
    }

    public void Processing(final Context context, final String message) {
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Processing")
                .setContentText(message)
                .setCustomImage(R.drawable.processing)
                .show();
    }


    public void Successful(final Context context, final String message, final int i, final Activity register) {

        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        pDialog.setTitleText(context.getResources().getString(R.string.successful_title));
        pDialog.setContentText(message);
        pDialog.setCustomImage(AppCompatResources.getDrawable(context,R.drawable.ic_tick));
        pDialog.setCancelable(false);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (i==1){

                    context.startActivity(new Intent(context, LoginActivity.class));
                    sweetAlertDialog.dismiss();
                    register.finish();

                } else  if (i==2){

                    sweetAlertDialog.dismiss();
                    register.finish();

                }else  if (i==3){

                    context.startActivity(new Intent(context, MainActivity.class));
                    sweetAlertDialog.dismiss();
                    register.finish();


                    FragmentActivityMessage
                            fragmentActivityMessage =
                            new FragmentActivityMessage("setValuedeta","");
                    GlobalBus.getBus().post(fragmentActivityMessage);

                }
                else if(i==4)
                {
                    //context.startActivity(new Intent(context, h));
                }
                else {

                    sweetAlertDialog.dismiss();

                }
            }
        });
        pDialog.show();
    }

    public void Failed(final Context context, final String message, final int i) {

        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        //pDialog.setTitleText(context.getResources().getString(R.string.attention_error_title));
        pDialog.setContentText(message);
        pDialog.setCustomImage(AppCompatResources.getDrawable(context,R.drawable.norecordfound));

        pDialog.setCancelable(false);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (i==1){
                    sweetAlertDialog.dismiss();

                   /* context.startActivity(new Intent(context, LoginActivity.class));
                    sweetAlertDialog.dismiss();*/
                }
                else {
                    sweetAlertDialog.dismiss();
                }
            }
        });
        pDialog.show();
    }

    public void NetworkError(final Context context, String title, final String message) {
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setCustomImage(AppCompatResources.getDrawable(context,R.drawable.ic_connection_lost_24dp))
                .show();
    }

    public boolean isNetworkAvialable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    TextView textotp,resendotp,resend;
    public  EditText  edMobileFwp;
    public TextInputLayout tilMobileFwp;

    public Button FwdokButton;
    public Button cancelButton;
    private static CountDownTimer countDownTimer;


    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                resendotp.setText(hms);//set text
            }

            public void onFinish() {

                resendotp.setVisibility(View.GONE);
                resend.setVisibility(View.VISIBLE);

                countDownTimer = null;//set CountDownTimer to null
                //  resendotp.setText(getString(R.string.start_timer));//Change button text
            }
        }.start();

    }


    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


    public void setLoginrespose(Context context, String Loginrespose ,String one ) {

        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.Loginrespose, Loginrespose);
        editor.putString(ApplicationConstant.INSTANCE.one, one);
        editor.commit();

    }



    public void UserRegistration(final Context context, final String StateID, final String UserType, final String UserCode, final String fName,
                                 final String lName, final String MobileNo, final String WorkingStateID, final String WorkingDistrictID,final String WorkingBlockID,
                                 final String Dealer1ID, final String Dealer2ID, final String Dealer3ID, final String OfficeAddress, final String OfficePINCODE,
                                 final String OfficeStateID, final String OfficeDistrictID, final String Dob, String WhatsappNo,
                                 String Aadhaarcardnumber, String GSTnumber, final Loader loader, final Activity activity,String AadhaarPhoto,final Double Lat,final Double Long,final String AgentType,final String CompanyName) {

        Toast.makeText(context, "Lat - "+Lat, Toast.LENGTH_SHORT).show();

        Log.d("Login", "is : " + "StateID : " + StateID + " , UserType : " + UserType);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            ApiBodyParam apiBodyParam = new ApiBodyParam();


            apiBodyParam.setaPPID(ApplicationConstant.INSTANCE.APPID);
            apiBodyParam.setiMEI(UtilMethods.INSTANCE.getDeviceId(context));
            apiBodyParam.setRegKey("online");
            apiBodyParam.setVersion(BuildConfig.VERSION_NAME);
            apiBodyParam.setStateID(StateID);
            apiBodyParam.setUserType(UserType);
            apiBodyParam.setUserCode(UserCode);
            apiBodyParam.setfName(fName);
            apiBodyParam.setlName(lName);
            apiBodyParam.setMobileNo(MobileNo);
            apiBodyParam.setWhatsappNo(WhatsappNo);
            apiBodyParam.setEmailID("");
            apiBodyParam.setWorkingStateID(WorkingStateID);
            apiBodyParam.setWorkingDistrictID(WorkingDistrictID);
            apiBodyParam.setWorkingBlockID(WorkingBlockID);
            apiBodyParam.setDealer1ID(Dealer1ID);
            apiBodyParam.setDealer2ID(Dealer2ID);
            apiBodyParam.setDealer3ID(Dealer3ID);
            apiBodyParam.setOfficeAddress(OfficeAddress);
            apiBodyParam.setOfficePINCODE(OfficePINCODE);
            apiBodyParam.setOfficeCity("");
            apiBodyParam.setOfficeStateID(OfficeStateID);
            apiBodyParam.setOfficeDistrictID(OfficeDistrictID);
            apiBodyParam.setDob(Dob);
            apiBodyParam.setAnniversary("");
            apiBodyParam.setAadhaarNumber(Aadhaarcardnumber);
            apiBodyParam.setAadhaarPhoto(AadhaarPhoto);
            apiBodyParam.setGstNumber(GSTnumber);
            apiBodyParam.setOfficeLat(Lat.toString());
            apiBodyParam.setOfficeLong(Long.toString());
            apiBodyParam.setPhoto("");
            apiBodyParam.setPanNumber("");
            apiBodyParam.setCompanyContactName(CompanyName);
            apiBodyParam.setAgentType(AgentType);
            Call<RegisterResponse> call = git.UserRegistration(apiBodyParam);

            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse>response) {
                    Log.d("Login", "is : " + new Gson().toJson(response.body()).toString());


                    if(response!=null)
                    {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }

                        try {
                            if (response.body().getStatuscode()!=null && response.body().getStatuscode().equalsIgnoreCase("1")) {

                                UtilMethods.INSTANCE.Successful(context, response.body().getMsg(),1,activity);


                            } else {
                                if (loader != null) {
                                    if (loader.isShowing())
                                        loader.dismiss();
                                }
                                UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.e("exception",ex.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse>call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("response", t.getMessage());
                   // UtilMethods.INSTANCE.Error(context, context.getResources().getString(R.string.network_error));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void UserDetails(final Context context, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("Session",Sessiondeta);
        //Log.e("newstartelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<ProfileResponse> call = git.UserDetails(jsonObj);
            call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, final retrofit2.Response<ProfileResponse> response) {
                    Log.e("newstartelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {
                                //Log.e("userdetailsresponse",response.body().getStatus());
                                IsAppValid=Boolean.parseBoolean(response.body().getIsAppValid());
                                IsSessionValid=Boolean.parseBoolean(response.body().getIsSessionVaild());
                                IsVersionValid=Boolean.parseBoolean(response.body().getIsVersionValid());
                                if(!CheckAppDefault(context))
                                {
                                    return;
                                }
                                if(response.body().getStatus().equalsIgnoreCase("1")){
                                    UtilMethods.INSTANCE.setProfileDetails(context, new Gson().toJson(response.body()).toString());

                                    Log.e("userdetailsresponseset",new Gson().toJson(response.body()).toString());
                                }
                                else{

                                }
                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


 public void UpdateKYC(final Context context,String ID,String Aadhaarcardnumber,String Pancardnumber, String GSTnumber, final Loader loader, final Activity activity,String AadhaarPhoto,String AadhaarBackPhoto) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
     String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
     RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
     String Sessiondeta = balanceCheckResponse.getSession();
     String userid = balanceCheckResponse.getUserID();
     RegisterResponse balanceCheckResponsevv = new Gson().fromJson(balanceResponse, RegisterResponse.class);
     String UserCodemnm=""+balanceCheckResponsevv.getUserRole();

     JsonObject jsonObj = new JsonObject();
     jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
     jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
     jsonObj.addProperty("RegKey", "Online");
     jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
     jsonObj.addProperty("session",Sessiondeta);
     jsonObj.addProperty("UserID",userid);
     jsonObj.addProperty("Usertype",UserCodemnm);
     jsonObj.addProperty("id",ID);
     jsonObj.addProperty("aadhaarNumber",Aadhaarcardnumber);
     jsonObj.addProperty("panNumber",Pancardnumber);
     jsonObj.addProperty("aadhaarPhoto",AadhaarPhoto);
     jsonObj.addProperty("aadhaarBackPhoto",AadhaarBackPhoto);
     jsonObj.addProperty("gstNumber",GSTnumber);

     Log.e("UpdateKYC", "  apiBodyParam   : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);

            Call<UpdateKYCrespose> call = git.UpdateKYC(jsonObj);
    call.enqueue(new Callback<UpdateKYCrespose>() {

        @Override
                public void onResponse(Call<UpdateKYCrespose> call, final retrofit2.Response<UpdateKYCrespose>response) {
                    Log.e("UpdateKYCres", "is : " + new Gson().toJson(response.body()).toString());


                    if(response!=null)
                    {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }

                        try {
                            if (response.body().getStatuscode()!=null &&
                                    response.body().getStatuscode().equalsIgnoreCase("1")) {
                                UtilMethods.INSTANCE.UserDetails(context,loader);
                                UtilMethods.INSTANCE.Successful(context, response.body().getMsg(),2,activity);


                            } else {

                                UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.e("exception",ex.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateKYCrespose>call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("response", t.getMessage());
                   // UtilMethods.INSTANCE.Error(context, context.getResources().getString(R.string.network_error));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


  public void UpdateProfie(final Context context,String ID, final String StateID, final String UserType, final String fName,
                                 final String lName, final String MobileNo, final String WorkingStateID, final String WorkingDistrictID,
                                 final String WorkingBlockID, final String OfficeAddress, final String OfficePINCODE,
                                 final String Dob,String Anniversary, String WhatsappNo, String AgentType, String CompanyName,
                                 final Loader loader, final Activity activity) {


      SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
      String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
      RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
      String Sessiondeta = balanceCheckResponse.getSession();
      String userid = balanceCheckResponse.getUserID();
      RegisterResponse balanceCheckResponsevv = new Gson().fromJson(balanceResponse, RegisterResponse.class);
      String UserCodemnm=""+balanceCheckResponsevv.getUserRole();

        Log.d("Login", "is : " + "StateID : " + StateID + " , UserType : " + UserType);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            ApiBodyParam apiBodyParam = new ApiBodyParam();

            apiBodyParam.setaPPID(ApplicationConstant.INSTANCE.APPID);
            apiBodyParam.setiMEI(UtilMethods.INSTANCE.getDeviceId(context));
            apiBodyParam.setRegKey("online");
            apiBodyParam.setVersion(BuildConfig.VERSION_NAME);
            apiBodyParam.setID(ID);
            apiBodyParam.setStateID(StateID);
            apiBodyParam.setUserType(UserType);
            apiBodyParam.setAgentType(AgentType);
            apiBodyParam.setfName(fName);
            apiBodyParam.setUserID(userid);
            apiBodyParam.setSession(Sessiondeta);
            apiBodyParam.setlName(lName);
            apiBodyParam.setMobileNo(MobileNo);
            apiBodyParam.setWhatsappNo(WhatsappNo);
            apiBodyParam.setEmailID("");
            apiBodyParam.setWorkingStateID(WorkingStateID);
            apiBodyParam.setWorkingDistrictID(WorkingDistrictID);
            apiBodyParam.setWorkingBlockID(WorkingBlockID);
            apiBodyParam.setDealer1ID("0");
            apiBodyParam.setDealer2ID("0");
            apiBodyParam.setDealer3ID("0");
            apiBodyParam.setOfficeAddress(OfficeAddress);
            apiBodyParam.setOfficePINCODE(OfficePINCODE);
            apiBodyParam.setOfficeCity("");
            apiBodyParam.setOfficeStateID("0");
            apiBodyParam.setOfficeDistrictID("0");
            apiBodyParam.setDob(Dob);
            apiBodyParam.setAnniversary(Anniversary);
            apiBodyParam.setOfficeLat("");
            apiBodyParam.setOfficeLong("");
            apiBodyParam.setPhoto("");
            apiBodyParam.setCompanyContactName(CompanyName);

            Log.d("apiBodyParam", "   apiBodyParam   : " + apiBodyParam);

            Call<UpdateKYCrespose> call = git.UpdateProfie(apiBodyParam);

            call.enqueue(new Callback<UpdateKYCrespose>() {
                @Override
                public void onResponse(Call<UpdateKYCrespose> call, final retrofit2.Response<UpdateKYCrespose>response) {
                    Log.d("apiBodyParamres", "is : " + new Gson().toJson(response.body()).toString());


                    if(response!=null)
                    {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }

                        try {
                            if (response.body().getStatuscode()!=null && response.body().getStatuscode().equalsIgnoreCase("1")) {
                                UtilMethods.INSTANCE.UserDetails(context,loader);
                                UtilMethods.INSTANCE.Successful(context, response.body().getMsg(),2,activity);


                            } else {
                                if (loader != null) {
                                    if (loader.isShowing())
                                        loader.dismiss();
                                }
                                UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.e("exception",ex.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateKYCrespose>call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("response", t.getMessage());

                   // UtilMethods.INSTANCE.Error(context, context.getResources().getString(R.string.network_error));

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


public void UpdatePhoto(final Context context,String ID,String Photo,final Loader loader,final  Activity activity)
{
    SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
    String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
    RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
    String Sessiondeta = balanceCheckResponse.getSession();
    String userid = balanceCheckResponse.getUserID();
    RegisterResponse balanceCheckResponsevv = new Gson().fromJson(balanceResponse, RegisterResponse.class);
    String UserCodemnm=""+balanceCheckResponsevv.getUserRole();

    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
    jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
    jsonObj.addProperty("RegKey", "Online");
    jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
    jsonObj.addProperty("session",Sessiondeta);
    jsonObj.addProperty("UserID",userid);
    jsonObj.addProperty("Usertype",UserCodemnm);
    jsonObj.addProperty("id",ID);
    jsonObj.addProperty("photo",Photo);
    try {
        EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);

        Call<UpdateKYCrespose> call = git.UpdatePhoto(jsonObj);
        call.enqueue(new Callback<UpdateKYCrespose>() {

            @Override
            public void onResponse(Call<UpdateKYCrespose> call, final retrofit2.Response<UpdateKYCrespose>response) {
                Log.e("UpdatePhotores", "is : " + new Gson().toJson(response.body()).toString());


                if(response!=null)
                {
                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }

                    try {
                        if (response.body().getStatuscode()!=null &&
                                response.body().getStatuscode().equalsIgnoreCase("1")) {
                            UtilMethods.INSTANCE.UserDetails(context,loader);
                            UtilMethods.INSTANCE.Successful(context, response.body().getMsg(),2,activity);


                        } else {

                            UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                        }
                    }
                    catch (Exception ex)
                    {
                        Log.e("exception",ex.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateKYCrespose>call, Throwable t) {

                if (loader != null) {
                    if (loader.isShowing())
                        loader.dismiss();
                }
                Log.e("response", t.getMessage());
                // UtilMethods.INSTANCE.Error(context, context.getResources().getString(R.string.network_error));
            }
        });

    } catch (Exception e) {
        e.printStackTrace();
    }

}


    public void SiteList(final Context context, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        Log.e("newstartelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.SiteList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("newstartelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {

if(response.body().getStatuscode().equalsIgnoreCase("1")){

    UtilMethods.INSTANCE.setSiteTypeList(context,new Gson().toJson(response.body()).toString());



    FragmentActivityMessage
            fragmentActivityMessage =
            new FragmentActivityMessage("SiteList",""+new Gson().toJson(response.body()).toString());
    GlobalBus.getBus().post(fragmentActivityMessage);



}





                            } else {



                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }
    public void FeedBackCategoryList(final Context context, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        Log.e("FeedBackCategoryList", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.FeedBackCategoryList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("FeedBackCategoryListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {

if(response.body().getStatuscode().equalsIgnoreCase("1")){

    UtilMethods.INSTANCE.setFeedBackCategoryList(context,new Gson().toJson(response.body()).toString());



}





                            } else {



                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void SitetypeList(final Context context, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        Log.e("newstartelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.SitetypeList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("newstartelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {

if(response.body().getStatuscode().equalsIgnoreCase("1")){


    UtilMethods.INSTANCE.setSiteTList(context,new Gson().toJson(response.body()).toString());


    FragmentActivityMessage
            fragmentActivityMessage =
            new FragmentActivityMessage("SitetypeList",""+new Gson().toJson(response.body()).toString());
    GlobalBus.getBus().post(fragmentActivityMessage);



} else{

    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

}  } else {



                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void Constructionstage(final Context context, String id, final Loader loader, final Dialog dialog) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("sitetypeid",id);
        Log.e("newstartelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.Constructionstage(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("newstartelistres", "is : " + new Gson().toJson(response.body()).toString());
                    dialog.dismiss();

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {

if(response.body().getStatuscode().equalsIgnoreCase("1")){

    UtilMethods.INSTANCE.setConstruction(context, new Gson().toJson(response.body()).toString());


    FragmentActivityMessage
            fragmentActivityMessage =
            new FragmentActivityMessage("ConstructionStageList",""+new Gson().toJson(response.body()).toString());
    GlobalBus.getBus().post(fragmentActivityMessage);

}
else
{UtilMethods.INSTANCE.setConstruction(context, "");}
                            } else {
                                UtilMethods.INSTANCE.setConstruction(context, "");


                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void AgentTypeList(final Context context, final Loader loader) {



        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        Log.e("newstartelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.AgentTypeList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("newstartelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {

                                if(response.body().getStatuscode().equalsIgnoreCase("1")){

                                    UtilMethods.INSTANCE.setAgentTypeList(context, new Gson().toJson(response.body()).toString());
                                    UtilMethods.INSTANCE.setDistrictList(context, null);
                                    UtilMethods.INSTANCE.removeBlockList(context);
                                    /*FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("StateList",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);*/



                                }





                            } else {



                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void StateList(final Context context, final Loader loader) {



        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        Log.e("newstartelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.StateList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("newstartelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {

if(response.body().getStatuscode().equalsIgnoreCase("1")){

    UtilMethods.INSTANCE.setStateList(context, new Gson().toJson(response.body()).toString());
    UtilMethods.INSTANCE.setDistrictList(context, null);
    UtilMethods.INSTANCE.removeBlockList(context);
    FragmentActivityMessage
            fragmentActivityMessage =
            new FragmentActivityMessage("StateList",""+new Gson().toJson(response.body()).toString());
    GlobalBus.getBus().post(fragmentActivityMessage);



}





                            } else {



                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void DistrictList(final Context context, String StateId, final Loader loader,final Dialog dialog ) {

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("StateId",StateId);

        Log.e("DistrictList", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.DistrictList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("DistrictListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    UtilMethods.INSTANCE.setDistrictList(context, new Gson().toJson(response.body()).toString());
                                    UtilMethods.INSTANCE.removeBlockList(context);

                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("DistrictList",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                    dialog.dismiss();

                                }else{

                                    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void BlockList(final Context context, String DistrictId, final Loader loader,final Dialog dialog ) {

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("DistrictId",DistrictId);

        Log.e("BlockList", "startelist : " + jsonObj);
        UtilMethods.INSTANCE.setBlockList(context,"");

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.BlockList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("DistrictListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    UtilMethods.INSTANCE.setBlockList(context, new Gson().toJson(response.body()).toString());


                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("BlockList",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                    //dialog.dismiss();

                                }else{
                                    UtilMethods.INSTANCE.setBlockList(context,"");
                                    //UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void officeStateList(final Context context, final Loader loader) {

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        Log.e("newstartelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.StateList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("newstartelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                FragmentActivityMessage
                                        fragmentActivityMessage =
                                        new FragmentActivityMessage("officeStateList",""+new Gson().toJson(response.body()).toString());
                                GlobalBus.getBus().post(fragmentActivityMessage);


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void officeDistrictList(final Context context,String StateId, final Loader loader) {

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("StateId",StateId);

        Log.e("DistrictList", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.DistrictList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("DistrictListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                FragmentActivityMessage
                                        fragmentActivityMessage =
                                        new FragmentActivityMessage("officeDistrictList",""+new Gson().toJson(response.body()).toString());
                                GlobalBus.getBus().post(fragmentActivityMessage);


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }



    public void DealerList(final Context context, final Loader loader) {

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        Log.e("startelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.DealerList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                UtilMethods.INSTANCE.setDealerlist(context, new Gson().toJson(response.body()).toString());

                                FragmentActivityMessage
                                        fragmentActivityMessage =
                                        new FragmentActivityMessage("DealerListnew",""+new Gson().toJson(response.body()).toString());
                                GlobalBus.getBus().post(fragmentActivityMessage);


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void AgentList(final Context context, final Loader loader) {

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        Log.e("startelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.AgentList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                //UtilMethods.INSTANCE.setDealerlist(context, new Gson().toJson(response.body()).toString());

                                FragmentActivityMessage
                                        fragmentActivityMessage =
                                        new FragmentActivityMessage("AgentListnew",""+new Gson().toJson(response.body()).toString());
                                GlobalBus.getBus().post(fragmentActivityMessage);


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void BrandList(final Context context, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.BrandList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                FragmentActivityMessage
                                        fragmentActivityMessage =
                                        new FragmentActivityMessage("BrandList",""+new Gson().toJson(response.body()).toString());
                                GlobalBus.getBus().post(fragmentActivityMessage);


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void ProductCategoryList(final Context context, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.ProductCategoryList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {
                                UtilMethods.INSTANCE.setProductCategorylist(context, new Gson().toJson(response.body()).toString());

                                FragmentActivityMessage
                                        fragmentActivityMessage =
                                        new FragmentActivityMessage("BrandList",""+new Gson().toJson(response.body()).toString());
                                GlobalBus.getBus().post(fragmentActivityMessage);


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void DashBoardAgent(final Context context, final Loader loader, final LinearLayout LI_SUBMITPURCHASE, final LinearLayout product_liall,
                               final String val, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("startelist", " jsonObj  : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<DashBoardAgentApiRes> call = git.DashBoardAgent(jsonObj);
            call.enqueue(new Callback<DashBoardAgentApiRes>() {
                @Override
                public void onResponse(Call<DashBoardAgentApiRes> call, final retrofit2.Response<DashBoardAgentApiRes> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {
                                IsAppValid=Boolean.parseBoolean(response.body().getIsAppValid());
                                IsSessionValid=Boolean.parseBoolean(response.body().getIsSessionVaild());
                                IsVersionValid=Boolean.parseBoolean(response.body().getIsVersionValid());
                                if(!CheckAppDefault(context))
                                {
                                    return;
                                }
if(response.body().getStatuscode().equalsIgnoreCase("1")){

    UtilMethods.INSTANCE.setBalanceCheck(context,new Gson().toJson(response.body().getData()).toString());
    UtilMethods.INSTANCE.setDashBoardAgent(context,new Gson().toJson(response.body()).toString());

    FragmentActivityMessage
            fragmentActivityMessage =
            new FragmentActivityMessage("DashBoardAgent",""+new Gson().toJson(response.body()).toString());
    GlobalBus.getBus().post(fragmentActivityMessage);

    if(val.equalsIgnoreCase("1")){

        activity.finish();
        context.startActivity(new Intent(context, MainActivity.class));



    }

}else {


    LI_SUBMITPURCHASE.setVisibility(View.GONE);
    product_liall.setVisibility(View.GONE);


    UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);
}





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<DashBoardAgentApiRes> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }
    public void DashBoardSubAdmin(final Context context, final Loader loader, final LinearLayout LI_PENDINGSITES, final LinearLayout product_liall,
                                  final String val, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("DashBoardSubAdmin", " jsonObj  : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<DashBoardAgentApiRes> call = git.DashBoardSubAdmin(jsonObj);
            call.enqueue(new Callback<DashBoardAgentApiRes>() {
                @Override
                public void onResponse(Call<DashBoardAgentApiRes> call, final retrofit2.Response<DashBoardAgentApiRes> response) {
                    Log.e("DashBoardSubAdminres", "is : " + new Gson().toJson(response.body()).toString());
                    Log.e("DashAdminresrrrr", "is : " + response.body().getSiteList().size());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){

                                    UtilMethods.INSTANCE.setBalanceCheck(context,new Gson().toJson(response.body().getData()).toString());
                                    UtilMethods.INSTANCE.setDashBoardSubAdmin(context, new Gson().toJson(response.body()).toString());



                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("DashBoardSubAdmin",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                    if(val.equalsIgnoreCase("1")){

                                        context.startActivity(new Intent(context, MainActivity.class));
                                        activity.finish();



                                    }



                                }else {

                                    LI_PENDINGSITES.setVisibility(View.GONE);
                                    product_liall.setVisibility(View.GONE);
                                    UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);

                                }


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<DashBoardAgentApiRes> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }



    public void DashBoardDealer(final Context context, final Loader loader, final LinearLayout LI_PENDINGSITES,final LinearLayout LI_SaleLIst,
                                final LinearLayout product_liall, final String val,final  Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("DashBoardDealer", " jsonObj  : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<DashBoardAgentApiRes> call = git.DashBoardDealer(jsonObj);
            call.enqueue(new Callback<DashBoardAgentApiRes>() {
                @Override
                public void onResponse(Call<DashBoardAgentApiRes> call, final retrofit2.Response<DashBoardAgentApiRes> response) {
                    Log.e("DashBoardDealerres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){

                                     UtilMethods.INSTANCE.setDashBoardDealer(context,new Gson().toJson(response.body()).toString());

                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("DashBoardDealer",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);




                                    if(response.body().getPurchaseList().size()==0){

                                        //LI_PENDINGSITES.setVisibility(View.GONE);

                                    }
                                    else if(response.body().getSaleList().size()==0)
                                    {
                                        //LI_SaleLIst.setVisibility(View.GONE);
                                    }



                                    if(val.equalsIgnoreCase("1")){



                                       context.startActivity(new Intent(context, MainActivity.class));

                                        activity.finish();


                                    }


                                }else {


                                    LI_PENDINGSITES.setVisibility(View.GONE);
                                    product_liall.setVisibility(View.GONE);

                                    UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);

                                }







                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<DashBoardAgentApiRes> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void setBalanceCheck(Context context, String balance) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.balancePref, balance);
        editor.commit();

    }

 public void setPointStatusReport(Context context, String setPointStatusReport) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setPointStatusReport, setPointStatusReport);
        editor.commit();

    }


  public void setDashBoardSubAdmin(Context context, String setDashBoardSubAdmin) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setDashBoardSubAdmin, setDashBoardSubAdmin);
        editor.commit();


    }
    public void setAgentTypeList(Context context, String setAgentTypeList) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setAgentTypeList, setAgentTypeList);
        editor.commit();
    }
    public void setProfile(Context context, String setProfile) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setProfile, setProfile);
        editor.commit();


    }
    public void  setProfileDetails(Context context, String setProfile) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setProfileDetails, setProfile);
        editor.commit();
    }
    public void setStateList(Context context, String setStateList) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setStateList, setStateList);
        editor.commit();


    }
 public void setDistrictList(Context context, String setDistrictList) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setDistrictList, setDistrictList);
        editor.commit();


    }
public  void setBlockList(Context context, String setBlockList){
        SharedPreferences sp=context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(ApplicationConstant.INSTANCE.setBlockList,setBlockList);
        editor.commit();
}
public void removeBlockList(Context context)
{
    SharedPreferences sp=context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref,context.MODE_PRIVATE);
    SharedPreferences.Editor editor=sp.edit();
    editor.remove(ApplicationConstant.INSTANCE.setBlockList);
    editor.commit();
}
public void setDealerlist(Context context, String setDealer) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setDealer, setDealer);
        editor.commit();

    }
    public void setProductCategorylist(Context context, String setProductCategory) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setProductCategory, setProductCategory);
        editor.commit();

    }





public void setFeedBackCategoryList(Context context, String setFeedBackCategoryList) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setFeedBackCategoryList, setFeedBackCategoryList);
        editor.commit();

    }

    public void setSiteTypeList(Context context, String setSiteTypeList) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setSitetypeList, setSiteTypeList);
        editor.commit();

    }
    public void setSiteTList(Context context, String setSiteTypeList) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setSiteTList, setSiteTypeList);
        editor.commit();

    }

public void setConstruction(Context context, String setConstruction) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setConstruction, setConstruction);
        editor.commit();


    }


  public void setDashBoardDealer(Context context, String setDashBoardDealer) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setDashBoardDealer, setDashBoardDealer);
        editor.commit();


    }




  public void setDashBoardAgent(Context context, String setDashBoardAgent) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.setDashBoardAgent, setDashBoardAgent);
        editor.commit();


    }



    public void ProductList(final Context context, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);




        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.ProductList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                FragmentActivityMessage
                                        fragmentActivityMessage =
                                        new FragmentActivityMessage("ProductList",""+new Gson().toJson(response.body()).toString());
                                GlobalBus.getBus().post(fragmentActivityMessage);


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void ProductByCategoryList(final Context context,final String CategoryID, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("CategoryID",CategoryID);



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.ProductbyCategoryList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                FragmentActivityMessage
                                        fragmentActivityMessage =
                                        new FragmentActivityMessage("ProductList",""+new Gson().toJson(response.body()).toString());
                                GlobalBus.getBus().post(fragmentActivityMessage);


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void RequestTechReport(final Context context, final Loader loader, final ScrollView scrrolview, final ImageView image) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.RequestTechReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {

                                if(response.body().getStatuscode().equalsIgnoreCase("1")){

                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("RequestTechReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);


                                }else {



                                    scrrolview.setVisibility(View.GONE);
                                    image.setVisibility(View.VISIBLE);


                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void RedemptionReport(final Context context, final Loader loader, final ScrollView horizantal, final ImageView imageView) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.RedemptionReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("RedemptionReport", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){



                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("detaredemption",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);


                                }else {
                                    imageView.setVisibility(View.VISIBLE);
                                    horizantal.setVisibility(View.GONE);

                                   // UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }






                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }



 public void PurchaseAgentReport(final Context context,String SiteID, final Loader loader,final HorizontalScrollView horizontalScrollView,final ImageView imageView) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("SiteID",SiteID);

        Log.e("PurchaseAgentReport","  jsonObj  :  "+ jsonObj  );



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.PurchaseAgentReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("PurchaseAgentReportres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {



                                if(response.body().getStatuscode().equalsIgnoreCase("1")){



                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("PurchaseAgentReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                }else {

                                    //UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                                    horizontalScrollView.setVisibility(View.GONE);
                                    imageView.setVisibility(View.VISIBLE);
                                }









                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

public void PurchaseDealerReport(final Context context,String ProductID,String Status,String AgentID, final Loader loader, final HorizontalScrollView horizantal, final ImageView image) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("ProductID",ProductID);
        jsonObj.addProperty("Status",Status);
        jsonObj.addProperty("AgentID",AgentID);

        Log.e("PurchaseDealerReport","  jsonObj  :  "+ jsonObj  );

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.PurchaseDealerReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("PurchaseDealerReportres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {



                                if(response.body().getStatuscode().equalsIgnoreCase("1")){



                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("PurchaseDealerReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                }else {

                                    horizantal.setVisibility(View.GONE);
                                    image.setVisibility(View.VISIBLE);

                                   // UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }









                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }
public void SaleDealerReport(final Context context,String ProductID, final Loader loader, final HorizontalScrollView horizantal, final ImageView image) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("ProductID",ProductID);


        Log.e("PurchaseDealerReport","  jsonObj  :  "+ jsonObj  );

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.SaleDealerReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("SaleDealerReportres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {



                                if(response.body().getStatuscode().equalsIgnoreCase("1")){



                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("SaleDealerReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                }else {

                                    horizantal.setVisibility(View.GONE);
                                    image.setVisibility(View.VISIBLE);

                                    // UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }









                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

public void AgentReport(final Context context, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("PurchaseDealerReport","  jsonObj  :  "+ jsonObj  );

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.AgentReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("PurchaseDealerReportres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {



                                if(response.body().getStatuscode().equalsIgnoreCase("1")){



                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("AgentReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                }else {

                                    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }









                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


 public void EarnPointReport(final Context context, final Loader loader, final ScrollView horizantal, final ImageView image) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.EarnPointReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {

                                if(response.body().getStatuscode().equalsIgnoreCase("1")){



                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("EarnPointReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                }else {

                                    horizantal.setVisibility(View.GONE);
                                    image.setVisibility(View.VISIBLE);

                                    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }



 public void DealerReport(final Context context, final Loader loader, final HorizontalScrollView horizantal, final ImageView image) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.DealerReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){



                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("DealerReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                }else {

                                    horizantal.setVisibility(View.GONE);
                                    image.setVisibility(View.VISIBLE);

                                   // UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }







                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


 public void SiteReport(final Context context, final Loader loader) {
//, final ScrollView horizantal, final ImageView image
        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("SiteReport",""+jsonObj);



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.SiteReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("SiteReportres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){

                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("SiteReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                }else {

                                    //horizantal.setVisibility(View.GONE);
                                    //image.setVisibility(View.VISIBLE);

                                  //  UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }







                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

 public void MapReport(final Context context,String DistrictID, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("DistrictID",DistrictID);

        Log.e("MapReport","  jsonObj  :  "+ jsonObj  );



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<MapResponse> call = git.MapReport(jsonObj);
            call.enqueue(new Callback<MapResponse>() {
                @Override
                public void onResponse(Call<MapResponse> call, final retrofit2.Response<MapResponse> response) {
                    Log.e("MapReportres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {



                                if(response.body().getStatus().equalsIgnoreCase("1")){
                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("MapReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                }else {

                                    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }









                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("maplistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<MapResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("maplistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

 public void SubAdminSitesReport(final Context context, final Loader loader) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("SiteReport",""+jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.SubAdminSitesReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("SiteReportres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){

                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("SiteReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);

                                }else {

                                    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }







                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void SubmitNewPurchase(final Context context, String districtID,String blockID, String siteTypeID, String consStageID, String dealerID, String ownerName,
                                  String mobile, String address, String startDate, String brandID, String bagRequired, String purchased,
                                  String productID,String latitude,String longitude,String otherProduct,String otherDealer, final Loader loader, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("districtID",districtID);
        jsonObj.addProperty("BlockID",blockID);
        jsonObj.addProperty("siteTypeID",siteTypeID);
        jsonObj.addProperty("consStageID",consStageID==""?"0":consStageID);
        jsonObj.addProperty("dealerID",dealerID);
        jsonObj.addProperty("ownerName",ownerName);
        jsonObj.addProperty("mobile",mobile);
        jsonObj.addProperty("address",address);
        jsonObj.addProperty("startDate",startDate);
        jsonObj.addProperty("brandID",brandID);
        jsonObj.addProperty("bagRequired",bagRequired==""?"0":bagRequired);
        jsonObj.addProperty("purchased",purchased==""?"0":purchased);
        jsonObj.addProperty("productID",productID==""?"0":productID);
        jsonObj.addProperty("latAgent",latitude);
        jsonObj.addProperty("longAgent",longitude);
        jsonObj.addProperty("otherDealer",otherDealer);
        jsonObj.addProperty("otherProduct",otherProduct);

        Log.e("SubmitNewPurchase","SubmitNewPurchase :  "+ jsonObj );



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.SubmitNewPurchase(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("SubmitNewPurchaseres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body().getStatuscode()!=null && response.body().getStatuscode().equalsIgnoreCase("1")) {

                                UtilMethods.INSTANCE.Successful(context, response.body().getMsg(),2,activity);


                            } else {
                                if (loader != null) {
                                    if (loader.isShowing())
                                        loader.dismiss();
                                }
                                UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void SubmitExistingPurchase(final Context context, String consStageID, String dealerID, String purchased,
                                       String productID, String siteID, final Loader loader, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("siteID",siteID);
        jsonObj.addProperty("consStageID",consStageID);
        jsonObj.addProperty("dealerID",dealerID);
        jsonObj.addProperty("purchased",purchased);
        jsonObj.addProperty("productID",productID);


        Log.e("SubmitNewPurchase","SubmitNewPurchase :  "+ jsonObj );



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.SubmitExistingPurchase(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("SubmitNewPurchaseres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body().getStatuscode()!=null && response.body().getStatuscode().equalsIgnoreCase("1")) {

                                UtilMethods.INSTANCE.Successful(context, response.body().getMsg(),2,activity);


                            } else {

                                UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void UpdateDealerStatus(final Context context,String ID, String Status, String Remark, final Loader loader, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("PurchaseStatus",Status);
        jsonObj.addProperty("PurchaseRemark",Remark);
        jsonObj.addProperty("ID",ID);


        Log.e("UpdateDealerStatus","UpdateDealerStatus :  "+ jsonObj );



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.UpdateDealerStatus(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("UpdateDealerStatus", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body().getStatuscode()!=null && response.body().getStatuscode().equalsIgnoreCase("1")) {

                                UtilMethods.INSTANCE.Successful(context, response.body().getMsg(),3,activity);


                            } else {

                                UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void SubmitDealerPurchase(final Context context,  String purchased,
                                       String productID, String PurchaseDate, final Loader loader, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("PurchaseDate",PurchaseDate);
        jsonObj.addProperty("purchased",purchased);
        jsonObj.addProperty("productID",productID);


        Log.e("SubmitDealerPurchase","SubmitDealerPurchase :  "+ jsonObj );



        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.SubmitDealerPurchase(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("SubmitDealerPurchase", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body().getStatuscode()!=null && response.body().getStatuscode().equalsIgnoreCase("1")) {

                                UtilMethods.INSTANCE.Successful(context, response.body().getMsg(),3,activity);


                            } else {

                                UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

 public void SiteVarification(final Context context, String siteID,double latval,double Longval,String encodedString,String SubAdminRemark,String active,final Loader loader, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("SiteID",siteID);
        jsonObj.addProperty("Lat",latval);
        jsonObj.addProperty("Long",Longval);
        jsonObj.addProperty("SubAdminStatus",active);
        jsonObj.addProperty("SubAdminRemark",SubAdminRemark);
        jsonObj.addProperty("Photo",encodedString);

        Log.e("SubmitNewPurchase","SubmitNewPurchase :  "+ jsonObj );

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.SiteVarification(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("SubmitNewPurchaseres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body().getStatuscode()!=null && response.body().getStatuscode().equalsIgnoreCase("1")) {

                                UtilMethods.INSTANCE.Successful(context, response.body().getMsg(),3,activity);


                            } else {

                                UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void SiteDetails(final Context context, String siteID,final Loader loader, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("SiteID",siteID);


        Log.e("SubmitNewPurchase","SubmitNewPurchase :  "+ jsonObj );

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<SiteResponse> call = git.SiteDetails(jsonObj);
            call.enqueue(new Callback<SiteResponse>() {
                @Override
                public void onResponse(Call<SiteResponse> call, final retrofit2.Response<SiteResponse> response) {
                    Log.e("SiteDetails", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body().getStatus()!=null && response.body().getStatus().equalsIgnoreCase("1")) {
                                FragmentActivityMessage
                                        fragmentActivityMessage =
                                        new FragmentActivityMessage("SiteDetails",""+new Gson().toJson(response.body()).toString());
                                GlobalBus.getBus().post(fragmentActivityMessage);
                                //UtilMethods.INSTANCE.Successful(context, response.body().getMsg(),2,activity);


                            } else {

                                UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);
                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<SiteResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void VideosList(final Context context, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("VideosList", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.Videos(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("VideosListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response",""+new Gson().toJson(response.body()).toString());
                                    intent.putExtra("type","Videos");
                                    context.startActivity(intent);


                                }else {
                                    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void Sendwish(final Context context, String Wish, final Loader loader ) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("Wish",Wish);

        Log.e("VideosList", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.Sendwish(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("VideosListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    UtilMethods.INSTANCE.Successful(context,response.body().getMsg(),0,null);

                                }else {

                                     UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void FeedbackReport(final Context context, final Loader loader, final RecyclerView scrrolview, final ImageView image) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("FeedbackReport", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.FeedbackReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("FeedbackReportres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){



                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("FeedbackReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);


                                }else {

                                    scrrolview.setVisibility(View.GONE);
                                    image.setVisibility(View.VISIBLE);
                                   //  UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void WishReport(final Context context, final Loader loader, final RecyclerView scrrolview, final ImageView image) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("WishReport", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.WishReport(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("WishReportres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){



                                    FragmentActivityMessage
                                            fragmentActivityMessage =
                                            new FragmentActivityMessage("WishReport",""+new Gson().toJson(response.body()).toString());
                                    GlobalBus.getBus().post(fragmentActivityMessage);


                                }else {

                                    scrrolview.setVisibility(View.GONE);
                                    image.setVisibility(View.VISIBLE);

                                    // UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

 public void FeedBack(final Context context, String Wish,String id, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("Description",Wish);
        jsonObj.addProperty("CategoryID",id);

        Log.e("VideosList", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.FeedBack(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("VideosListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    UtilMethods.INSTANCE.Successful(context,response.body().getMsg(),0,null);

                                }else {

                                     UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void RewardRedeem(final Context context, String rewardID,String quantity,String districtID,String city,String address,String pinCode,
                             final Loader loader, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("rewardID",rewardID);
        jsonObj.addProperty("quantity",quantity);
        jsonObj.addProperty("districtID",0);
        jsonObj.addProperty("city",city);
        jsonObj.addProperty("address",address);
        jsonObj.addProperty("pinCode",pinCode);

        Log.e("VideosList", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.RewardRedeem(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("VideosListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){

                                    UtilMethods.INSTANCE.Successful(context,response.body().getMsg(),2,activity);

                                }else {

                                     UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }

                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void GenerateOTPChangePassword(final Context context, final String MobileNo , final Loader loader, final Dialog dialog, final String val) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("MobileNo",MobileNo);

        Log.e("VideosList", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.GenerateOTPChangePassword(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("VideosListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatus().equalsIgnoreCase("1")){
                                    dialog.dismiss();

                                   if(val.equalsIgnoreCase("1")){

                                       LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                       View vv = inflater.inflate(R.layout.otp_new_layout, null);

                                       final LinearLayout otpTextLayoutnumber = (LinearLayout) vv.findViewById(R.id.otpTextLayoutnumber);
                                       final AppCompatTextView title = (AppCompatTextView) vv.findViewById(R.id.title);
                                       final AppCompatTextView message = (AppCompatTextView) vv.findViewById(R.id.message);
                                       final EditText newnumberuser = (EditText) vv.findViewById(R.id.newnumberuser);
                                       final EditText numberuser = (EditText) vv.findViewById(R.id.numberuser);
                                       final AppCompatButton okButton = (AppCompatButton) vv.findViewById(R.id.okButton);
                                       final AppCompatButton cancelButton = (AppCompatButton) vv.findViewById(R.id.cancelButton);
                                       final Dialog dialog = new Dialog(context);

                                       dialog.setCancelable(false);
                                       dialog.setContentView(vv);

                                       otpTextLayoutnumber.setVisibility(View.VISIBLE);

                                      // numberuser.setText(""+MobileNo);
                                       message.setText("Otp Send on Mobile Number");

                                       cancelButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               dialog.dismiss();
                                           }
                                       });

                                       okButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                                                   loader.show();
                                                   loader.setCancelable(false);
                                                   loader.setCanceledOnTouchOutside(false);

                                                   UtilMethods.INSTANCE.Changemobile (context, newnumberuser.getText().toString().trim(), MobileNo.trim(),
                                                           loader,dialog);



                                                //   UtilMethods.INSTANCE.GenerateOTPChangePassword(context, ""+newnumberuser,loader,dialog,"1");

                                               } else {
                                                   UtilMethods.INSTANCE.NetworkError(context, context.getResources().getString(R.string.network_error_title),
                                                           context.getResources().getString(R.string.network_error_message));
                                               }

                                           }
                                       });
                                       dialog.show();

                                   }
                                   else {


                                       context.startActivity(new Intent(context, ChangePasswordActivity.class));


                                   }



                                }else {

                                     UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }

                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void RequestTechExpress(final Context context, String Name,String Time  ,String Date ,String MobileNo , final Loader loader, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("Date",Date);
        jsonObj.addProperty("Time",Time);
        jsonObj.addProperty("Name",Name);
        jsonObj.addProperty("Mobile",MobileNo);

        Log.e("VideosList", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.RequestTechExpress(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("VideosListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){



                                     UtilMethods.INSTANCE.Successful(context,response.body().getMsg(),0,activity);

                                }else {

                                     UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }

                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }



    public void changePassword(final Context context, String OTP,String Password1, final Loader loader, final Activity activity) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();
        String MobileNo = balanceCheckResponse.getMobileNo();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("Password1",Password1);
        jsonObj.addProperty("Password2",Password1);
        jsonObj.addProperty("OTP",OTP);
        jsonObj.addProperty("MobileNo",MobileNo);

        Log.e("VideosList", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.ChangePassword(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("VideosListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatus().equalsIgnoreCase("1")){



                                     UtilMethods.INSTANCE.Successful(context,response.body().getMsg(),2,activity);

                                }else {

                                     UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }

                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void Changemobile (final Context context, String OTP, String Password1, final Loader loader, final Dialog dialog ) {

        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();
        String MobileNo = balanceCheckResponse.getMobileNo();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("Password1",Password1);
        jsonObj.addProperty("Password2",Password1);
        jsonObj.addProperty("OTP",OTP);
        jsonObj.addProperty("MobileNo",MobileNo);

        Log.e("VideosList", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.Changemobile(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("VideosListres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatus().equalsIgnoreCase("1")){
                                    dialog.dismiss();


                                     UtilMethods.INSTANCE.Successful(context,response.body().getMsg(),0,null);

                                }else {

                                     UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }

                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }



    public void Notificationlist(final Context context, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();
        String UserRole = balanceCheckResponse.getUserRole();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);
        jsonObj.addProperty("SerialNo","");
        jsonObj.addProperty("RoleID",UserRole);

        Log.e("Notificationlist", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.Notificationlist(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("Notificationlistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response",""+new Gson().toJson(response.body()).toString());
                                    intent.putExtra("type","Notification");
                                    context.startActivity(intent);


                                }else {

                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response","Notificationerror");
                                    intent.putExtra("type","Notification");
                                    context.startActivity(intent);

                                 }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void WishList(final Context context, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("Events", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.WishList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("Eventsres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response",""+new Gson().toJson(response.body()).toString());
                                    intent.putExtra("type","Wishes");
                                    context.startActivity(intent);




                                }else {

                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response","Notificationerror");
                                    intent.putExtra("type","Wishes.");
                                    context.startActivity(intent);

                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void RewardList(final Context context, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("Events", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.RewardList(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("Eventsres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    Intent intent = new Intent(context, RewardListActivity.class);
                                    intent.putExtra("response",""+new Gson().toJson(response.body()).toString());
                                    intent.putExtra("type","Reward");
                                    context.startActivity(intent);


                                }else {

                                    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void EventsList(final Context context, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("Events", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.Events(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("Eventsres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response",""+new Gson().toJson(response.body()).toString());
                                    intent.putExtra("type","Events");
                                    context.startActivity(intent);




                                }else {


                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response","Notificationerror");
                                    intent.putExtra("type","Events.");
                                    context.startActivity(intent);

                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }
    public void KnowMore(final Context context, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("Events", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.KnowMore(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("Eventsres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response",""+new Gson().toJson(response.body()).toString());
                                    intent.putExtra("type","Know More");
                                    context.startActivity(intent);



                                }else {


                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response","Notificationerror");
                                    intent.putExtra("type","Know More.");
                                    context.startActivity(intent);


                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void FAQ(final Context context, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("Events", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.FAQ(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("Eventsres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response",""+new Gson().toJson(response.body()).toString());
                                    intent.putExtra("type","FAQ");
                                    context.startActivity(intent);




                                }else {

                                    Intent intent = new Intent(context, VideosListActivity.class);
                                    intent.putExtra("response","Notificationerror");
                                    intent.putExtra("type","FAQ.");
                                    context.startActivity(intent);
                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }


    public void TermsCondition(final Context context, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("Events", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<TermsConditionResponse> call = git.TermsCondition(jsonObj);
            call.enqueue(new Callback<TermsConditionResponse>() {
                @Override
                public void onResponse(Call<TermsConditionResponse> call, final retrofit2.Response<TermsConditionResponse> response) {
                    Log.e("Eventsres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    Intent intent = new Intent(context, TermsconditionActivity.class);
                                    intent.putExtra("description",""+ response.body().getData().getDescription() );
                                    intent.putExtra("name",""+ response.body().getData().getName());
                                     context.startActivity(intent);


                                }else {

                                    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<TermsConditionResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void ContactUS(final Context context, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("Events", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<TermsConditionResponse> call = git.ContactUS(jsonObj);
            call.enqueue(new Callback<TermsConditionResponse>() {
                @Override
                public void onResponse(Call<TermsConditionResponse> call, final retrofit2.Response<TermsConditionResponse> response) {
                    Log.e("Eventsres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    Intent intent = new Intent(context, TermsconditionActivity.class);
                                    intent.putExtra("description",""+ response.body().getData().getDescription() );
                                    intent.putExtra("name",""+ response.body().getData().getName());
                                     context.startActivity(intent);


                                }else {

                                    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<TermsConditionResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void PrivacyPolicy(final Context context, final Loader loader) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String Sessiondeta = balanceCheckResponse.getSession();
        String userid = balanceCheckResponse.getUserID();


        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("session",Sessiondeta);
        jsonObj.addProperty("UserID",userid);

        Log.e("Events", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<TermsConditionResponse> call = git.PrivacyPolicy(jsonObj);
            call.enqueue(new Callback<TermsConditionResponse>() {
                @Override
                public void onResponse(Call<TermsConditionResponse> call, final retrofit2.Response<TermsConditionResponse> response) {
                    Log.e("Eventsres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatuscode().equalsIgnoreCase("1")){


                                    Intent intent = new Intent(context, TermsconditionActivity.class);
                                    intent.putExtra("description",""+ response.body().getData().getDescription() );
                                    intent.putExtra("name",""+ response.body().getData().getName());
                                     context.startActivity(intent);


                                }else {

                                    UtilMethods.INSTANCE.Failed(context, response.body().getMsg(),0);

                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<TermsConditionResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void LoginWithPassword(final Context context, String MobileNo, String  Password, final Loader loader, final Activity activity) {


        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
          String tokendeta = ""+myPreferences.getString(ApplicationConstant.INSTANCE.token, null);

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("MobileNo",MobileNo);
        jsonObj.addProperty("Password",Password );
        jsonObj.addProperty("Token",tokendeta );

        Log.e("startelist", "startelist : " + jsonObj);

        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.LoginWithPassword(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatus().equalsIgnoreCase("1")){


                                    String UserCode= response.body().getUserRole();

                                    Log.e("startelistres", "is : " + new Gson().toJson(response.body().getUserCode()).toString() +"  UserCode  "+ UserCode);


                                    UtilMethods.INSTANCE.setLoginrespose(context, new Gson().toJson(response.body()).toString(),"1");

                                    UtilMethods.INSTANCE.UserDetails(context,loader);

                                    if(UserCode.equalsIgnoreCase("3")){


                                        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                                            loader.show();
                                            loader.setCancelable(false);
                                            loader.setCanceledOnTouchOutside(false);

                                            UtilMethods.INSTANCE.DashBoardSubAdmin(context, loader,null,null,"1",activity);

                                        } else {
                                            UtilMethods.INSTANCE.NetworkError(context, context.getResources().getString(R.string.network_error_title),
                                                    context.getResources().getString(R.string.network_error_message));


                                        }




                                    }
                                    else  if(UserCode.equalsIgnoreCase("4")){



                                        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                                            loader.show();
                                            loader.setCancelable(false);
                                            loader.setCanceledOnTouchOutside(false);

                                            UtilMethods.INSTANCE.DashBoardDealer(context, loader,null,null,null,"1",activity);

                                        } else {
                                            UtilMethods.INSTANCE.NetworkError(context, context.getResources().getString(R.string.network_error_title),
                                                    context.getResources().getString(R.string.network_error_message));


                                        }




                                    }
                                    else  if(UserCode.equalsIgnoreCase("5")) {
//  Agent


                                        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                                            loader.show();
                                            loader.setCancelable(false);
                                            loader.setCanceledOnTouchOutside(false);

                                            UtilMethods.INSTANCE.DashBoardAgent(context, loader, null, null,"1",activity);

                                        } else {
                                            UtilMethods.INSTANCE.NetworkError(context, context.getResources().getString(R.string.network_error_title),
                                                    context.getResources().getString(R.string.network_error_message));
                                        }


                                    }



                                        //  context.startActivity(new Intent(context, MainActivity.class));

                                  //  activity.finish();


                                }else {


                                    UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void LoginWithOTP(final Context context, String MobileNo, String  Password, final Loader loader, final Dialog dialog, final Activity activity) {
        SharedPreferences myPreferences = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String tokendeta = ""+myPreferences.getString(ApplicationConstant.INSTANCE.token, null);
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("MobileNo",MobileNo);
        jsonObj.addProperty("Password",Password );
        jsonObj.addProperty("Token",tokendeta );

        Log.e("startelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.LoginWithOTP(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatus().equalsIgnoreCase("1")){

                                    dialog.dismiss();
                                    String UserCode= response.body().getUserRole();
                                    UtilMethods.INSTANCE.setLoginrespose(context, new Gson().toJson(response.body()).toString(),"1");
                                    UtilMethods.INSTANCE.UserDetails(context,loader);
                                    if(UserCode.equalsIgnoreCase("3")){


                                        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                                            loader.show();
                                            loader.setCancelable(false);
                                            loader.setCanceledOnTouchOutside(false);

                                            UtilMethods.INSTANCE.DashBoardSubAdmin(context, loader,null,null,"1",activity);

                                        } else {
                                            UtilMethods.INSTANCE.NetworkError(context, context.getResources().getString(R.string.network_error_title),
                                                    context.getResources().getString(R.string.network_error_message));


                                        }




                                    }
                                    else  if(UserCode.equalsIgnoreCase("4")){



                                        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                                            loader.show();
                                            loader.setCancelable(false);
                                            loader.setCanceledOnTouchOutside(false);

                                            UtilMethods.INSTANCE.DashBoardDealer(context, loader,null,null,null,"1",activity);

                                        } else {
                                            UtilMethods.INSTANCE.NetworkError(context, context.getResources().getString(R.string.network_error_title),
                                                    context.getResources().getString(R.string.network_error_message));


                                        }




                                    }
                                    else  if(UserCode.equalsIgnoreCase("5")) {
//  Agent


                                        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                                            loader.show();
                                            loader.setCancelable(false);
                                            loader.setCanceledOnTouchOutside(false);

                                            UtilMethods.INSTANCE.DashBoardAgent(context, loader, null, null,"1",activity);

                                        } else {
                                            UtilMethods.INSTANCE.NetworkError(context, context.getResources().getString(R.string.network_error_title),
                                                    context.getResources().getString(R.string.network_error_message));
                                        }


                                    }


                                }else {

                                    UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);
                                }

                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void GenerateOTP(final Context context, final String MobileNo, final Loader loader, final Activity activity) {

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("APPID", ApplicationConstant.INSTANCE.APPID);
        jsonObj.addProperty("IMEI", UtilMethods.INSTANCE.getDeviceId(context));
        jsonObj.addProperty("RegKey", "Online");
        jsonObj.addProperty("Version",BuildConfig.VERSION_NAME );
        jsonObj.addProperty("MobileNo",MobileNo);
        jsonObj.addProperty("Password","" );
        Log.e("startelist", "startelist : " + jsonObj);


        try {
            EndPointInterface git = ApiClient.getClient().create(EndPointInterface.class);
            Call<RegisterResponse> call = git.GenerateOTP(jsonObj);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, final retrofit2.Response<RegisterResponse> response) {
                    Log.e("startelistres", "is : " + new Gson().toJson(response.body()).toString());

                    if (response != null) {
                        if (loader != null) {
                            if (loader.isShowing())
                                loader.dismiss();
                        }
                        try {
                            if (response.body() != null) {


                                if(response.body().getStatus().equalsIgnoreCase("1")){

                         /////////////////////////DIalog Show///////////////////


                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View view = inflater.inflate(R.layout.otppopup, null);

                                    edMobileFwp = (EditText) view.findViewById(R.id.ed_mobile_fwp);
                                    tilMobileFwp=(TextInputLayout)view.findViewById(R.id.til_mobile_fwp);
                                    FwdokButton = (Button) view.findViewById(R.id.okButton);
                                    cancelButton = (Button) view.findViewById(R.id.cancelButton);

                                    final Dialog dialog = new Dialog(context);

                                    dialog.setCancelable(false);
                                    dialog.setContentView(view);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



                                    cancelButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    FwdokButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {



                                            if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {

                                                loader.show();
                                                loader.setCancelable(false);
                                                loader.setCanceledOnTouchOutside(false);


                                                UtilMethods.INSTANCE.LoginWithOTP(context,MobileNo, edMobileFwp.getText().toString().trim(), loader,dialog,activity);


                                            } else {
                                                UtilMethods.INSTANCE.NetworkError(context, context.getResources().getString(R.string.network_error_title),
                                                        context.getResources().getString(R.string.network_error_message));
                                            }

                                            // dialog.dismiss();
                                        }
                                    });

                                    dialog.show();




//////////////////dialog end ////////////////

                                }else {


                                    UtilMethods.INSTANCE.Failed(context,response.body().getMsg(),0);


                                }





                            } else {

                            }
                        } catch (Exception ex) {
                            Log.e("startelistexception", ex.getMessage());
                            if (loader != null) {
                                if (loader.isShowing())
                                    loader.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    if (loader != null) {
                        if (loader.isShowing())
                            loader.dismiss();
                    }
                    Log.e("startelistonFailure", t.getMessage());
                }
            });
        } catch (Exception ex) {

        }
    }

    public void logout(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        UtilMethods.INSTANCE.setLoginrespose(context, "", "");
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        Intent startIntent = new Intent(context, Splash.class);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(startIntent);

    }

    public void Getremaning(final Context context, String remaining) {

        SharedPreferences prefs = context.getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ApplicationConstant.INSTANCE.remaining, remaining);
        editor.commit();


    }
}