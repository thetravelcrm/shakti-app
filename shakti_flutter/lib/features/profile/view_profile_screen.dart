import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class ViewProfileScreen extends ConsumerStatefulWidget {
  const ViewProfileScreen({super.key});

  @override
  ConsumerState<ViewProfileScreen> createState() => _ViewProfileScreenState();
}

class _ViewProfileScreenState extends ConsumerState<ViewProfileScreen> {
  bool _isLoading = true;
  Map<String, dynamic> _profile = {};

  @override
  void initState() {
    super.initState();
    _loadProfile();
  }

  Future<void> _loadProfile() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.userDetails);
      if (response.isSuccess && mounted) {
        final data = response.data;
        setState(() {
          _profile = (data != null && data.isNotEmpty) ? data.first : response.raw;
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('My Profile')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _profile.isEmpty
            ? const Center(child: Text('No profile data'))
            : RefreshIndicator(
                onRefresh: _loadProfile,
                child: SingleChildScrollView(
                  physics: const AlwaysScrollableScrollPhysics(),
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      // Avatar section
                      CircleAvatar(
                        radius: 50,
                        backgroundColor: AppTheme.primaryColor,
                        backgroundImage: _profile['photo'] != null &&
                                _profile['photo'].toString().isNotEmpty
                            ? NetworkImage(_profile['photo'].toString())
                            : null,
                        child: _profile['photo'] == null ||
                                _profile['photo'].toString().isEmpty
                            ? const Icon(Icons.person, size: 50, color: Colors.white)
                            : null,
                      ),
                      const SizedBox(height: 12),
                      Text(
                        _profile['name']?.toString() ?? _profile['fName']?.toString() ?? '',
                        style: const TextStyle(
                            fontSize: 22, fontWeight: FontWeight.bold),
                      ),
                      Text(
                        _profile['userCode']?.toString() ?? '',
                        style: const TextStyle(color: AppTheme.textSecondary),
                      ),
                      const SizedBox(height: 24),

                      // Info cards
                      _infoCard('Personal Information', [
                        _infoRow('First Name', _profile['fName']),
                        _infoRow('Last Name', _profile['lName']),
                        _infoRow('Date of Birth', _profile['dob']),
                        _infoRow('Email', _profile['emailID']),
                      ]),
                      const SizedBox(height: 12),
                      _infoCard('Contact Information', [
                        _infoRow('Mobile', _profile['mobileNo']),
                        _infoRow('WhatsApp', _profile['whatsappNo']),
                        _infoRow('Office Address', _profile['officeAddress']),
                        _infoRow('PIN Code', _profile['officePINCODE']),
                      ]),
                      const SizedBox(height: 12),
                      _infoCard('Location', [
                        _infoRow('State', _profile['stateName'] ?? _profile['state']),
                        _infoRow('District', _profile['districtName'] ?? _profile['district']),
                        _infoRow('Block', _profile['blockName'] ?? _profile['block']),
                      ]),
                      const SizedBox(height: 12),
                      _infoCard('KYC Details', [
                        _infoRow('Aadhaar', _profile['aadhaarNumber']),
                        _infoRow('PAN', _profile['panNumber']),
                        _infoRow('GST', _profile['gstNumber']),
                      ]),
                      const SizedBox(height: 12),
                      _infoCard('Account Details', [
                        _infoRow('User Code', _profile['userCode']),
                        _infoRow('Role', _profile['role'] ?? _profile['userRole']),
                        _infoRow('Agent Type', _profile['agentType']),
                        _infoRow('Dealer', _profile['dealerName'] ?? _profile['dealer']),
                      ]),
                    ],
                  ),
                ),
              ),
      ),
    );
  }

  Widget _infoCard(String title, List<Widget> children) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              title,
              style: const TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
                color: AppTheme.primaryColor,
              ),
            ),
            const Divider(),
            ...children,
          ],
        ),
      ),
    );
  }

  Widget _infoRow(String label, dynamic value) {
    final displayValue = value?.toString() ?? '-';
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 120,
            child: Text(
              label,
              style: const TextStyle(
                color: AppTheme.textSecondary,
                fontWeight: FontWeight.w500,
              ),
            ),
          ),
          Expanded(
            child: Text(
              displayValue.isEmpty ? '-' : displayValue,
              style: const TextStyle(fontWeight: FontWeight.w500),
            ),
          ),
        ],
      ),
    );
  }
}
