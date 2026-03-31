import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class EarnPointReportScreen extends ConsumerStatefulWidget {
  const EarnPointReportScreen({super.key});

  @override
  ConsumerState<EarnPointReportScreen> createState() => _EarnPointReportScreenState();
}

class _EarnPointReportScreenState extends ConsumerState<EarnPointReportScreen> {
  bool _isLoading = true;
  List<Map<String, dynamic>> _earnData = [];

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.earnPointReport);
      if (response.isSuccess && mounted) {
        setState(() {
          _earnData = response.earn ?? response.data ?? response.list ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Earned Points')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _earnData.isEmpty && !_isLoading
            ? const Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.arrow_upward, size: 64, color: AppTheme.textSecondary),
                    SizedBox(height: 12),
                    Text('No earn records found', style: TextStyle(color: AppTheme.textSecondary)),
                  ],
                ),
              )
            : RefreshIndicator(
                onRefresh: _load,
                child: ListView.builder(
                  padding: const EdgeInsets.all(12),
                  itemCount: _earnData.length,
                  itemBuilder: (context, index) {
                    final item = _earnData[index];
                    return Card(
                      margin: const EdgeInsets.only(bottom: 10),
                      child: ListTile(
                        contentPadding: const EdgeInsets.all(14),
                        leading: Container(
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: Colors.green.withOpacity(0.1),
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: const Icon(Icons.arrow_upward, color: Colors.green),
                        ),
                        title: Text(
                          item['description']?.toString() ??
                              item['productName']?.toString() ??
                              item['remarks']?.toString() ?? 'Points Earned',
                          style: const TextStyle(fontWeight: FontWeight.w600),
                        ),
                        subtitle: Text(
                          item['date']?.toString() ??
                              item['earnDate']?.toString() ??
                              item['transactionDate']?.toString() ?? '',
                          style: const TextStyle(fontSize: 13, color: AppTheme.textSecondary),
                        ),
                        trailing: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                          decoration: BoxDecoration(
                            color: Colors.green.withOpacity(0.1),
                            borderRadius: BorderRadius.circular(20),
                          ),
                          child: Text(
                            '+${item['points']?.toString() ?? item['earnPoints']?.toString() ?? '0'}',
                            style: const TextStyle(
                              color: Colors.green,
                              fontWeight: FontWeight.bold,
                              fontSize: 14,
                            ),
                          ),
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
