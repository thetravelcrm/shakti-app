import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class RedemptionReportScreen extends ConsumerStatefulWidget {
  const RedemptionReportScreen({super.key});

  @override
  ConsumerState<RedemptionReportScreen> createState() => _RedemptionReportScreenState();
}

class _RedemptionReportScreenState extends ConsumerState<RedemptionReportScreen> {
  bool _isLoading = true;
  List<Map<String, dynamic>> _redemptions = [];

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.redemptionReport);
      if (response.isSuccess && mounted) {
        setState(() {
          _redemptions = response.redemptionList ?? response.redeem ?? response.data ?? response.list ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Redemption History')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _redemptions.isEmpty && !_isLoading
            ? const Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.redeem, size: 64, color: AppTheme.textSecondary),
                    SizedBox(height: 12),
                    Text('No redemptions found', style: TextStyle(color: AppTheme.textSecondary)),
                  ],
                ),
              )
            : RefreshIndicator(
                onRefresh: _load,
                child: ListView.builder(
                  padding: const EdgeInsets.all(12),
                  itemCount: _redemptions.length,
                  itemBuilder: (context, index) {
                    final r = _redemptions[index];
                    return Card(
                      margin: const EdgeInsets.only(bottom: 10),
                      child: ListTile(
                        contentPadding: const EdgeInsets.all(14),
                        leading: Container(
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: Colors.orange.withOpacity(0.1),
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: const Icon(Icons.redeem, color: Colors.orange),
                        ),
                        title: Text(
                          r['rewardName']?.toString() ?? r['name']?.toString() ?? 'Reward',
                          style: const TextStyle(fontWeight: FontWeight.w600),
                        ),
                        subtitle: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const SizedBox(height: 4),
                            Text(
                              'Points: ${r['points']?.toString() ?? r['redeemPoints']?.toString() ?? '-'}',
                              style: const TextStyle(fontSize: 13),
                            ),
                            Text(
                              'Date: ${r['date']?.toString() ?? r['redeemDate']?.toString() ?? '-'}',
                              style: const TextStyle(fontSize: 13, color: AppTheme.textSecondary),
                            ),
                            if (r['status'] != null)
                              Text(
                                'Status: ${r['status']}',
                                style: const TextStyle(fontSize: 13),
                              ),
                          ],
                        ),
                        trailing: Text(
                          '${r['quantity']?.toString() ?? '1'}x',
                          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                        ),
                      ),
                    );
                  },
                ),
              ),
      ),
    );
  }
}
