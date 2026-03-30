package com.Shakti.Shakti.Dashbord.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Register.ui.RegisterActivity;
import com.Shakti.Shakti.Register.ui.SteatelistAdapter;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

public class RequesttechexpressActivity extends Fragment implements View.OnClickListener {

    EditText MobileNumber,personName,time,selectdate;
    Button bt_submit;
    Calendar calendar;
    private int CalendarHour, CalendarMinute;
    String format;
    String timeval;
    Loader loader;

    TimePickerDialog timepickerdialog;


   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesttechexpress);*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.activity_requesttechexpress, container, false);

        getid(v);




        return  v;

    }

    private void getid(View v) {

        loader = new Loader(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);



        bt_submit=v.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);







        MobileNumber=v.findViewById(R.id.MobileNumber);
        personName=v.findViewById(R.id.personName);
        time=v.findViewById(R.id.time);
        MobileNumber=v.findViewById(R.id.MobileNumber);
        selectdate=v.findViewById(R.id.selectdate);

        selectdate.setOnClickListener(this);

        time.setOnClickListener(this);
        bt_submit.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {



        if(view==time){


            calendar = Calendar.getInstance();
            CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
            CalendarMinute = calendar.get(Calendar.MINUTE);


            timepickerdialog = new TimePickerDialog(getActivity(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            if (hourOfDay == 0) {

                                hourOfDay += 12;

                                format = "AM";
                            }
                            else if (hourOfDay == 12) {

                                format = "PM";

                            }
                            else if (hourOfDay > 12) {

                                hourOfDay -= 12;

                                format = "PM";

                            }
                            else {

                                format = "AM";
                            }


                            time.setText(hourOfDay + ":" + minute + format);

                            timeval=hourOfDay + ":" + minute + format;

                        }
                    }, CalendarHour, CalendarMinute, false);
            timepickerdialog.show();



        }


  if(view==selectdate){


            Datepicker();

        }

 if(view==bt_submit){



     if(personName.getText().toString().trim().equalsIgnoreCase("")){

         Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT).show();



     }else if(time.getText().toString().trim().equalsIgnoreCase("")){

         Toast.makeText(getActivity(), "Select Time", Toast.LENGTH_SHORT).show();



     }else if(selectdate.getText().toString().trim().equalsIgnoreCase("")){

         Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT).show();



     }else if(MobileNumber.getText().toString().trim().equalsIgnoreCase("")){

         Toast.makeText(getActivity(), "Mobile Number", Toast.LENGTH_SHORT).show();



     }else {


         if (UtilMethods.INSTANCE.isNetworkAvialable( getActivity())) {

             loader.show();
             loader.setCancelable(false);
             loader.setCanceledOnTouchOutside(false);


             UtilMethods.INSTANCE.RequestTechExpress(getActivity(), personName.getText().toString().trim(),time.getText().toString().trim(),
                     selectdate.getText().toString().trim(),MobileNumber.getText().toString().trim(), loader,null);

         } else {
             UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                     getResources().getString(R.string.network_error_message));
         }

     }



        }



    }


    private int mYear, mMonth, mDay;

    public void Datepicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {




                        int in=(monthOfYear + 1);

                        int dd=dayOfMonth;

                        int b=Integer.toString(in).length();
                        int ddd=Integer.toString(dd).length();


                        if(b==2){

                            if(ddd==2){
                                selectdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }else if(ddd==1){

                                selectdate.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }




                        }else if(b==1){
                            if(ddd==2){
                                selectdate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                            }else if(ddd==1){

                                selectdate.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                            }
                        }




                        //    startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();



    }


}
