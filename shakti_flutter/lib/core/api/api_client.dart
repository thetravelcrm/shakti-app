import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../constants.dart';
import 'api_response.dart';

class ApiClient {
  final http.Client _client;
  final SharedPreferences _prefs;

  ApiClient(this._prefs) : _client = http.Client();

  String? get _session {
    final loginJson = _prefs.getString(Constants.keyLoginResponse);
    if (loginJson == null) return null;
    try {
      final map = jsonDecode(loginJson) as Map<String, dynamic>;
      return map['session']?.toString();
    } catch (_) {
      return null;
    }
  }

  String? get _userId {
    final loginJson = _prefs.getString(Constants.keyLoginResponse);
    if (loginJson == null) return null;
    try {
      final map = jsonDecode(loginJson) as Map<String, dynamic>;
      return map['userID']?.toString();
    } catch (_) {
      return null;
    }
  }

  Future<ApiResponse> post(
    String endpoint,
    Map<String, dynamic> body, {
    bool injectAuth = true,
  }) async {
    // Auto-inject common params
    body['APPID'] = Constants.appId;
    body['Version'] = Constants.appVersion;
    body['IMEI'] = Constants.imei;
    body['RegKey'] = Constants.regKey;

    if (injectAuth) {
      final session = _session;
      final userId = _userId;
      if (session != null) body['session'] = session;
      if (userId != null) body['UserID'] = userId;
    }

    final url = '${Constants.baseUrl}/app/$endpoint';

    if (kDebugMode) {
      print('API POST $url');
      print('Body: ${jsonEncode(body)}');
    }

    try {
      final response = await _client
          .post(
            Uri.parse(url),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode(body),
          )
          .timeout(const Duration(seconds: 30));

      if (kDebugMode) {
        print('Response ${response.statusCode}: ${response.body.length > 500 ? response.body.substring(0, 500) : response.body}');
      }

      if (response.statusCode == 200) {
        final json = jsonDecode(response.body);
        if (json is Map<String, dynamic>) {
          return ApiResponse.fromJson(json);
        }
        return ApiResponse(
          statuscode: '0',
          msg: 'Invalid response format',
          raw: {},
        );
      } else {
        return ApiResponse(
          statuscode: '0',
          msg: 'Server error: ${response.statusCode}',
          raw: {},
        );
      }
    } catch (e) {
      if (kDebugMode) print('API Error: $e');
      return ApiResponse(
        statuscode: '0',
        msg: 'Connection error. Please check your internet.',
        raw: {},
      );
    }
  }

  // Convenience: post with just session/userId (most common pattern)
  Future<ApiResponse> postAuth(String endpoint, [Map<String, dynamic>? extra]) async {
    final body = <String, dynamic>{};
    if (extra != null) body.addAll(extra);
    return post(endpoint, body);
  }

  void dispose() {
    _client.close();
  }
}
