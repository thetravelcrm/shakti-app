package com.Shakti.Shakti.Login.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.Shakti.Shakti.Dashbord.ui.MainActivity;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.ui.RegisterActivity;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public Button btLogin,bt_Register;
    public Button FwdokButton;
    public Button cancelButton;
    public EditText tilMobile;
    public EditText tilPass;
    public TextInputLayout tilMobileFwp,textLayoutMobile,textLayoutPassword;
    CircleImageView imgProfilepic;
    public AutoCompleteTextView edMobile;
    public EditText edPass;
    public  EditText  edMobileFwp;
    public TextView forgotpass;

    Loader loader;
    String[] recent;
    String[] recentNumber;
    CheckBox rememberCheck;
    private static CountDownTimer countDownTimer;
    LinearLayout agentregistrer,lIMobile,LIpassword,LI_btnLogin,LI_btnRegister,LI_btnRadio;
    RadioButton rb_havepassword,rb_otp;
    RadioGroup rg;

    String typevalue="1";
    LinearLayout passwordlin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        getId();

    }

    public void getId() {

        passwordlin=findViewById(R.id.passwordlin);
        rg=findViewById(R.id.rg);
        rb_otp=findViewById(R.id.rb_otp);
        rb_havepassword=findViewById(R.id.rb_havepassword);

        rb_havepassword.setOnClickListener(this);
        rb_otp.setOnClickListener(this);


        agentregistrer=findViewById(R.id.agentregistrer);
        agentregistrer.setOnClickListener(this);

        loader = new Loader(this,android.R.style.Theme_Translucent_NoTitleBar);
        btLogin=(Button)findViewById(R.id.bt_login);
        bt_Register=findViewById(R.id.bt_Register);
        bt_Register.setOnClickListener(this);
        tilMobile=findViewById(R.id.til_mobile);
        tilPass=findViewById(R.id.til_pass);
        rememberCheck = (CheckBox) findViewById(R.id.check_pass);
        textLayoutMobile=findViewById(R.id.textLayoutMobile);
        textLayoutPassword=findViewById(R.id.textLayoutPassword);
        edMobile=(AutoCompleteTextView)findViewById(R.id.mobilenumber);
        edPass=(EditText)findViewById(R.id.password);
        LIpassword=findViewById(R.id.LIpassword);
        lIMobile=findViewById(R.id.lIMobile);
        LI_btnLogin=findViewById(R.id.LI_btnLogin);
        LI_btnRegister=findViewById(R.id.LI_btnRegister);
        LI_btnRadio=findViewById(R.id.LI_btnRadio);
        forgotpass=(TextView) findViewById(R.id.tv_forgotpass);
        imgProfilepic=findViewById(R.id.imgProfilepic);
        edMobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < (recent.length - 1); i++) {
                    if(edMobile.getText().toString().equalsIgnoreCase(recent[i])){
                        edPass.setText(recent[i + 1]);
                    }
                }
            }
        });

        setListners();
        RecentNumbers();


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    if(checkedRadioButton.getText().equals("Send OTP"))
                    {

                        btLogin.setText("Send OTP");
                        typevalue="2";
                        rb_otp.setChecked(true);
                        rb_havepassword.setChecked(false);
                        passwordlin.setVisibility(View.GONE);


                    }
                    else
                    {
                        btLogin.setText("Login");

                        typevalue="1";
                        rb_otp.setChecked(false);
                        rb_havepassword.setChecked(true);
                        passwordlin.setVisibility(View.VISIBLE);
                    }
                    // Changes the textview's text to "Checked: example radiobutton text"
                    Log.d("checked","Checked:" + checkedRadioButton.getText());
                }
            }
        });


        SetLayoutWidth();


    }

    public void RecentNumbers() {
        String abcd = UtilMethods.INSTANCE.getRecentLogin(this);
        if (abcd != null && abcd.length() > 0) {
            recent = abcd.split(",");
            recentNumber = new String[recent.length / 2];
            for (int i = 0; i < (recent.length - 1); i++) {
                if (i % 2 == 0) {
                    if (i > 1)
                        recentNumber[i / 2] = recent[i];
                    else
                        recentNumber[i] = recent[i];
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, recentNumber);
            edMobile.setAdapter(adapter);
            edMobile.setThreshold(1);

        }
    }

    public void setListners() {

         btLogin.setOnClickListener(this);
         forgotpass.setOnClickListener(this);

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {

        if(v==bt_Register){



            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));



        }


        if(v==btLogin)
        {

            if(typevalue.equalsIgnoreCase("1")){
                if(tilMobile.getText().toString().trim().equalsIgnoreCase("")){
                    textLayoutMobile.setError("Enter Mobile Number");
                    ///Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }else if(tilPass.getText().toString().trim().equalsIgnoreCase("")){
                   textLayoutPassword.setError("Enter Password");
                   // Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();

                }else {
                    textLayoutMobile.setError("");textLayoutPassword.setError("");
                    if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        UtilMethods.INSTANCE.LoginWithPassword(this,tilMobile.getText().toString().trim(),tilPass.getText().toString().trim()  , loader,this);


                    } else {
                        UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                                getResources().getString(R.string.network_error_message));
                    }

                }


            }else{


                if(tilMobile.getText().toString().trim().equalsIgnoreCase("")){
                    //tilMobile.error = getString(R.string.error)
                    textLayoutMobile.setError("Enter Mobile Number");


                } else {

                    if (UtilMethods.INSTANCE.isNetworkAvialable( this)) {

                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        UtilMethods.INSTANCE.GenerateOTP(this,tilMobile.getText().toString().trim(), loader,this);


                    } else {
                        UtilMethods.INSTANCE.NetworkError(this, getResources().getString(R.string.network_error_title),
                                getResources().getString(R.string.network_error_message));
                    }

                }


            }

        }

        if(v==forgotpass)
        {
            OpenDialogFwd();

        }
    }
    public void OpenDialogFwd() {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.forgotpass, null);

        edMobileFwp = (EditText) view.findViewById(R.id.ed_mobile_fwp);
         tilMobileFwp=(TextInputLayout)view.findViewById(R.id.til_mobile_fwp);
        FwdokButton = (Button) view.findViewById(R.id.okButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        final Dialog dialog = new Dialog(this);

        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edMobileFwp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!validateMobileFwp()) {
                    return;
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        FwdokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateMobileFwp()) {
                    return;
                }



             }
        });

        dialog.show();
    }

    public boolean validateMobileFwp(){
        if (edMobileFwp.getText().toString().trim().isEmpty()) {
            tilMobileFwp.setError(getString(R.string.err_msg_mobile));
            requestFocus(edMobileFwp);
            return false;
        } else {
            tilMobileFwp.setErrorEnabled(false);
            FwdokButton.setEnabled(true);
        }

        return true;
    }


    public void SetLayoutWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width=displayMetrics.widthPixels;
        ViewGroup.LayoutParams params = imgProfilepic.getLayoutParams();
        params.height=(height *30)/100;;
        params.width=(height *30)/100;;

        int leftMargin=(width*5)/100;
        LinearLayout.LayoutParams mobileparam = (LinearLayout.LayoutParams)lIMobile.getLayoutParams();
        mobileparam.setMargins(leftMargin,10,leftMargin,10);
        lIMobile.setLayoutParams(mobileparam);
        LinearLayout.LayoutParams radioparam = (LinearLayout.LayoutParams)LI_btnRadio.getLayoutParams();
        radioparam.setMargins(leftMargin*2,10,leftMargin*2,10);
        LI_btnRadio.setLayoutParams(radioparam);
        LinearLayout.LayoutParams passparam = (LinearLayout.LayoutParams)LIpassword.getLayoutParams();
        passparam.setMargins(leftMargin,10,leftMargin,10);
        LIpassword.setLayoutParams(passparam);

        LinearLayout.LayoutParams loginparam = (LinearLayout.LayoutParams)LI_btnLogin.getLayoutParams();
        loginparam.setMargins(leftMargin*2,10,leftMargin*2,10);
        LI_btnLogin.setLayoutParams(loginparam);

        LinearLayout.LayoutParams registerparam = (LinearLayout.LayoutParams)LI_btnRegister.getLayoutParams();
        registerparam.setMargins(leftMargin*4,20,leftMargin*4,10);
        LI_btnRegister.setLayoutParams(registerparam);



        Log.e("ScreenSize","height : "+height);
        Log.e("ScreenSize","logoheight : "+params.height);
        Log.e("ScreenSize","leftMargin : "+leftMargin);
    }
}
