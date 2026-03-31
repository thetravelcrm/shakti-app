import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';
import 'reward_redeem_screen.dart';

class RewardListScreen extends ConsumerStatefulWidget {
  const RewardListScreen({super.key});

  @override
  ConsumerState<RewardListScreen> createState() => _RewardListScreenState();
}

class _RewardListScreenState extends ConsumerState<RewardListScreen> {
  bool _isLoading = true;
  List<Map<String, dynamic>> _rewards = [];

  @override
  void initState() {
    super.initState();
    _loadRewards();
  }

  Future<void> _loadRewards() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.rewardList);
      if (response.isSuccess && mounted) {
        setState(() {
          _rewards = response.data ?? response.list ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Rewards')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _rewards.isEmpty && !_isLoading
            ? const Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.card_giftcard, size: 64, color: AppTheme.textSecondary),
                    SizedBox(height: 12),
                    Text('No rewards available', style: TextStyle(color: AppTheme.textSecondary, fontSize: 16)),
                  ],
                ),
              )
            : RefreshIndicator(
                onRefresh: _loadRewards,
                child: GridView.builder(
                  padding: const EdgeInsets.all(12),
                  gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 2,
                    mainAxisSpacing: 12,
                    crossAxisSpacing: 12,
                    childAspectRatio: 0.72,
                  ),
                  itemCount: _rewards.length,
                  itemBuilder: (context, index) {
                    final reward = _rewards[index];
                    final imageUrl = reward['image']?.toString() ??
                        reward['rewardImage']?.toString() ?? '';
                    final name = reward['rewardName']?.toString() ??
                        reward['name']?.toString() ?? 'Reward';
                    final points = reward['points']?.toString() ??
                        reward['rewardPoints']?.toString() ?? '0';

                    return GestureDetector(
                      onTap: () => Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => RewardRedeemScreen(reward: reward),
                        ),
                      ),
                      child: Card(
                        clipBehavior: Clip.antiAlias,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Expanded(
                              flex: 3,
                              child: Container(
                                width: double.infinity,
                                color: Colors.grey.shade100,
                                child: imageUrl.isNotEmpty
                                    ? Image.network(
                                        imageUrl,
                                        fit: BoxFit.cover,
                                        errorBuilder: (_, __, ___) =>
                                            const Center(child: Icon(Icons.card_giftcard, size: 40, color: AppTheme.textSecondary)),
                                      )
                                    : const Center(child: Icon(Icons.card_giftcard, size: 40, color: AppTheme.textSecondary)),
                              ),
                            ),
                            Expanded(
                              flex: 2,
                              child: Padding(
                                padding: const EdgeInsets.all(10),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      name,
                                      maxLines: 2,
                                      overflow: TextOverflow.ellipsis,
                                      style: const TextStyle(
                                        fontWeight: FontWeight.w600,
                                        fontSize: 13,
                                      ),
                                    ),
                                    const Spacer(),
                                    Container(
                                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                                      decoration: BoxDecoration(
                                        color: AppTheme.primaryColor.withOpacity(0.1),
                                        borderRadius: BorderRadius.circular(8),
                                      ),
                                      child: Text(
                                        '$points pts',
                                        style: const TextStyle(
                                          color: AppTheme.primaryColor,
                                          fontWeight: FontWeight.bold,
                                          fontSize: 12,
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ),
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
}
