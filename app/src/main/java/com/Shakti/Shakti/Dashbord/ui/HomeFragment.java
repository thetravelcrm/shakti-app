package com.Shakti.Shakti.Dashbord.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Shakti.Shakti.Dashbord.dto.DashBoardAdapterSitepending;
import com.Shakti.Shakti.Dashbord.dto.DashBoardAgentApiRes;
import com.Shakti.Shakti.Dashbord.dto.DashBoardProductAdapter;
import com.Shakti.Shakti.Dashbord.dto.DashBoardRedemptionAdapter;
import com.Shakti.Shakti.Dashbord.dto.DashBoardSiteAdapter;
import com.Shakti.Shakti.Dashbord.dto.Data;
import com.Shakti.Shakti.Dashbord.dto.ProductList;
import com.Shakti.Shakti.Map.MapActivity;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Register.dto.ProfileResponse;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.ReportAll.Activity.DealerPurchaseStatusActivity;
import com.Shakti.Shakti.ReportAll.Activity.DealerSaleStatusActivity;
import com.Shakti.Shakti.ReportAll.Activity.Helpernew;
import com.Shakti.Shakti.ReportAll.Activity.PurchaseStatusActivity;
import com.Shakti.Shakti.ReportAll.Activity.RedemptionHistoryActivity;
import com.Shakti.Shakti.ReportAll.Fragment.PointStatesActivityBoth;
import com.Shakti.Shakti.SubmitPurchase.ui.DealerpurchaseActivity;
import com.Shakti.Shakti.SubmitPurchase.ui.MyAccountActivity;
import com.Shakti.Shakti.SubmitPurchase.ui.PurchaseActivity;
import com.Shakti.Shakti.SubmitPurchase.ui.ReemallActivity;
import com.Shakti.Shakti.Utils.ApplicationConstant;
import com.Shakti.Shakti.Utils.FragmentActivityMessage;
import com.Shakti.Shakti.Utils.GlobalBus;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.Shakti.Shakti.ViewAll.ViewDetail_therr_Activity;
import com.Shakti.Shakti.ViewAll.ViewDetail_two_Activity;
import com.Shakti.Shakti.ViewAll.View_Detail_four_Activity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener {

    SwipeRefreshLayout mSwipeRefreshLayout;

    TextView AGENTname,AGENTcode,Remaining,Redeemed,Earned;
    LinearLayout SubmitPurchase,Redemptionli,myaccount;
    Loader loader;
    RecyclerView recycler_viewpackage;
    ArrayList<ProductList> transactionsObjectspack = new ArrayList<>();
    DashBoardAgentApiRes transactionspack = new DashBoardAgentApiRes();
    DashBoardProductAdapter mAdapterpackage;
    DashBoardAdapterSitepending mAdapterpackagesite;
    DashBoardRedemptionAdapter mAdapterpackageredeem;
    DashBoardSiteAdapter madapterSiteAdapter;
    LinearLayoutManager mLayoutManagerpack;
    LinearLayout li_REDEMPTION,LI_SUBMITPURCHASE,LI_PENDINGSITES,product_liall,LI_DashBoardDealer,point,Reportsall,LI_DashBoardDealerPurchase;
    LinearLayout Li_Dealer_Sale_Varify,Li_Dealer_Sale_Date,Li_Dealer_Sale_OrderNO,Li_Dealer_Sale_SiteName,Li_Dealer_Sale_Product,Li_Dealer_Sale_Agent,Li_Dealer_Sale_District,Li_Dealer_Sale_Quantity;


    ArrayList<String> Statuslist = new ArrayList<String>();
    ArrayList<String> orderNolist = new ArrayList<String>();
    ArrayList<String> createdDatelist = new ArrayList<String>();
    ArrayList<String> SiteNamelist = new ArrayList<String>();
    ArrayList<String> productNamelist = new ArrayList<String>();
    ArrayList<String> dealerNamelist = new ArrayList<String>();
    ArrayList<String> quantitylisr = new ArrayList<String>();
    ArrayList<String> pointslist = new ArrayList<String>();


    ArrayList<String> DeliveryStatus = new ArrayList<String>();
    ArrayList<String> pointslistredem = new ArrayList<String>();
    ArrayList<String> quantitylisrredem = new ArrayList<String>();
    ArrayList<String> productNamelistredem = new ArrayList<String>();
    ArrayList<String> orderNolistredem = new ArrayList<String>();
    ArrayList<String> createdDatelistredem = new ArrayList<String>();

    ArrayList<String> AgentNasub = new ArrayList<String>();
    ArrayList<String> siteNamesub = new ArrayList<String>();
    ArrayList<String> siteTypesub = new ArrayList<String>();
    ArrayList<String> DistrictNamesub = new ArrayList<String>();
    ArrayList<String> CreatedDatesub = new ArrayList<String>();
    ArrayList<String> MobileNosub = new ArrayList<String>();
    ArrayList<String> verfyid = new ArrayList<String>();

    ArrayList<String> AgentNamedealerval = new ArrayList<String>();
    ArrayList<String> SiteNamedealerdeta = new ArrayList<String>();
    ArrayList<String> OrderNodealerdeta = new ArrayList<String>();
    ArrayList<String> DistrictNamedealer = new ArrayList<String>();
    ArrayList<String> CreatedDatedealer = new ArrayList<String>();
    ArrayList<String> ProdNamedealer = new ArrayList<String>();
    ArrayList<String> Quantitydealerval = new ArrayList<String>();
    ArrayList<String> PurchaseStatusdealer = new ArrayList<String>();
    ArrayList<String> VarifyDealerList=new ArrayList<>();

    ArrayList<String> DealerSnoList =new ArrayList<>();
    ArrayList<String> DealerStatusList =new ArrayList<>();
    ArrayList<String> DealerPurchaseDateList =new ArrayList<>();
    ArrayList<String> DealerEntryDateList =new ArrayList<>();
    ArrayList<String> DealerProductList =new ArrayList<>();
    ArrayList<String> DealerQuantityList =new ArrayList<>();

    ListView Status,Date,Ordernumber,Site,Dealer,Quantity,Points;
    ListView OrdernumberRECENT,Productname,Quantityrecent,Pointsresent,Statusresent,DateRECENT;
    ListView Districtsub,Datesub,SiteNamesub,SiteOwnermobilenumb,SiteTypesub,AgentNamesub,AgentMobileNosub,verfy;
    ListView verifySaleDealer,Statusdealer,Quantitydealer,Districtdealer,AgentNamedealer,ProductNamedealer,SiteNamedealer,Ordeenodealer,Datedealer;
    ListView SnodealerPurchase,StatusdealerPurchase,ProductdealerPurchase,QuantitydealerPurchase,DatedealerPurchase,PurchaseDatedealerPurchase;
    MyAdapter adapter;
    String UserCode="";
    TextView v1,v2,v3,v4,v5,vd1;
    LinearLayout Earned_li,Redeemed_li,Li_MapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_home, container, false);

       GetId(v);
        setValueReferse();
        return v;

    }

    private void GetId(View v) {

        Redeemed_li=v.findViewById(R.id.Redeemed_li);
        Earned_li=v.findViewById(R.id.Earned_li);
        Li_MapView=v.findViewById(R.id.Li_MapView);
        Li_MapView.setOnClickListener(this);
        point=v.findViewById(R.id.point);
        Reportsall=v.findViewById(R.id.Reportsall);
        v1=v.findViewById(R.id.v1);
        v2=v.findViewById(R.id.v2);
        v3=v.findViewById(R.id.v3);
        v4=v.findViewById(R.id.v4);
        v5=v.findViewById(R.id.v5);
        vd1=v.findViewById(R.id.vd1);

        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        v4.setOnClickListener(this);
        v5.setOnClickListener(this);
        vd1.setOnClickListener(this);

        Earned_li.setOnClickListener(this);
        Redeemed_li.setOnClickListener(this);

        verifySaleDealer=v.findViewById(R.id.verifySaleDealer);
        Statusdealer=v.findViewById(R.id.Statusdealer);
        Quantitydealer=v.findViewById(R.id.Quantitydealer);
        Districtdealer=v.findViewById(R.id.Districtdealer);
        AgentNamedealer=v.findViewById(R.id.AgentNamedealer);
        ProductNamedealer=v.findViewById(R.id.ProductNamedealer);
        SiteNamedealer=v.findViewById(R.id.SiteNamedealer);
        Ordeenodealer=v.findViewById(R.id.Ordeenodealer);
        Datedealer=v.findViewById(R.id.Datedealer);
        myaccount=v.findViewById(R.id.myaccount);
        myaccount.setOnClickListener(this);

        SnodealerPurchase=v.findViewById(R.id.SnodealerPurchase);
        StatusdealerPurchase=v.findViewById(R.id.StatusdealerPurchase);
        ProductdealerPurchase=v.findViewById(R.id.ProductdealerPurchase);
        QuantitydealerPurchase=v.findViewById(R.id.QuantitydealerPurchase);
        DatedealerPurchase=v.findViewById(R.id.DatedealerPurchase);
        PurchaseDatedealerPurchase=v.findViewById(R.id.PurchaseDatedealerPurchase);

        Status=v.findViewById(R.id.Status);
        Date=v.findViewById(R.id.Date);
        Ordernumber=v.findViewById(R.id.Ordernumber);
        Site=v.findViewById(R.id.Site);
        Dealer=v.findViewById(R.id.Dealer);
        Quantity=v.findViewById(R.id.Quantity);
        Points=v.findViewById(R.id.Points);

        loader = new Loader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);

        Districtsub=v.findViewById(R.id.Districtsub);
        Datesub=v.findViewById(R.id.Datesub);
        SiteNamesub=v.findViewById(R.id.SiteNamesub);
        SiteOwnermobilenumb=v.findViewById(R.id.SiteOwnermobilenumb);
        SiteTypesub=v.findViewById(R.id.SiteTypesub);
        AgentNamesub=v.findViewById(R.id.AgentNamesub);
        AgentMobileNosub=v.findViewById(R.id.AgentMobileNosub);
        verfy=v.findViewById(R.id.verify);

        OrdernumberRECENT=v.findViewById(R.id.OrdernumberRECENT);
        Productname=v.findViewById(R.id.Productname);
        Quantityrecent=v.findViewById(R.id.Quantityrecent);
        Pointsresent=v.findViewById(R.id.Pointsresent);
        Statusresent=v.findViewById(R.id.Statusresent);
        DateRECENT=v.findViewById(R.id.DateRECENT);

         LI_SUBMITPURCHASE=v.findViewById(R.id.LI_SUBMITPURCHASE);
        LI_PENDINGSITES=v.findViewById(R.id.LI_PENDINGSITES);
        LI_DashBoardDealer=v.findViewById(R.id.LI_DashBoardDealer);
        product_liall=v.findViewById(R.id.product_liall);
         li_REDEMPTION=v.findViewById(R.id.li_REDEMPTION);
        LI_DashBoardDealerPurchase=v.findViewById(R.id.LI_DashBoardDealerPurchase);
         recycler_viewpackage=v.findViewById(R.id.recycler_viewpackage);


        SubmitPurchase=v.findViewById(R.id.SubmitPurchase);
        Redemptionli=v.findViewById(R.id.Redemptionli);
        AGENTname=v.findViewById(R.id.AGENTname);
        AGENTcode=v.findViewById(R.id.AGENTcode);
        Remaining=v.findViewById(R.id.Remaining);
        Redeemed=v.findViewById(R.id.Redeemed);
        Earned=v.findViewById(R.id.Earned);

        AGENTname.setOnClickListener(this);
        AGENTcode.setOnClickListener(this);
        SubmitPurchase.setOnClickListener(this);
        Redemptionli.setOnClickListener(this);
        Reportsall.setOnClickListener(this);

        Li_Dealer_Sale_Product=v.findViewById(R.id.Li_Dealer_Sale_Product);
        Li_Dealer_Sale_Varify=v.findViewById(R.id.Li_Dealer_Sale_Varify);
        Li_Dealer_Sale_Date=v.findViewById(R.id.Li_Dealer_Sale_Date);
        Li_Dealer_Sale_OrderNO=v.findViewById(R.id.Li_Dealer_Sale_OrderNO);
        Li_Dealer_Sale_SiteName=v.findViewById(R.id.Li_Dealer_Sale_SiteName);
        Li_Dealer_Sale_Agent=v.findViewById(R.id.Li_Dealer_Sale_Agent);
        Li_Dealer_Sale_District=v.findViewById(R.id.Li_Dealer_Sale_District);
        Li_Dealer_Sale_Quantity=v.findViewById(R.id.Li_Dealer_Sale_Quantity);

        mSwipeRefreshLayout = v.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                setValueReferse();

                mSwipeRefreshLayout.setRefreshing(false);


            }
        });


        setValue();

        if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

            //UtilMethods.INSTANCE.SiteList(getActivity(), null);
            UtilMethods.INSTANCE.FeedBackCategoryList(getActivity(), null);
            UtilMethods.INSTANCE.PointStatusReport(getActivity(), null,null,null);


          /*  loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            UtilMethods.INSTANCE.SitetypeList(this, loader);*/

        } else {
            UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                    getResources().getString(R.string.network_error_message));
        }





    }

    @Override
    public void onClick(View v) {


        if(v==Reportsall  ) {

            Intent i=new Intent(getActivity(), ReportAllActivity.class);
            startActivity(i);

        }


       if(v==Redeemed_li  ) {

           Intent i=new Intent(new Intent(getActivity(), PointStatesActivityBoth.class));
           i.putExtra("type","2");
           startActivity(i);

        }

       if(v==Earned_li  ) {

           Intent i=new Intent(new Intent(getActivity(), PointStatesActivityBoth.class));
           i.putExtra("type","1");
           startActivity(i);

        }

        if(v==v1  ) {

            Intent i=new Intent(getActivity(), DealerPurchaseStatusActivity.class);
            startActivity(i);
        }


        if(v==v2  ) {

            Intent i=new Intent(getActivity(), ViewDetail_two_Activity.class);
            startActivity(i);
        }


  if(v==v3  ) {

      startActivity(new Intent(getActivity(), PurchaseStatusActivity.class));


  }


  if(v==v4 ) {
      startActivity(new Intent(getActivity(), RedemptionHistoryActivity.class));

        }

  if(v==vd1)
  {
      startActivity(new Intent(getActivity(), DealerSaleStatusActivity.class));

  }
        if(v==Li_MapView)
        {startActivity(new Intent(getActivity(), MapActivity.class));}

        if(v==myaccount) {

            Intent i=new Intent(getActivity(), MyAccountActivity.class);
            startActivity(i);

        }


        if(v==SubmitPurchase) {

            if(UserCode.equalsIgnoreCase("5")) {
                Intent i = new Intent(getActivity(), PurchaseActivity.class);
                startActivity(i);
            }
            else {
                Intent i = new Intent(getActivity(), DealerpurchaseActivity.class);
                startActivity(i);
            }

        }





        if(v==Redemptionli) {


            if (UtilMethods.INSTANCE.isNetworkAvialable( getActivity())) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.RewardList(getActivity(),  loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }



            /* Intent i=new Intent(getActivity(), ReemallActivity.class);
            startActivity(i);*/


        }



        if(v==AGENTcode){




        }




    }

    private void setValueReferse() {

        Statuslist.clear();
        orderNolist.clear();
        createdDatelist.clear();
        SiteNamelist.clear();
        productNamelist.clear();
        dealerNamelist.clear();
        quantitylisr.clear();
        pointslist.clear();

        DeliveryStatus.clear();
        pointslistredem.clear();
        quantitylisrredem.clear();
        productNamelistredem.clear();
        orderNolistredem.clear();
        createdDatelistredem.clear();

        AgentNasub.clear();
        siteNamesub.clear();
        siteTypesub.clear();
        DistrictNamesub.clear();
        CreatedDatesub.clear();
        MobileNosub.clear();
        verfyid.clear();

        VarifyDealerList.clear();
        AgentNamedealerval.clear();
        SiteNamedealerdeta.clear();
        OrderNodealerdeta.clear();
        DistrictNamedealer.clear();
        CreatedDatedealer.clear();
        ProdNamedealer.clear();
        Quantitydealerval.clear();
        PurchaseStatusdealer.clear();

        DealerSnoList.clear();
        DealerStatusList.clear();
        DealerPurchaseDateList.clear();
        DealerEntryDateList.clear();
        DealerProductList.clear();
        DealerQuantityList.clear();

       // orderNolist.clear(); // this list which you hava passed in Adapter for your listview
       // yourAdapter.notifyDataSetChanged(); // notify to listview for refresh


        SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        String onevalue = ""+myPreferences.getString(ApplicationConstant.INSTANCE.one, null);

        String DashBoardSubAdmin = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDashBoardSubAdmin, null);
        String DashBoardDealer = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDashBoardDealer, null);
        String DashBoardAgent = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDashBoardAgent, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        String UserCode=""+balanceCheckResponse.getUserRole();

        Log.e("UserCode","UserCode :  "+ UserCode +"  onevalue  "+onevalue +"     DashBoardSubAdmin  " +
                ":  "+ DashBoardSubAdmin  +"      DashBoardDealer :  "+ DashBoardDealer +"      DashBoardAgent    :  "+ DashBoardAgent   );



        if(UserCode.equalsIgnoreCase("3")){

            SubmitPurchase.setVisibility(View.GONE);
            Redemptionli.setVisibility(View.GONE);
            myaccount.setVisibility(View.VISIBLE);
            product_liall.setVisibility(View.GONE);
            LI_SUBMITPURCHASE.setVisibility(View.GONE);
            li_REDEMPTION.setVisibility(View.GONE);
            LI_PENDINGSITES.setVisibility(View.VISIBLE);
             LI_DashBoardDealer.setVisibility(View.GONE);

                if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    UtilMethods.INSTANCE.DashBoardSubAdmin(getActivity(), loader,LI_PENDINGSITES,product_liall,"2",null);

                } else {
                    UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message));
                }

        }
        else  if(UserCode.equalsIgnoreCase("4")){

            ///Dealer
            Li_MapView.setVisibility(View.GONE);
            SubmitPurchase.setVisibility(View.VISIBLE);
            Redemptionli.setVisibility(View.GONE);
            LI_SUBMITPURCHASE.setVisibility(View.GONE);
            li_REDEMPTION.setVisibility(View.GONE);
            LI_PENDINGSITES.setVisibility(View.GONE);
            product_liall.setVisibility(View.GONE);
            myaccount.setVisibility(View.VISIBLE);



            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    UtilMethods.INSTANCE.DashBoardDealer(getActivity(), loader,LI_DashBoardDealer,LI_DashBoardDealerPurchase,product_liall,"2",null);

                } else {
                    UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message));


            }




        }
        else  if(UserCode.equalsIgnoreCase("5")){
            Li_MapView.setVisibility(View.GONE);
            myaccount.setVisibility(View.GONE);
            SubmitPurchase.setVisibility(View.VISIBLE);
            Redemptionli.setVisibility(View.VISIBLE);

            myaccount.setVisibility(View.GONE);



//  Agent

             LI_PENDINGSITES.setVisibility(View.GONE);
            LI_DashBoardDealer.setVisibility(View.GONE);
            LI_DashBoardDealerPurchase.setVisibility(View.GONE);

                if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    UtilMethods.INSTANCE.DashBoardAgent(getActivity(), loader,LI_SUBMITPURCHASE,product_liall,"2",null);

                } else {
                    UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message));
                }




        }


        AGENTname.setText(""+balanceCheckResponse.getName());
        AGENTcode.setText(" -  "+balanceCheckResponse.getUserCode()+"");

    }

    private void setValue() {

        SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.Loginrespose, null);
        String onevalue = ""+myPreferences.getString(ApplicationConstant.INSTANCE.one, null);

        String DashBoardSubAdmin = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDashBoardSubAdmin, null);
        String DashBoardDealer = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDashBoardDealer, null);
        String DashBoardAgent = ""+myPreferences.getString(ApplicationConstant.INSTANCE.setDashBoardAgent, null);

        RegisterResponse balanceCheckResponse = new Gson().fromJson(balanceResponse, RegisterResponse.class);
        UserCode=""+balanceCheckResponse.getUserRole();

        Log.e("UserCode","UserCode :  "+ UserCode +"  onevalue  "+onevalue +"     DashBoardSubAdmin  " +
                ":  "+ DashBoardSubAdmin  +"      DashBoardDealer :  "+ DashBoardDealer +"      DashBoardAgent    :  "+ DashBoardAgent   );



        if(UserCode.equalsIgnoreCase("3")){

            SubmitPurchase.setVisibility(View.GONE);
            Redemptionli.setVisibility(View.GONE);
            myaccount.setVisibility(View.VISIBLE);
            product_liall.setVisibility(View.GONE);
            LI_SUBMITPURCHASE.setVisibility(View.GONE);
            li_REDEMPTION.setVisibility(View.GONE);
            LI_PENDINGSITES.setVisibility(View.VISIBLE);
            LI_DashBoardDealer.setVisibility(View.GONE);
            LI_DashBoardDealerPurchase.setVisibility(View.GONE);
            point.setVisibility(View.GONE);


            if ( DashBoardSubAdmin!=null){


                dataParsepackageSite(DashBoardSubAdmin);
                //dataParsepackageSitepending(DashBoardSubAdmin);

            }else{


                if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    UtilMethods.INSTANCE.DashBoardSubAdmin(getActivity(), loader,LI_PENDINGSITES,product_liall,"2",null);

                } else {
                    UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message));
                }

            }




        }
        else  if(UserCode.equalsIgnoreCase("4")){

            ///Dealer
            Li_MapView.setVisibility(View.GONE);
            SubmitPurchase.setVisibility(View.VISIBLE);

            Redemptionli.setVisibility(View.GONE);
            LI_SUBMITPURCHASE.setVisibility(View.GONE);
            li_REDEMPTION.setVisibility(View.GONE);
            LI_PENDINGSITES.setVisibility(View.GONE);
            product_liall.setVisibility(View.GONE);
            myaccount.setVisibility(View.VISIBLE);
            point.setVisibility(View.GONE);



            if ( DashBoardDealer!=null){


                dataParsedealersubmit(DashBoardDealer);
                //dataParsepackageSitepending(DashBoardDealer);
                dataParsedealerSalesubmit(DashBoardDealer);

            }else{


                if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    UtilMethods.INSTANCE.DashBoardDealer(getActivity(), loader,LI_DashBoardDealer,LI_DashBoardDealerPurchase,product_liall,"2",null);

                } else {
                    UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message));
                }

            }


        }
        else  if(UserCode.equalsIgnoreCase("5")){
            Li_MapView.setVisibility(View.GONE);
//  Agent
            myaccount.setVisibility(View.GONE);
            SubmitPurchase.setVisibility(View.VISIBLE);
            Redemptionli.setVisibility(View.VISIBLE);

            myaccount.setVisibility(View.GONE);
            LI_DashBoardDealerPurchase.setVisibility(View.GONE);
            LI_PENDINGSITES.setVisibility(View.GONE);
            LI_DashBoardDealer.setVisibility(View.GONE);
            point.setVisibility(View.VISIBLE);


            if ( DashBoardAgent!=null){

                dataParsepackage(DashBoardAgent);
                dataParseREDEMPTION(DashBoardAgent);
                dataParseSUBMITPURCHASE(DashBoardAgent);

            }else{


                if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    UtilMethods.INSTANCE.DashBoardAgent(getActivity(), loader,LI_SUBMITPURCHASE,product_liall,"2",null);

                } else {
                    UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                            getResources().getString(R.string.network_error_message));
                }

            }


        }

        AGENTname.setText(""+balanceCheckResponse.getName());
        AGENTcode.setText(" -  "+balanceCheckResponse.getUserCode()+"  ");

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
         if (activityFragmentMessage.getMessage().equalsIgnoreCase("DashBoardAgent")) {
            String startelist=""+activityFragmentMessage.getFrom();

             dataParsepackage(startelist);
             dataParseREDEMPTION(startelist);
             dataParseSUBMITPURCHASE(startelist);

        }else if (activityFragmentMessage.getMessage().equalsIgnoreCase("DashBoardSubAdmin")) {

             String startelist=""+activityFragmentMessage.getFrom();

             dataParsepackageSite(startelist);
            //dataParsepackageSitepending(startelist);

         }else if (activityFragmentMessage.getMessage().equalsIgnoreCase("DashBoardDealer")) {

             String startelist=""+activityFragmentMessage.getFrom();
             Log.i("SaleSubmit",""+startelist);
             dataParsedealersubmit(startelist);
             //dataParsepackageSitepending(startelist);
             dataParsedealerSalesubmit(startelist);

         }

    }


    public void dataParseSUBMITPURCHASE(String response) {
        Statuslist.clear();
        pointslist.clear();
        quantitylisr.clear();
        dealerNamelist.clear();
        productNamelist.clear();
        SiteNamelist.clear();
        orderNolist.clear();
        createdDatelist.clear();
        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, DashBoardAgentApiRes.class);
        transactionsObjectspack = transactionspack.getPurchaseList();

        if (transactionsObjectspack.size() > 0) {




            if (transactionsObjectspack != null && transactionsObjectspack.size() > 0) {

                for (int i = 0; i < transactionsObjectspack.size(); i++) {

                    Statuslist.add(transactionsObjectspack.get(i).getPurchaseStatus());
                    pointslist.add(transactionsObjectspack.get(i).getPoints());
                    quantitylisr.add(transactionsObjectspack.get(i).getQuantity());
                    dealerNamelist.add(transactionsObjectspack.get(i).getDealerName());
                    productNamelist.add(transactionsObjectspack.get(i).getProductName());
                    SiteNamelist.add(transactionsObjectspack.get(i).getSiteName());
                    orderNolist.add(transactionsObjectspack.get(i).getOrderNo());
                    createdDatelist.add(transactionsObjectspack.get(i).getCreatedDate());


                }



            }


            adapter = new MyAdapter(getActivity(), Statuslist,"3");
            Status.setAdapter(adapter);



        /*    Status.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, Statuslist));
            Helpernew.getListViewSize(Status);*/


  Ordernumber.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, orderNolist));
            Helpernew.getListViewSize(Ordernumber);






            Site.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, SiteNamelist));
            Helpernew.getListViewSize(Site);


            Dealer.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, dealerNamelist));
            Helpernew.getListViewSize(Dealer);




            Date.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, createdDatelist));
            Helpernew.getListViewSize(Date);




            Quantity.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, quantitylisr));
            Helpernew.getListViewSize(Quantity);



            Points.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, pointslist));
            Helpernew.getListViewSize(Points);



        } else {
            LI_SUBMITPURCHASE.setVisibility(View.GONE);
        }


    }

    public void dataParseREDEMPTION(String response) {
        DeliveryStatus.clear();
        pointslistredem.clear();
        quantitylisrredem.clear();
        productNamelistredem.clear();
        orderNolistredem.clear();
        createdDatelistredem.clear();
        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, DashBoardAgentApiRes.class);
        transactionsObjectspack = transactionspack.getRedemptionList();

        if (transactionsObjectspack.size() > 0) {

            if (transactionsObjectspack != null && transactionsObjectspack.size() > 0) {

                for (int i = 0; i < transactionsObjectspack.size(); i++) {

                    DeliveryStatus.add(transactionsObjectspack.get(i).getDeliveryStatus());
                    pointslistredem.add(transactionsObjectspack.get(i).getPoints());
                    quantitylisrredem.add(transactionsObjectspack.get(i).getQuantity());
                     productNamelistredem.add(transactionsObjectspack.get(i).getProductName());
                     orderNolistredem.add(transactionsObjectspack.get(i).getOrderNo());
                    createdDatelistredem.add(transactionsObjectspack.get(i).getCreatedDate());

                }

            }


            OrdernumberRECENT.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, orderNolistredem));
            Helpernew.getListViewSize(OrdernumberRECENT);




            Productname.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, productNamelistredem));
            Helpernew.getListViewSize(Productname);




            Quantityrecent.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, quantitylisrredem));
            Helpernew.getListViewSize(Quantityrecent);



            Pointsresent.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, pointslistredem));
            Helpernew.getListViewSize(Pointsresent);


            adapter = new MyAdapter(getActivity(), DeliveryStatus,"3");
            Statusresent.setAdapter(adapter);



            /*

            ArrayAdapter<String> countryAdapter5;
            countryAdapter5 = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, DeliveryStatus);
            Statusresent.setAdapter(countryAdapter5);

            */




            DateRECENT.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, createdDatelistredem));
            Helpernew.getListViewSize(DateRECENT);



        }  else {


            li_REDEMPTION.setVisibility(View.GONE);


        }


    }

    public void dataParsepackage(String response) {

        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, DashBoardAgentApiRes.class);
        transactionsObjectspack = transactionspack.getProductList();

        SharedPreferences myPreferences = getActivity().getSharedPreferences(ApplicationConstant.INSTANCE.prefNamePref, MODE_PRIVATE);
        String balanceResponse = myPreferences.getString(ApplicationConstant.INSTANCE.balancePref, null);

        Data balanceCheckResponse = new Gson().fromJson(balanceResponse, Data.class);

        UtilMethods.INSTANCE.Getremaning(getActivity(),balanceCheckResponse.getRemaining());

        Remaining.setText(""+balanceCheckResponse.getRemaining());
        Redeemed.setText(""+balanceCheckResponse.getRedeemed());
        Earned.setText(""+balanceCheckResponse.getEarned());

        if (transactionsObjectspack.size() > 0) {
            mAdapterpackage = new DashBoardProductAdapter(transactionsObjectspack, getActivity(),"1");
            mLayoutManagerpack = new LinearLayoutManager( getActivity());
            recycler_viewpackage.setLayoutManager(new GridLayoutManager(getActivity(),2));
            recycler_viewpackage.setItemAnimator(new DefaultItemAnimator());
            recycler_viewpackage.setAdapter(mAdapterpackage);
            recycler_viewpackage.setVisibility(View.VISIBLE);
        } else {

            recycler_viewpackage.setVisibility(View.GONE);
            product_liall.setVisibility(View.GONE);




        }

    }

    public void dataParsepackageSitepending(String response) {

        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, DashBoardAgentApiRes.class);
        transactionsObjectspack = transactionspack.getProductList();


        if (transactionsObjectspack.size() > 0) {
            mAdapterpackagesite = new DashBoardAdapterSitepending(transactionsObjectspack, getActivity());
            mLayoutManagerpack = new LinearLayoutManager( getActivity());
          //  recycler_viewpackage.setLayoutManager(new LinearLayoutManager( getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recycler_viewpackage.setLayoutManager(new GridLayoutManager(getActivity(),2));
            recycler_viewpackage.setItemAnimator(new DefaultItemAnimator());
            recycler_viewpackage.setAdapter(mAdapterpackagesite);
            recycler_viewpackage.setVisibility(View.VISIBLE);
        } else {
            recycler_viewpackage.setVisibility(View.GONE);

            product_liall.setVisibility(View.GONE);

        }

    }

    public void dataParsepackageSite(String response) {
        AgentNasub.clear();
        siteNamesub.clear();
        siteTypesub.clear();
        DistrictNamesub.clear();
        CreatedDatesub.clear();
        MobileNosub.clear();
        verfyid.clear();

        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, DashBoardAgentApiRes.class);
        transactionsObjectspack = transactionspack.getSiteList();

        if (transactionsObjectspack.size() > 0) {

            if (transactionsObjectspack != null && transactionsObjectspack.size() > 0) {

                for (int i = 0; i < transactionsObjectspack.size(); i++) {



                    AgentNasub.add(transactionsObjectspack.get(i).getAgentName());
                    siteNamesub.add(transactionsObjectspack.get(i).getSiteName());
                    siteTypesub.add(transactionsObjectspack.get(i).getSiteType());
                    DistrictNamesub.add(transactionsObjectspack.get(i).getDistrictName());
                    CreatedDatesub.add(transactionsObjectspack.get(i).getCreatedDate());
                    MobileNosub.add(transactionsObjectspack.get(i).getMobileNo());
                    verfyid.add(transactionsObjectspack.get(i).getId());



                }

            }



            SiteNamesub.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, siteNamesub));
            Helpernew.getListViewSize(SiteNamesub);


            Districtsub.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, DistrictNamesub));
            Helpernew.getListViewSize(Districtsub);





            Datesub.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, CreatedDatesub));
            Helpernew.getListViewSize(Datesub);





            SiteTypesub.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, siteTypesub));
            Helpernew.getListViewSize(SiteTypesub);






            AgentNamesub.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, AgentNasub));
            Helpernew.getListViewSize(AgentNamesub);






            AgentMobileNosub.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, MobileNosub));
            Helpernew.getListViewSize(AgentMobileNosub);




            adapter = new MyAdapter(getActivity(), verfyid,"1");
            verfy.setAdapter(adapter);



        } else {

            LI_PENDINGSITES.setVisibility(View.GONE);


        }

    }

    public void dataParsedealersubmit(String response) {
        VarifyDealerList.clear();
        AgentNamedealerval.clear();
        SiteNamedealerdeta.clear();
        OrderNodealerdeta.clear();
        DistrictNamedealer.clear();
        CreatedDatedealer.clear();
        ProdNamedealer.clear();
        Quantitydealerval.clear();
        PurchaseStatusdealer.clear();
        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, DashBoardAgentApiRes.class);
        transactionsObjectspack = transactionspack.getPurchaseList();

        if (transactionsObjectspack!=null && transactionsObjectspack.size() > 0) {

            if (transactionsObjectspack != null && transactionsObjectspack.size() > 0) {

                for (int i = 0; i < transactionsObjectspack.size(); i++) {

                    VarifyDealerList.add(transactionsObjectspack.get(i).getId());
                    AgentNamedealerval.add(transactionsObjectspack.get(i).getAgentName());
                    SiteNamedealerdeta.add(transactionsObjectspack.get(i).getSiteName());
                    OrderNodealerdeta.add(transactionsObjectspack.get(i).getOrderNo());
                    DistrictNamedealer.add(transactionsObjectspack.get(i).getDistrictName());
                    CreatedDatedealer.add(transactionsObjectspack.get(i).getCreatedDate());
                    ProdNamedealer.add(transactionsObjectspack.get(i).getProductName());
                    Quantitydealerval.add(transactionsObjectspack.get(i).getQuantity());
                    PurchaseStatusdealer.add(transactionsObjectspack.get(i).getPurchaseStatus());

                }

            }

            adapter=new MyAdapter(getActivity(),VarifyDealerList,"2");
            verifySaleDealer.setAdapter(adapter);
            //Helpernew.setLinearLayoutStaticWidth(Li_Dealer_Sale_Varify,200);
            //Helpernew.setListViewWidth(verifySaleDealer,Li_Dealer_Sale_Varify);

            adapter = new MyAdapter(getActivity(), PurchaseStatusdealer,"3");
            Statusdealer.setAdapter(adapter);

          /*  ArrayAdapter<String> countryAsiteNamesubr;
            countryAsiteNamesubr = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, PurchaseStatusdealer);
            Statusdealer.setAdapter(countryAsiteNamesubr);*/






            Quantitydealer.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, Quantitydealerval));
            Helpernew.getListViewSize(Quantitydealer);
            Helpernew.setListViewWidth(Quantitydealer,Li_Dealer_Sale_Quantity,200);



            Districtdealer.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, DistrictNamedealer));
            Helpernew.getListViewSize(Districtdealer);
            Helpernew.setListViewWidth(Districtdealer,Li_Dealer_Sale_District,100);



            AgentNamedealer.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, AgentNamedealerval));
            Helpernew.getListViewSize(AgentNamedealer);
            Helpernew.setListViewWidth(AgentNamedealer,Li_Dealer_Sale_Agent,100);


            ProductNamedealer.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, ProdNamedealer));
            Helpernew.getListViewSize(ProductNamedealer);
            Helpernew.setListViewWidth(ProductNamedealer,Li_Dealer_Sale_Product,100);

            Ordeenodealer.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, OrderNodealerdeta));
            Helpernew.getListViewSize(Ordeenodealer);
            Helpernew.setListViewWidth(Ordeenodealer,Li_Dealer_Sale_OrderNO,100);

            Datedealer.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, CreatedDatedealer));
            Helpernew.getListViewSize(Datedealer);
            Helpernew.setListViewWidth(Datedealer,Li_Dealer_Sale_Date,100);
            SiteNamedealer.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.activity_listview, SiteNamedealerdeta));
            Helpernew.getListViewSize(SiteNamedealer);
            Helpernew.setListViewWidth(SiteNamedealer,Li_Dealer_Sale_SiteName,100);

        }
        else {

            LI_DashBoardDealer.setVisibility(View.GONE);

        }


    }

    public void dataParsedealerSalesubmit(String response) {
        DealerSnoList.clear();
        DealerPurchaseDateList.clear();
        DealerEntryDateList.clear();
        DealerProductList.clear();
        DealerQuantityList.clear();

        Gson gson = new Gson();
        transactionspack = gson.fromJson(response, DashBoardAgentApiRes.class);
        transactionsObjectspack = transactionspack.getSaleList();
        int Sno=1;

            Log.i("SaleSubmit",""+transactionsObjectspack.size());
            if (transactionsObjectspack!=null && transactionsObjectspack.size() > 0) {

                for (int i = 0; i < transactionsObjectspack.size(); i++) {
                    DealerSnoList.add(""+Sno);
                    Sno++;
                    DealerStatusList.add(transactionsObjectspack.get(i).getPurchaseStatus());
                    DealerProductList.add(transactionsObjectspack.get(i).getProductName());
                    DealerEntryDateList.add(transactionsObjectspack.get(i).getCreatedDate());
                    DealerQuantityList.add(transactionsObjectspack.get(i).getQuantity());
                    DealerPurchaseDateList.add(transactionsObjectspack.get(i).getPurchaseDate());
                }

//            adapter = new MyAdapter(getActivity(), PurchaseStatusdealer,"3");
//            StatusdealerPurchase.setAdapter(adapter);
                Log.i("SaleSubmit",""+DealerSnoList.size()+" again");
                if(getActivity()!=null) {

                    adapter = new MyAdapter(getActivity(), DealerStatusList,"3");
                    StatusdealerPurchase.setAdapter(adapter);

                    SnodealerPurchase.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, DealerSnoList));
                    Helpernew.getListViewSize(SnodealerPurchase);

                    ProductdealerPurchase.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, DealerProductList));
                    Helpernew.getListViewSize(ProductdealerPurchase);

                    QuantitydealerPurchase.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, DealerQuantityList));
                    Helpernew.getListViewSize(QuantitydealerPurchase);

                    DatedealerPurchase.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, DealerEntryDateList));
                    Helpernew.getListViewSize(DatedealerPurchase);

                    PurchaseDatedealerPurchase.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, DealerPurchaseDateList));
                    Helpernew.getListViewSize(PurchaseDatedealerPurchase);
                }
            }
            else {

                LI_DashBoardDealerPurchase.setVisibility(View.GONE);

            }


    }

}
