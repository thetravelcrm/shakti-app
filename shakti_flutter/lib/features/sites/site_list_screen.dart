import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class SiteListScreen extends ConsumerStatefulWidget {
  const SiteListScreen({super.key});

  @override
  ConsumerState<SiteListScreen> createState() => _SiteListScreenState();
}

class _SiteListScreenState extends ConsumerState<SiteListScreen> {
  bool _isLoading = true;
  List<Map<String, dynamic>> _sites = [];

  @override
  void initState() {
    super.initState();
    _loadSites();
  }

  Future<void> _loadSites() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.siteReport);
      if (response.isSuccess && mounted) {
        setState(() {
          _sites = response.siteList ?? response.data ?? response.list ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  Color _statusColor(String? status) {
    if (status == null) return Colors.grey;
    final s = status.toLowerCase();
    if (s.contains('verified') || s.contains('approved')) return Colors.green;
    if (s.contains('pending')) return Colors.orange;
    if (s.contains('rejected')) return Colors.red;
    return Colors.grey;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('My Sites')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _sites.isEmpty && !_isLoading
            ? const Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.location_off, size: 64, color: AppTheme.textSecondary),
                    SizedBox(height: 12),
                    Text('No sites found', style: TextStyle(color: AppTheme.textSecondary, fontSize: 16)),
                  ],
                ),
              )
            : RefreshIndicator(
                onRefresh: _loadSites,
                child: ListView.builder(
                  padding: const EdgeInsets.all(12),
                  itemCount: _sites.length,
                  itemBuilder: (context, index) {
                    final site = _sites[index];
                    final status = site['siteStatus']?.toString() ?? site['status']?.toString() ?? '';
                    return Card(
                      margin: const EdgeInsets.only(bottom: 10),
                      child: Padding(
                        padding: const EdgeInsets.all(14),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              children: [
                                Container(
                                  padding: const EdgeInsets.all(8),
                                  decoration: BoxDecoration(
                                    color: AppTheme.primaryColor.withOpacity(0.1),
                                    borderRadius: BorderRadius.circular(8),
                                  ),
                                  child: const Icon(Icons.location_on, color: AppTheme.primaryColor),
                                ),
                                const SizedBox(width: 12),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        site['siteName']?.toString() ?? 'Site',
                                        style: const TextStyle(
                                          fontSize: 16,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                      const SizedBox(height: 2),
                                      Text(
                                        site['district']?.toString() ??
                                            site['districtName']?.toString() ?? '',
                                        style: const TextStyle(
                                          color: AppTheme.textSecondary,
                                          fontSize: 13,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                                Container(
                                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                                  decoration: BoxDecoration(
                                    color: _statusColor(status).withOpacity(0.1),
                                    borderRadius: BorderRadius.circular(12),
                                    border: Border.all(color: _statusColor(status).withOpacity(0.5)),
                                  ),
                                  child: Text(
                                    status,
                                    style: TextStyle(
                                      fontSize: 11,
                                      fontWeight: FontWeight.w600,
                                      color: _statusColor(status),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: 10),
                            Row(
                              children: [
                                _infoChip(Icons.home_work, site['siteType']?.toString() ?? site['siteTypeName']?.toString() ?? '-'),
                                const SizedBox(width: 12),
                                _infoChip(Icons.person, site['ownerName']?.toString() ?? '-'),
                              ],
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                ),
              ),
      ),
    );
  }

  Widget _infoChip(IconData icon, String text) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Icon(icon, size: 14, color: AppTheme.textSecondary),
        const SizedBox(width: 4),
        Text(text, style: const TextStyle(fontSize: 12, color: AppTheme.textSecondary)),
      ],
    );
  }
}
