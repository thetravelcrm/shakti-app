class ApiResponse {
  final String? status;
  final String? statuscode;
  final String? msg;
  final String? session;
  final String? userID;
  final String? userCode;
  final String? userRole;
  final String? mobileNo;
  final String? name;
  final String? role;
  final String? photo;
  final String? otp;
  final String? isVersionValid;
  final String? isAppValid;
  final String? isSessionVaild;
  final String? isOTPRequired;
  final List<Map<String, dynamic>>? data;
  final List<Map<String, dynamic>>? list;
  final List<Map<String, dynamic>>? earn;
  final List<Map<String, dynamic>>? redeem;

  // Dashboard-specific fields
  final Map<String, dynamic>? dashData;
  final List<Map<String, dynamic>>? siteList;
  final List<Map<String, dynamic>>? purchaseList;
  final List<Map<String, dynamic>>? saleList;
  final List<Map<String, dynamic>>? redemptionList;
  final List<Map<String, dynamic>>? productList;

  // Raw JSON for caching
  final Map<String, dynamic> raw;

  ApiResponse({
    this.status,
    this.statuscode,
    this.msg,
    this.session,
    this.userID,
    this.userCode,
    this.userRole,
    this.mobileNo,
    this.name,
    this.role,
    this.photo,
    this.otp,
    this.isVersionValid,
    this.isAppValid,
    this.isSessionVaild,
    this.isOTPRequired,
    this.data,
    this.list,
    this.earn,
    this.redeem,
    this.dashData,
    this.siteList,
    this.purchaseList,
    this.saleList,
    this.redemptionList,
    this.productList,
    required this.raw,
  });

  factory ApiResponse.fromJson(Map<String, dynamic> json) {
    return ApiResponse(
      status: json['status']?.toString(),
      statuscode: json['statuscode']?.toString(),
      msg: json['msg']?.toString(),
      session: json['session']?.toString(),
      userID: json['userID']?.toString(),
      userCode: json['userCode']?.toString(),
      userRole: json['userRole']?.toString(),
      mobileNo: json['mobileNo']?.toString(),
      name: json['name']?.toString(),
      role: json['role']?.toString(),
      photo: json['photo']?.toString(),
      otp: json['otp']?.toString(),
      isVersionValid: json['isVersionValid']?.toString(),
      isAppValid: json['isAppValid']?.toString(),
      isSessionVaild: json['isSessionVaild']?.toString(),
      isOTPRequired: json['isOTPRequired']?.toString(),
      data: _toListMap(json['data']),
      list: _toListMap(json['list']),
      earn: _toListMap(json['earn']),
      redeem: _toListMap(json['redeem']),
      dashData: json['data'] is Map<String, dynamic> ? json['data'] : null,
      siteList: _toListMap(json['siteList']),
      purchaseList: _toListMap(json['purchaseList']),
      saleList: _toListMap(json['saleList']),
      redemptionList: _toListMap(json['redemptionList']),
      productList: _toListMap(json['productList']),
      raw: json,
    );
  }

  bool get isSuccess => statuscode == '1';

  static List<Map<String, dynamic>>? _toListMap(dynamic value) {
    if (value is List) {
      return value.map((e) => e is Map<String, dynamic> ? e : <String, dynamic>{}).toList();
    }
    return null;
  }
}
