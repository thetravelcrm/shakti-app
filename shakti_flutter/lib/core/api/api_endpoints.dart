class ApiEndpoints {
  // Auth
  static const String loginWithPassword = 'LoginWithPassword';
  static const String generateOTP = 'GenerateOTP';
  static const String loginWithOTP = 'LoginWithOTP';
  static const String userRegistration = 'UserRegistration';
  static const String generateOTPChangePassword = 'GenerateOTPChangePassword';
  static const String changePassword = 'ChangePassword';
  static const String changeMobile = 'Changemobile';

  // Profile
  static const String userDetails = 'UserDetails';
  static const String updateProfile = 'UpdateProfie';
  static const String updateKYC = 'UpdateKYC';
  static const String updatePhoto = 'UpdatePhoto';

  // Dashboard
  static const String dashBoardAgent = 'DashBoardAgent';
  static const String dashBoardDealer = 'DashBoardDealer';
  static const String dashBoardSubAdmin = 'DashBoardSubAdmin';
  static const String pointStatusReport = 'PointStatusReport';

  // Master Data
  static const String stateList = 'StateList';
  static const String districtList = 'DistrictList';
  static const String blockList = 'BlockList';
  static const String agentTypeList = 'AgentTypeList';
  static const String dealerList = 'DealerList';
  static const String agentList = 'AgentList';
  static const String productCategoryList = 'ProductCategoryList';
  static const String brandList = 'BrandList';
  static const String productList = 'ProductList';
  static const String productByCategoryList = 'ProductbyCategoryList';
  static const String constructionStageList = 'ConstructionStageList';
  static const String siteTypeList = 'SitetypeList';
  static const String feedBackCategoryList = 'FeedBackCategoryList';

  // Sites
  static const String siteList = 'SiteList';
  static const String siteDetails = 'SiteDetails';
  static const String siteVerification = 'SiteVarification';

  // Purchase
  static const String submitNewPurchase = 'SubmitNewPurchase';
  static const String submitExistingPurchase = 'SubmitExistingPurchase';
  static const String submitDealerPurchase = 'SubmitDealerPurchase';
  static const String updateDealerStatus = 'UpdateDealerStatus';

  // Reports
  static const String purchaseAgentReport = 'PurchaseAgentReport';
  static const String purchaseDealerReport = 'PurchaseDealerReport';
  static const String saleDealerReport = 'SaleDealerReport';
  static const String agentReport = 'AgentReport';
  static const String dealerReport = 'DealerReport';
  static const String earnPointReport = 'EarnPointReport';
  static const String redemptionReport = 'RedemptionReport';
  static const String siteReport = 'SiteReport';
  static const String subAdminSitesReport = 'SubAdminSitesReport';
  static const String mapReport = 'MapReport';
  static const String feedbackReport = 'FeedbackReport';
  static const String wishReport = 'WishReport';
  static const String requestTechReport = 'RequestTechReport';

  // Content
  static const String videos = 'Videos';
  static const String faq = 'FAQ';
  static const String knowMore = 'KnowMore';
  static const String events = 'Events';

  // Feedback & Wishes
  static const String feedBack = 'FeedBack';
  static const String sendWish = 'Sendwish';
  static const String wishList = 'WishList';
  static const String requestTechExpress = 'RequestTechExpress';

  // Rewards
  static const String rewardList = 'RewardList';
  static const String rewardRedeem = 'RewardRedeem';

  // Notifications
  static const String notificationList = 'NotificationList';

  // Static Pages
  static const String termsCondition = 'TermsCondition';
  static const String privacyPolicy = 'PrivacyPolicy';
  static const String contactUs = 'ContactUS';
}
