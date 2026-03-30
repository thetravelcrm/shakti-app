package com.Shakti.Shakti.Dashbord.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.Shakti.Shakti.Adapter.FaqAdapter;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.Datares;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.Setting.WishkaroActivity;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


public class FeedBackFragmentReport extends Fragment {

    RecyclerView recycler_view;
    LinearLayoutManager mLayoutManager;
    ArrayList<Datares> transactionsObjects = new ArrayList<>();
    RegisterResponse transactions = new RegisterResponse();
    FaqAdapter faqAdapter;
    Loader loader;



     ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_feed_back_report, container, false);

        recycler_view=v.findViewById(R.id.recycler_view);
        loader = new Loader(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
         image=v.findViewById(R.id.image);

//   dataParseFAQ(""+response);


        HitApi();

        return v;
    }

    private void HitApi() {


        if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.FeedbackReport(getActivity(),  loader,recycler_view,image);

        } else {
            UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
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
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("FeedbackReport")) {
            String startelist=""+activityFragmentMessage.getFrom();

            dataParseFAQ(startelist);


        }

    }





    public void dataParseFAQ(String response) {

        Gson gson = new Gson();
        transactions = gson.fromJson(response, RegisterResponse.class);
        transactionsObjects = transactions.getList();

        if (transactionsObjects.size() > 0) {
            faqAdapter = new FaqAdapter(transactionsObjects, getActivity(),"2");
            mLayoutManager = new GridLayoutManager(getActivity(),1);
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(faqAdapter);

            recycler_view.setVisibility(View.VISIBLE);
        } else {
            recycler_view.setVisibility(View.GONE);
        }

    }


}