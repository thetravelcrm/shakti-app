import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class DealerReportScreen extends ConsumerStatefulWidget {
  const DealerReportScreen({super.key});

  @override
  ConsumerState<DealerReportScreen> createState() => _DealerReportScreenState();
}

class _DealerReportScreenState extends ConsumerState<DealerReportScreen> {
  bool _isLoading = true;
  List<Map<String, dynamic>> _dealers = [];

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.dealerReport);
      if (response.isSuccess && mounted) {
        setState(() {
          _dealers = response.data ?? response.list ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('My Dealers')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _dealers.isEmpty && !_isLoading
            ? const Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.store, size: 64, color: AppTheme.textSecondary),
                    SizedBox(height: 12),
                    Text('No dealers found', style: TextStyle(color: AppTheme.textSecondary)),
                  ],
                ),
              )
            : RefreshIndicator(
                onRefresh: _load,
                child: ListView.builder(
                  padding: const EdgeInsets.all(12),
                  itemCount: _dealers.length,
                  itemBuilder: (context, index) {
                    final d = _dealers[index];
                    return Card(
                      margin: const EdgeInsets.only(bottom: 10),
                      child: ListTile(
                        contentPadding: const EdgeInsets.all(14),
                        leading: CircleAvatar(
                          backgroundColor: Colors.teal.withOpacity(0.1),
                          child: const Icon(Icons.store, color: Colors.teal),
                        ),
                        title: Text(
                          d['dealerName']?.toString() ?? d['name']?.toString() ?? 'Dealer',
                          style: const TextStyle(fontWeight: FontWeight.w600),
                        ),
                        subtitle: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const SizedBox(height: 4),
                            if (d['mobileNo'] != null || d['mobile'] != null)
                              Text(
                                d['mobileNo']?.toString() ?? d['mobile']?.toString() ?? '',
                                style: const TextStyle(fontSize: 13),
                              ),
                            if (d['district'] != null || d['districtName'] != null)
                              Text(
                                d['district']?.toString() ?? d['districtName']?.toString() ?? '',
                                style: const TextStyle(fontSize: 13, color: AppTheme.textSecondary),
                              ),
                          ],
                        ),
                        trailing: d['dealerCode'] != null || d['userCode'] != null
                            ? Chip(
                                label: Text(
                                  d['dealerCode']?.toString() ?? d['userCode']?.toString() ?? '',
                                  style: const TextStyle(fontSize: 11),
                                ),
                              )
                            : null,
                      ),
                    );
                  },
                ),
              ),
      ),
    );
  }
}
