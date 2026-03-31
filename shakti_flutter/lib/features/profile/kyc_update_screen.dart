import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class KycUpdateScreen extends ConsumerStatefulWidget {
  const KycUpdateScreen({super.key});

  @override
  ConsumerState<KycUpdateScreen> createState() => _KycUpdateScreenState();
}

class _KycUpdateScreenState extends ConsumerState<KycUpdateScreen> {
  final _formKey = GlobalKey<FormState>();
  bool _isLoading = false;

  final _aadhaarCtrl = TextEditingController();
  final _gstCtrl = TextEditingController();
  final _panCtrl = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadCurrentKyc();
  }

  @override
  void dispose() {
    _aadhaarCtrl.dispose();
    _gstCtrl.dispose();
    _panCtrl.dispose();
    super.dispose();
  }

  Future<void> _loadCurrentKyc() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.userDetails);
      if (response.isSuccess && mounted) {
        final data = response.data;
        final profile = (data != null && data.isNotEmpty) ? data.first : response.raw;
        _aadhaarCtrl.text = profile['aadhaarNumber']?.toString() ?? '';
        _gstCtrl.text = profile['gstNumber']?.toString() ?? '';
        _panCtrl.text = profile['panNumber']?.toString() ?? '';
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.updateKYC, {
        'aadhaarNumber': _aadhaarCtrl.text.trim(),
        'gstNumber': _gstCtrl.text.trim(),
        'panNumber': _panCtrl.text.trim(),
      });
      if (mounted) {
        if (response.isSuccess) {
          showSuccessSnackbar(context, response.msg ?? 'KYC updated');
          Navigator.pop(context, true);
        } else {
          showErrorSnackbar(context, response.msg ?? 'Update failed');
        }
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('KYC Update')),
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
                    child: Row(
                      children: [
                        Icon(Icons.info_outline, color: Colors.blue.shade700),
                        const SizedBox(width: 12),
                        const Expanded(
                          child: Text(
                            'Update your KYC documents for verification. All information is kept secure.',
                            style: TextStyle(color: AppTheme.textSecondary, fontSize: 13),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 24),
                TextFormField(
                  controller: _aadhaarCtrl,
                  decoration: const InputDecoration(
                    labelText: 'Aadhaar Number',
                    prefixIcon: Icon(Icons.credit_card),
                    hintText: 'XXXX XXXX XXXX',
                  ),
                  keyboardType: TextInputType.number,
                  maxLength: 12,
                  validator: (v) {
                    if (v != null && v.isNotEmpty && v.length != 12) {
                      return 'Aadhaar must be 12 digits';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _panCtrl,
                  decoration: const InputDecoration(
                    labelText: 'PAN Number',
                    prefixIcon: Icon(Icons.badge),
                    hintText: 'ABCDE1234F',
                  ),
                  textCapitalization: TextCapitalization.characters,
                  maxLength: 10,
                  validator: (v) {
                    if (v != null && v.isNotEmpty && v.length != 10) {
                      return 'PAN must be 10 characters';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _gstCtrl,
                  decoration: const InputDecoration(
                    labelText: 'GST Number',
                    prefixIcon: Icon(Icons.receipt_long),
                    hintText: 'Enter GST number',
                  ),
                  textCapitalization: TextCapitalization.characters,
                  maxLength: 15,
                ),
                const SizedBox(height: 24),
                ElevatedButton.icon(
                  onPressed: _submit,
                  icon: const Icon(Icons.verified),
                  label: const Text('Update KYC'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
