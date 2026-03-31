import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class EditProfileScreen extends ConsumerStatefulWidget {
  const EditProfileScreen({super.key});

  @override
  ConsumerState<EditProfileScreen> createState() => _EditProfileScreenState();
}

class _EditProfileScreenState extends ConsumerState<EditProfileScreen> {
  final _formKey = GlobalKey<FormState>();
  bool _isLoading = false;

  final _fNameCtrl = TextEditingController();
  final _lNameCtrl = TextEditingController();
  final _mobileCtrl = TextEditingController();
  final _whatsappCtrl = TextEditingController();
  final _emailCtrl = TextEditingController();
  final _dobCtrl = TextEditingController();
  final _addressCtrl = TextEditingController();
  final _pinCtrl = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadCurrentProfile();
  }

  @override
  void dispose() {
    _fNameCtrl.dispose();
    _lNameCtrl.dispose();
    _mobileCtrl.dispose();
    _whatsappCtrl.dispose();
    _emailCtrl.dispose();
    _dobCtrl.dispose();
    _addressCtrl.dispose();
    _pinCtrl.dispose();
    super.dispose();
  }

  Future<void> _loadCurrentProfile() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.userDetails);
      if (response.isSuccess && mounted) {
        final data = response.data;
        final profile = (data != null && data.isNotEmpty) ? data.first : response.raw;
        _fNameCtrl.text = profile['fName']?.toString() ?? '';
        _lNameCtrl.text = profile['lName']?.toString() ?? '';
        _mobileCtrl.text = profile['mobileNo']?.toString() ?? '';
        _whatsappCtrl.text = profile['whatsappNo']?.toString() ?? '';
        _emailCtrl.text = profile['emailID']?.toString() ?? '';
        _dobCtrl.text = profile['dob']?.toString() ?? '';
        _addressCtrl.text = profile['officeAddress']?.toString() ?? '';
        _pinCtrl.text = profile['officePINCODE']?.toString() ?? '';
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  Future<void> _pickDate() async {
    final date = await showDatePicker(
      context: context,
      initialDate: DateTime(1990),
      firstDate: DateTime(1950),
      lastDate: DateTime.now(),
    );
    if (date != null) {
      _dobCtrl.text = '${date.day.toString().padLeft(2, '0')}/${date.month.toString().padLeft(2, '0')}/${date.year}';
    }
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.updateProfile, {
        'fName': _fNameCtrl.text.trim(),
        'lName': _lNameCtrl.text.trim(),
        'mobileNo': _mobileCtrl.text.trim(),
        'whatsappNo': _whatsappCtrl.text.trim(),
        'emailID': _emailCtrl.text.trim(),
        'dob': _dobCtrl.text.trim(),
        'officeAddress': _addressCtrl.text.trim(),
        'officePINCODE': _pinCtrl.text.trim(),
      });
      if (mounted) {
        if (response.isSuccess) {
          showSuccessSnackbar(context, response.msg ?? 'Profile updated');
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
      appBar: AppBar(title: const Text('Edit Profile')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Form(
            key: _formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                const Text(
                  'Update your profile information',
                  style: TextStyle(color: AppTheme.textSecondary, fontSize: 14),
                ),
                const SizedBox(height: 20),
                TextFormField(
                  controller: _fNameCtrl,
                  decoration: const InputDecoration(
                    labelText: 'First Name',
                    prefixIcon: Icon(Icons.person),
                  ),
                  validator: (v) => v == null || v.trim().isEmpty ? 'Required' : null,
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _lNameCtrl,
                  decoration: const InputDecoration(
                    labelText: 'Last Name',
                    prefixIcon: Icon(Icons.person_outline),
                  ),
                  validator: (v) => v == null || v.trim().isEmpty ? 'Required' : null,
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _mobileCtrl,
                  decoration: const InputDecoration(
                    labelText: 'Mobile Number',
                    prefixIcon: Icon(Icons.phone),
                  ),
                  keyboardType: TextInputType.phone,
                  enabled: false,
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _whatsappCtrl,
                  decoration: const InputDecoration(
                    labelText: 'WhatsApp Number',
                    prefixIcon: Icon(Icons.chat),
                  ),
                  keyboardType: TextInputType.phone,
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _emailCtrl,
                  decoration: const InputDecoration(
                    labelText: 'Email ID',
                    prefixIcon: Icon(Icons.email),
                  ),
                  keyboardType: TextInputType.emailAddress,
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _dobCtrl,
                  readOnly: true,
                  onTap: _pickDate,
                  decoration: const InputDecoration(
                    labelText: 'Date of Birth',
                    prefixIcon: Icon(Icons.calendar_today),
                  ),
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _addressCtrl,
                  maxLines: 2,
                  decoration: const InputDecoration(
                    labelText: 'Office Address',
                    prefixIcon: Icon(Icons.location_on),
                    alignLabelWithHint: true,
                  ),
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _pinCtrl,
                  decoration: const InputDecoration(
                    labelText: 'PIN Code',
                    prefixIcon: Icon(Icons.pin_drop),
                  ),
                  keyboardType: TextInputType.number,
                  maxLength: 6,
                ),
                const SizedBox(height: 24),
                ElevatedButton.icon(
                  onPressed: _submit,
                  icon: const Icon(Icons.save),
                  label: const Text('Update Profile'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
