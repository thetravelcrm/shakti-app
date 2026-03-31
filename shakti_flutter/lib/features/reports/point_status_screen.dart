import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';
import '../dashboard/widgets/stats_card.dart';

class PointStatusScreen extends ConsumerStatefulWidget {
  const PointStatusScreen({super.key});

  @override
  ConsumerState<PointStatusScreen> createState() => _PointStatusScreenState();
}

class _PointStatusScreenState extends ConsumerState<PointStatusScreen> {
  bool _isLoading = true;
  List<Map<String, dynamic>> _earnList = [];
  List<Map<String, dynamic>> _redeemList = [];
  Map<String, dynamic> _summary = {};

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.pointStatusReport);
      if (response.isSuccess && mounted) {
        setState(() {
          _earnList = response.earn ?? [];
          _redeemList = response.redeem ?? [];
          _summary = response.dashData ?? response.raw;
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final totalEarn = _summary['totalEarn']?.toString() ?? '0';
    final totalRedeem = _summary['totalRedeem']?.toString() ?? '0';
    final balance = _summary['balancePoints']?.toString() ?? '0';

    return Scaffold(
      appBar: AppBar(title: const Text('Point Status')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: RefreshIndicator(
          onRefresh: _load,
          child: SingleChildScrollView(
            physics: const AlwaysScrollableScrollPhysics(),
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Summary cards
                Row(
                  children: [
                    Expanded(
                      child: StatsCard(
                        label: 'Total Earned',
                        value: totalEarn,
                        icon: Icons.arrow_upward,
                        color: Colors.green.shade600,
                      ),
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      child: StatsCard(
                        label: 'Redeemed',
                        value: totalRedeem,
                        icon: Icons.arrow_downward,
                        color: Colors.orange.shade700,
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 10),
                StatsCard(
                  label: 'Balance Points',
                  value: balance,
                  icon: Icons.account_balance_wallet,
                  color: AppTheme.primaryColor,
                ),
                const SizedBox(height: 24),

                // Earn list
                if (_earnList.isNotEmpty) ...[
                  const Text(
                    'Points Earned',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 8),
                  ..._earnList.map((e) => Card(
                    margin: const EdgeInsets.only(bottom: 8),
                    child: ListTile(
                      leading: const Icon(Icons.arrow_upward, color: Colors.green),
                      title: Text(e['description']?.toString() ?? e['remarks']?.toString() ?? 'Earned'),
                      subtitle: Text(e['date']?.toString() ?? ''),
                      trailing: Text(
                        '+${e['points']?.toString() ?? '0'}',
                        style: const TextStyle(color: Colors.green, fontWeight: FontWeight.bold),
                      ),
                    ),
                  )),
                  const SizedBox(height: 20),
                ],

                // Redeem list
                if (_redeemList.isNotEmpty) ...[
                  const Text(
                    'Points Redeemed',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 8),
                  ..._redeemList.map((r) => Card(
                    margin: const EdgeInsets.only(bottom: 8),
                    child: ListTile(
                      leading: const Icon(Icons.arrow_downward, color: Colors.orange),
                      title: Text(r['description']?.toString() ?? r['rewardName']?.toString() ?? 'Redeemed'),
                      subtitle: Text(r['date']?.toString() ?? ''),
                      trailing: Text(
                        '-${r['points']?.toString() ?? '0'}',
                        style: const TextStyle(color: Colors.orange, fontWeight: FontWeight.bold),
                      ),
                    ),
                  )),
                ],

                if (_earnList.isEmpty && _redeemList.isEmpty && !_isLoading)
                  const Center(
                    child: Padding(
                      padding: EdgeInsets.only(top: 40),
                      child: Text('No point transactions yet', style: TextStyle(color: AppTheme.textSecondary)),
                    ),
                  ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
