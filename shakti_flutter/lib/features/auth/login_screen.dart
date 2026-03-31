import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class LoginScreen extends ConsumerStatefulWidget {
  const LoginScreen({super.key});

  @override
  ConsumerState<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends ConsumerState<LoginScreen> {
  final _mobileController = TextEditingController();
  final _passwordController = TextEditingController();
  final _otpController = TextEditingController();
  bool _isLoading = false;
  bool _useOTP = false;
  String? _otpSession;
  bool _otpSent = false;
  bool _obscurePassword = true;

  @override
  void dispose() {
    _mobileController.dispose();
    _passwordController.dispose();
    _otpController.dispose();
    super.dispose();
  }

  Future<void> _login() async {
    final mobile = _mobileController.text.trim();
    if (mobile.length != 10) {
      showErrorSnackbar(context, 'Enter valid 10-digit mobile number');
      return;
    }

    setState(() => _isLoading = true);

    try {
      if (_useOTP) {
        if (!_otpSent) {
          final res = await ref.read(authProvider.notifier).generateOTP(mobile);
          if (res.isSuccess) {
            setState(() {
              _otpSent = true;
              _otpSession = res.session ?? res.raw['session']?.toString();
            });
            if (mounted) showSuccessSnackbar(context, 'OTP sent to $mobile');
          } else {
            if (mounted) showErrorSnackbar(context, res.msg ?? 'Failed to send OTP');
          }
        } else {
          final otp = _otpController.text.trim();
          if (otp.isEmpty) {
            if (mounted) showErrorSnackbar(context, 'Enter OTP');
            return;
          }
          final res = await ref.read(authProvider.notifier).loginWithOTP(
            mobile,
            otp,
            _otpSession ?? '',
          );
          if (res.isSuccess) {
            if (mounted) context.go('/dashboard');
          } else {
            if (mounted) showErrorSnackbar(context, res.msg ?? 'Invalid OTP');
          }
        }
      } else {
        final password = _passwordController.text.trim();
        if (password.isEmpty) {
          if (mounted) showErrorSnackbar(context, 'Enter password');
          return;
        }
        final res = await ref.read(authProvider.notifier).loginWithPassword(mobile, password);
        if (res.isSuccess) {
          if (mounted) context.go('/dashboard');
        } else {
          if (mounted) showErrorSnackbar(context, res.msg ?? 'Login failed');
        }
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: LoadingOverlay(
        isLoading: _isLoading,
        message: _useOTP && !_otpSent ? 'Sending OTP...' : 'Logging in...',
        child: SafeArea(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(24),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                const SizedBox(height: 40),
                // Logo
                Container(
                  width: 80,
                  height: 80,
                  decoration: BoxDecoration(
                    color: AppTheme.primaryColor,
                    borderRadius: BorderRadius.circular(20),
                  ),
                  child: const Center(
                    child: Text(
                      'S',
                      style: TextStyle(
                        fontSize: 48,
                        fontWeight: FontWeight.bold,
                        color: Colors.white,
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 24),
                const Text(
                  'Welcome Back',
                  style: TextStyle(fontSize: 28, fontWeight: FontWeight.bold),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 8),
                const Text(
                  'Login to your Shakti account',
                  style: TextStyle(fontSize: 14, color: AppTheme.textSecondary),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 40),

                // Login type toggle
                Row(
                  children: [
                    Expanded(
                      child: _LoginTypeChip(
                        label: 'Password',
                        selected: !_useOTP,
                        onTap: () => setState(() {
                          _useOTP = false;
                          _otpSent = false;
                        }),
                      ),
                    ),
                    const SizedBox(width: 12),
                    Expanded(
                      child: _LoginTypeChip(
                        label: 'OTP',
                        selected: _useOTP,
                        onTap: () => setState(() {
                          _useOTP = true;
                          _otpSent = false;
                        }),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 24),

                // Mobile
                TextField(
                  controller: _mobileController,
                  keyboardType: TextInputType.phone,
                  maxLength: 10,
                  decoration: const InputDecoration(
                    labelText: 'Mobile Number',
                    prefixIcon: Icon(Icons.phone_android),
                    counterText: '',
                  ),
                ),
                const SizedBox(height: 16),

                // Password or OTP
                if (!_useOTP)
                  TextField(
                    controller: _passwordController,
                    obscureText: _obscurePassword,
                    decoration: InputDecoration(
                      labelText: 'Password',
                      prefixIcon: const Icon(Icons.lock_outline),
                      suffixIcon: IconButton(
                        icon: Icon(_obscurePassword ? Icons.visibility_off : Icons.visibility),
                        onPressed: () => setState(() => _obscurePassword = !_obscurePassword),
                      ),
                    ),
                  ),

                if (_useOTP && _otpSent)
                  TextField(
                    controller: _otpController,
                    keyboardType: TextInputType.number,
                    maxLength: 6,
                    decoration: const InputDecoration(
                      labelText: 'Enter OTP',
                      prefixIcon: Icon(Icons.pin),
                      counterText: '',
                    ),
                  ),

                const SizedBox(height: 32),

                // Login button
                ElevatedButton(
                  onPressed: _isLoading ? null : _login,
                  child: Text(
                    _useOTP
                        ? (_otpSent ? 'Verify OTP' : 'Send OTP')
                        : 'Login',
                    style: const TextStyle(fontSize: 16),
                  ),
                ),

                const SizedBox(height: 16),

                // Register
                OutlinedButton(
                  onPressed: () => context.push('/register'),
                  child: const Text('New Agent? Register Here'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _LoginTypeChip extends StatelessWidget {
  final String label;
  final bool selected;
  final VoidCallback onTap;

  const _LoginTypeChip({
    required this.label,
    required this.selected,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 12),
        decoration: BoxDecoration(
          color: selected ? AppTheme.primaryColor : Colors.white,
          borderRadius: BorderRadius.circular(10),
          border: Border.all(
            color: selected ? AppTheme.primaryColor : const Color(0xFFE0E0E0),
          ),
        ),
        child: Text(
          label,
          textAlign: TextAlign.center,
          style: TextStyle(
            color: selected ? Colors.white : AppTheme.textSecondary,
            fontWeight: FontWeight.w600,
          ),
        ),
      ),
    );
  }
}
