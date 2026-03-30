package com.Shakti.Shakti.Dashbord.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.Shakti.Shakti.Dashbord.dto.DashBoardAgentApiRes;
import com.Shakti.Shakti.Dashbord.dto.ProductList;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.ReportAll.Activity.Helpernew;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class RequesttexkexpreeActivityhistroyFragment extends Fragment implements View.OnClickListener {
    ListView li_mobile,li_name,li_time,li_date;
    Loader loader;

    RegisterResponse transactionspack = new RegisterResponse();
    ArrayList<Datares> transactionsObjectspack = new ArrayList<>();


    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> createdDatelist = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> Mobile = new ArrayList<String>();

    ScrollView scrrolview;
    ImageView image;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


         View v= inflater.inflate(R.layout.fragment_requesttexkexpree_activityhistroy, container, false);

        loader = new Loader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);



        scrrolview=v.findViewById(R.id.scrrolview);
        image=v.findViewById(R.id.image);


        li_mobile=v.findViewById(R.id.li_mobile);
        li_name=v.findViewById(R.id.li_name);
        li_time=v.findViewById(R.id.li_time);
        li_date=v.findViewById(R.id.li_date);



         HitApi();

         return  v;

    }

    private void HitApi() {

        if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.RequestTechReport(getActivity(), loader,scrrolview,image);

        } else {
            UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }



    }

    @Override
    public void onClick(View view) {




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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("RequestTechReport")) {
            String startelist=""+activityFragmentMessage.getFrom();


            dataParseRequestTechReport(startelist);

        }

    }

    public void dataParseRequestTechReport(String response) {

        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, RegisterResponse.class);
        transactionsObjectspack = transactionspack.getList();

        if (transactionsObjectspack.size() > 0) {




            if (transactionsObjectspack != null && transactionsObjectspack.size() > 0) {

                for (int i = 0; i < transactionsObjectspack.size(); i++) {

                    date.add(transactionsObjectspack.get(i).getDate());
                    time.add(transactionsObjectspack.get(i).getTime());
                    Name.add(transactionsObjectspack.get(i).getName());
                    Mobile.add(transactionsObjectspack.get(i).getMobile());
                    createdDatelist.add(transactionsObjectspack.get(i).getCreatedDate());

                }
            }



            li_mobile.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, Mobile));
            Helpernew.getListViewSize(li_mobile);





            li_name.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, Name));
            Helpernew.getListViewSize(li_name);






            li_time.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, time));
            Helpernew.getListViewSize(li_time);





            li_date.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, date));
            Helpernew.getListViewSize(li_date);






        } else {

        }


    }




}