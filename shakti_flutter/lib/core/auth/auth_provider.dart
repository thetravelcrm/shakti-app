import 'dart:convert';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../constants.dart';
import '../api/api_client.dart';
import '../api/api_response.dart';
import '../api/api_endpoints.dart';

enum UserRole { agent, dealer, subAdmin, unknown }

class AuthState {
  final bool isLoggedIn;
  final String? session;
  final String? userId;
  final String? userCode;
  final String? name;
  final String? mobileNo;
  final String? photo;
  final UserRole role;
  final Map<String, dynamic>? loginData;

  const AuthState({
    this.isLoggedIn = false,
    this.session,
    this.userId,
    this.userCode,
    this.name,
    this.mobileNo,
    this.photo,
    this.role = UserRole.unknown,
    this.loginData,
  });

  AuthState copyWith({
    bool? isLoggedIn,
    String? session,
    String? userId,
    String? userCode,
    String? name,
    String? mobileNo,
    String? photo,
    UserRole? role,
    Map<String, dynamic>? loginData,
  }) {
    return AuthState(
      isLoggedIn: isLoggedIn ?? this.isLoggedIn,
      session: session ?? this.session,
      userId: userId ?? this.userId,
      userCode: userCode ?? this.userCode,
      name: name ?? this.name,
      mobileNo: mobileNo ?? this.mobileNo,
      photo: photo ?? this.photo,
      role: role ?? this.role,
      loginData: loginData ?? this.loginData,
    );
  }

  static UserRole parseRole(String? role) {
    if (role == null) return UserRole.unknown;
    final r = role.toLowerCase().trim();
    if (r.contains('dealer')) return UserRole.dealer;
    if (r.contains('sub') || r.contains('admin')) return UserRole.subAdmin;
    return UserRole.agent;
  }
}

class AuthNotifier extends StateNotifier<AuthState> {
  final SharedPreferences _prefs;
  late final ApiClient _api;

  AuthNotifier(this._prefs) : super(const AuthState()) {
    _api = ApiClient(_prefs);
    _loadFromPrefs();
  }

  ApiClient get api => _api;

  void _loadFromPrefs() {
    final loginJson = _prefs.getString(Constants.keyLoginResponse);
    final isLoggedIn = _prefs.getString(Constants.keyOne) == '1';

    if (loginJson != null && isLoggedIn) {
      try {
        final data = jsonDecode(loginJson) as Map<String, dynamic>;
        state = AuthState(
          isLoggedIn: true,
          session: data['session']?.toString(),
          userId: data['userID']?.toString(),
          userCode: data['userCode']?.toString(),
          name: data['name']?.toString(),
          mobileNo: data['mobileNo']?.toString(),
          photo: data['photo']?.toString(),
          role: AuthState.parseRole(data['role']?.toString()),
          loginData: data,
        );
      } catch (_) {
        state = const AuthState();
      }
    }
  }

  Future<ApiResponse> loginWithPassword(String mobile, String password) async {
    final fcmToken = _prefs.getString(Constants.keyToken) ?? '';
    final response = await _api.post(
      ApiEndpoints.loginWithPassword,
      {
        'mobileNo': mobile,
        'password': password,
        'userType': '0',
        'FCMToken': fcmToken,
      },
      injectAuth: false,
    );

    if (response.isSuccess) {
      await _saveLogin(response);
    }
    return response;
  }

  Future<ApiResponse> generateOTP(String mobile) async {
    return _api.post(
      ApiEndpoints.generateOTP,
      {'mobileNo': mobile},
      injectAuth: false,
    );
  }

  Future<ApiResponse> loginWithOTP(String mobile, String otp, String session) async {
    final fcmToken = _prefs.getString(Constants.keyToken) ?? '';
    final response = await _api.post(
      ApiEndpoints.loginWithOTP,
      {
        'mobileNo': mobile,
        'otp': otp,
        'session': session,
        'FCMToken': fcmToken,
      },
      injectAuth: false,
    );

    if (response.isSuccess) {
      await _saveLogin(response);
    }
    return response;
  }

  Future<void> _saveLogin(ApiResponse response) async {
    final rawJson = jsonEncode(response.raw);
    await _prefs.setString(Constants.keyLoginResponse, rawJson);
    await _prefs.setString(Constants.keyOne, '1');

    if (response.mobileNo != null) {
      await _prefs.setString(Constants.keyRecentLogin, response.mobileNo!);
    }

    state = AuthState(
      isLoggedIn: true,
      session: response.session,
      userId: response.userID,
      userCode: response.userCode,
      name: response.name,
      mobileNo: response.mobileNo,
      photo: response.photo,
      role: AuthState.parseRole(response.role),
      loginData: response.raw,
    );
  }

  Future<void> logout() async {
    await _prefs.remove(Constants.keyLoginResponse);
    await _prefs.remove(Constants.keyOne);
    await _prefs.remove(Constants.keyProfile);
    state = const AuthState();
  }

  @override
  void dispose() {
    _api.dispose();
    super.dispose();
  }
}

// Providers
final sharedPrefsProvider = Provider<SharedPreferences>((ref) {
  throw UnimplementedError('Must be overridden in main');
});

final authProvider = StateNotifierProvider<AuthNotifier, AuthState>((ref) {
  final prefs = ref.watch(sharedPrefsProvider);
  return AuthNotifier(prefs);
});

final apiProvider = Provider<ApiClient>((ref) {
  return ref.watch(authProvider.notifier).api;
});
