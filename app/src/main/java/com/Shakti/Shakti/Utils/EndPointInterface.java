package com.Shakti.Shakti.Utils;


import com.Shakti.Shakti.ApisetRespose.ApiBodyParam;
import com.Shakti.Shakti.Dashbord.dto.DashBoardAgentApiRes;
import com.Shakti.Shakti.Map.MapResponse;
import com.Shakti.Shakti.Register.dto.ProfileResponse;
import com.Shakti.Shakti.Register.dto.RegisterResponse;
import com.Shakti.Shakti.SubmitPurchase.dto.SiteResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EndPointInterface {

    @Headers("Content-Type: application/json")
    @POST("/app/PointStatusReport")
    Call<RegisterResponse> PointStatusReport(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/UpdateProfie")
    Call<UpdateKYCrespose> UpdateProfie(@Body ApiBodyParam log) ;

    @Headers("Content-Type: application/json")
    @POST("/app/UserRegistration")
    Call<RegisterResponse> UserRegistration(@Body ApiBodyParam log) ;

    @Headers("Content-Type: application/json")
    @POST("/app/UserDetails")
    Call<ProfileResponse> UserDetails(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/UpdateKYC")
    Call<UpdateKYCrespose> UpdateKYC(@Body JsonObject option) ;
    @Headers("Content-Type: application/json")
    @POST("/app/UpdatePhoto")
    Call<UpdateKYCrespose> UpdatePhoto(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/LoginWithPassword")
    Call<RegisterResponse> LoginWithPassword(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/GenerateOTP")
    Call<RegisterResponse> GenerateOTP(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/LoginWithOTP")
    Call<RegisterResponse> LoginWithOTP(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/AgentTypeList")
    Call<RegisterResponse> AgentTypeList(@Body JsonObject option);

    @Headers("Content-Type: application/json")
    @POST("/app/StateList")
    Call<RegisterResponse> StateList(@Body JsonObject option);

    @Headers("Content-Type: application/json")
    @POST("/app/ConstructionStageList")
    Call<RegisterResponse> Constructionstage(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/SitetypeList")
    Call<RegisterResponse> SitetypeList(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/SiteList")
    Call<RegisterResponse> SiteList(@Body JsonObject option) ;


  @Headers("Content-Type: application/json")
    @POST("/app/FeedBackCategoryList")
    Call<RegisterResponse> FeedBackCategoryList(@Body JsonObject option) ;


    @Headers("Content-Type: application/json")
    @POST("/app/DistrictList")
    Call<RegisterResponse> DistrictList(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/BlockList")
    Call<RegisterResponse> BlockList(@Body JsonObject option);

    @Headers("Content-Type: application/json")
    @POST("/app/DealerList")
    Call<RegisterResponse> DealerList(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/AgentList")
    Call<RegisterResponse> AgentList(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/ProductCategoryList")
    Call<RegisterResponse> ProductCategoryList(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/BrandList")
    Call<RegisterResponse> BrandList(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/ProductList")
    Call<RegisterResponse> ProductList(@Body JsonObject option) ;
    @Headers("Content-Type: application/json")
    @POST("/app/ProductbyCategoryList")
    Call<RegisterResponse> ProductbyCategoryList(@Body JsonObject option) ;
   @Headers("Content-Type: application/json")
    @POST("/app/RequestTechReport")
    Call<RegisterResponse> RequestTechReport(@Body JsonObject option) ;

   @Headers("Content-Type: application/json")
    @POST("/app/RedemptionReport")
    Call<RegisterResponse> RedemptionReport(@Body JsonObject option) ;

 @Headers("Content-Type: application/json")
    @POST("/app/SiteReport")
    Call<RegisterResponse> SiteReport(@Body JsonObject option) ;

 @Headers("Content-Type: application/json")
    @POST("/app/SubAdminSitesReport")
    Call<RegisterResponse> SubAdminSitesReport(@Body JsonObject option) ;

 @Headers("Content-Type: application/json")
    @POST("/app/EarnPointReport")
    Call<RegisterResponse> EarnPointReport(@Body JsonObject option) ;

 @Headers("Content-Type: application/json")
    @POST("/app/PurchaseAgentReport")
    Call<RegisterResponse> PurchaseAgentReport(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/MapReport")
    Call<MapResponse> MapReport(@Body JsonObject option) ;

 @Headers("Content-Type: application/json")
    @POST("/app/PurchaseDealerReport")
    Call<RegisterResponse> PurchaseDealerReport(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/SaleDealerReport")
    Call<RegisterResponse> SaleDealerReport(@Body JsonObject option) ;

 @Headers("Content-Type: application/json")
    @POST("/app/AgentReport")
    Call<RegisterResponse> AgentReport(@Body JsonObject option) ;


 @Headers("Content-Type: application/json")
    @POST("/app/DealerReport")
    Call<RegisterResponse> DealerReport(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/DashBoardAgent")
    Call<DashBoardAgentApiRes> DashBoardAgent(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/DashBoardSubAdmin")
    Call<DashBoardAgentApiRes> DashBoardSubAdmin(@Body JsonObject option) ;

  @Headers("Content-Type: application/json")
    @POST("/app/DashBoardDealer")
    Call<DashBoardAgentApiRes> DashBoardDealer(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/SiteVarification")
    Call<RegisterResponse> SiteVarification(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/SiteDetails")
    Call<SiteResponse> SiteDetails(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/SubmitNewPurchase")
    Call<RegisterResponse> SubmitNewPurchase(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/SubmitExistingPurchase")
    Call<RegisterResponse> SubmitExistingPurchase(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/UpdateDealerStatus")
    Call<RegisterResponse> UpdateDealerStatus(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/SubmitDealerPurchase")
    Call<RegisterResponse> SubmitDealerPurchase(@Body JsonObject option) ;

   @Headers("Content-Type: application/json")
    @POST("/app/Videos")
    Call<RegisterResponse> Videos(@Body JsonObject option) ;

  @Headers("Content-Type: application/json")
    @POST("/app/FeedBack")
    Call<RegisterResponse> FeedBack(@Body JsonObject option) ;



  @Headers("Content-Type: application/json")
    @POST("/app/Sendwish")
    Call<RegisterResponse> Sendwish(@Body JsonObject option) ;


  @Headers("Content-Type: application/json")
    @POST("/app/FeedbackReport")
    Call<RegisterResponse> FeedbackReport(@Body JsonObject option) ;


  @Headers("Content-Type: application/json")
    @POST("/app/WishReport")
    Call<RegisterResponse> WishReport(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/RewardRedeem")
    Call<RegisterResponse> RewardRedeem(@Body JsonObject option) ;

 @Headers("Content-Type: application/json")
    @POST("/app/GenerateOTPChangePassword")
    Call<RegisterResponse> GenerateOTPChangePassword(@Body JsonObject option) ;


 @Headers("Content-Type: application/json")
    @POST("/app/ChangePassword")
    Call<RegisterResponse> ChangePassword(@Body JsonObject option) ;


 @Headers("Content-Type: application/json")
    @POST("/app/Changemobile")
    Call<RegisterResponse> Changemobile(@Body JsonObject option) ;

 @Headers("Content-Type: application/json")
    @POST("/app/RequestTechExpress")
    Call<RegisterResponse> RequestTechExpress(@Body JsonObject option) ;


    @Headers("Content-Type: application/json")
    @POST("/app/FAQ")
    Call<RegisterResponse> FAQ(@Body JsonObject option) ;


    @Headers("Content-Type: application/json")
    @POST("/app/KnowMore")
    Call<RegisterResponse> KnowMore(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/Events")
    Call<RegisterResponse> Events(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/WishList")
    Call<RegisterResponse> WishList(@Body JsonObject option) ;


    @Headers("Content-Type: application/json")
    @POST("/app/NotificationList")
    Call<RegisterResponse> Notificationlist(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/RewardList")
    Call<RegisterResponse> RewardList(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/TermsCondition")
    Call<TermsConditionResponse> TermsCondition(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/ContactUS")
    Call<TermsConditionResponse> ContactUS(@Body JsonObject option) ;

    @Headers("Content-Type: application/json")
    @POST("/app/PrivacyPolicy")
    Call<TermsConditionResponse> PrivacyPolicy(@Body JsonObject option) ;


}
