package com.Shakti.Shakti.Dashbord.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Setting.WishkaroActivity;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class FeedBackFragment extends Fragment implements View.OnClickListener {

    EditText Description;
    Button btsubmit;
    Loader loader;

    RegisterResponse transactions = new RegisterResponse();
    ArrayList<Datares> transactionsspinner = new ArrayList<>();
    ArrayList<String> bankList = new ArrayList<String>();

    Spinner sitensamelist;
    String idCategoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_feed_back, container, false);

        btsubmit=v.findViewById(R.id.btsubmit);
        btsubmit.setOnClickListener(this);

        loader = new Loader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);



        Description=v.findViewById(R.id.Description);
        sitensamelist=v.findViewById(R.id.sitensamelist);

        SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String setSitetypeList = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setFeedBackCategoryList, null);
        dataParsesCityList(setSitetypeList);

        return v;
    }


    public void dataParsesCityList(String response) {

        bankList.add("Select Category");

        Gson gson = new Gson();
        transactions = gson.fromJson(response, RegisterResponse.class);
        transactionsspinner = transactions.getData();

        if (transactionsspinner.size() > 0) {

            if (transactionsspinner != null && transactionsspinner.size() > 0) {

                for (int i = 0; i < transactionsspinner.size(); i++) {

                    bankList.add(transactionsspinner.get(i).getName());

                }
            }


            sitensamelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Log.e("spinner","  position   "+ position + "  ,  id  "+  id);

                    if (parent.getItemAtPosition(position).toString().equals("Select Category")) {
                        idCategoryList="0";
                    } else {
                        idCategoryList = transactionsspinner.get(position-1).getId();


                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            ArrayAdapter<String> countryAdapter;
            countryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, bankList);
            sitensamelist.setAdapter(countryAdapter);

        } else {

        }

    }


    @Override
    public void onClick(View view) {


        if(view==btsubmit){


            if(Description.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(getActivity(), "Description Can not be Empty", Toast.LENGTH_SHORT).show();

            }else {

                if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    UtilMethods.INSTANCE.FeedBack(getActivity(),  Description.getText().toString().trim(),idCategoryList,loader);

                } else {
                    UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message));
                }
            }

        }


    }
}