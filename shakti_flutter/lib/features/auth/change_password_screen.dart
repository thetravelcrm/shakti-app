import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class ChangePasswordScreen extends ConsumerStatefulWidget {
  const ChangePasswordScreen({super.key});

  @override
  ConsumerState<ChangePasswordScreen> createState() => _ChangePasswordScreenState();
}

class _ChangePasswordScreenState extends ConsumerState<ChangePasswordScreen> {
  final _formKey = GlobalKey<FormState>();
  bool _isLoading = false;
  bool _otpSent = false;
  bool _obscureNew = true;
  bool _obscureConfirm = true;

  final _otpCtrl = TextEditingController();
  final _newPassCtrl = TextEditingController();
  final _confirmPassCtrl = TextEditingController();
  String? _otpSession;

  @override
  void dispose() {
    _otpCtrl.dispose();
    _newPassCtrl.dispose();
    _confirmPassCtrl.dispose();
    super.dispose();
  }

  Future<void> _generateOtp() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.generateOTPChangePassword);
      if (mounted) {
        if (response.isSuccess) {
          setState(() {
            _otpSent = true;
            _otpSession = response.session ?? response.raw['session']?.toString();
          });
          showSuccessSnackbar(context, response.msg ?? 'OTP sent to your mobile');
        } else {
          showErrorSnackbar(context, response.msg ?? 'Failed to send OTP');
        }
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  Future<void> _changePassword() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final body = <String, dynamic>{
        'otp': _otpCtrl.text.trim(),
        'newPassword': _newPassCtrl.text.trim(),
      };
      if (_otpSession != null) {
        body['otpSession'] = _otpSession;
      }
      final response = await api.postAuth(ApiEndpoints.changePassword, body);
      if (mounted) {
        if (response.isSuccess) {
          showSuccessSnackbar(context, response.msg ?? 'Password changed');
          Navigator.pop(context);
        } else {
          showErrorSnackbar(context, response.msg ?? 'Failed to change password');
        }
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Change Password')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Form(
            key: _formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      children: [
                        const Icon(Icons.lock_outline, size: 48, color: AppTheme.primaryColor),
                        const SizedBox(height: 12),
                        const Text(
                          'Change Your Password',
                          style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(height: 4),
                        Text(
                          _otpSent
                              ? 'Enter the OTP sent to your mobile and set a new password'
                              : 'Click the button below to receive an OTP on your registered mobile',
                          textAlign: TextAlign.center,
                          style: const TextStyle(color: AppTheme.textSecondary),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 24),
                if (!_otpSent) ...[
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton.icon(
                      onPressed: _generateOtp,
                      icon: const Icon(Icons.sms),
                      label: const Text('Send OTP'),
                    ),
                  ),
                ] else ...[
                  TextFormField(
                    controller: _otpCtrl,
                    decoration: const InputDecoration(
                      labelText: 'OTP',
                      prefixIcon: Icon(Icons.pin),
                      hintText: 'Enter OTP',
                    ),
                    keyboardType: TextInputType.number,
                    maxLength: 6,
                    validator: (v) => v == null || v.trim().isEmpty ? 'Enter OTP' : null,
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _newPassCtrl,
                    obscureText: _obscureNew,
                    decoration: InputDecoration(
                      labelText: 'New Password',
                      prefixIcon: const Icon(Icons.lock),
                      suffixIcon: IconButton(
                        icon: Icon(_obscureNew ? Icons.visibility : Icons.visibility_off),
                        onPressed: () => setState(() => _obscureNew = !_obscureNew),
                      ),
                    ),
                    validator: (v) {
                      if (v == null || v.trim().isEmpty) return 'Enter new password';
                      if (v.length < 6) return 'Minimum 6 characters';
                      return null;
                    },
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _confirmPassCtrl,
                    obscureText: _obscureConfirm,
                    decoration: InputDecoration(
                      labelText: 'Confirm Password',
                      prefixIcon: const Icon(Icons.lock_outline),
                      suffixIcon: IconButton(
                        icon: Icon(_obscureConfirm ? Icons.visibility : Icons.visibility_off),
                        onPressed: () => setState(() => _obscureConfirm = !_obscureConfirm),
                      ),
                    ),
                    validator: (v) {
                      if (v == null || v.trim().isEmpty) return 'Confirm password';
                      if (v != _newPassCtrl.text) return 'Passwords do not match';
                      return null;
                    },
                  ),
                  const SizedBox(height: 24),
                  ElevatedButton.icon(
                    onPressed: _changePassword,
                    icon: const Icon(Icons.check),
                    label: const Text('Change Password'),
                  ),
                  const SizedBox(height: 12),
                  TextButton(
                    onPressed: _generateOtp,
                    child: const Text('Resend OTP'),
                  ),
                ],
              ],
            ),
          ),
        ),
      ),
    );
  }
}
