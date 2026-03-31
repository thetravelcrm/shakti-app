import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class RewardRedeemScreen extends ConsumerStatefulWidget {
  final Map<String, dynamic> reward;

  const RewardRedeemScreen({super.key, required this.reward});

  @override
  ConsumerState<RewardRedeemScreen> createState() => _RewardRedeemScreenState();
}

class _RewardRedeemScreenState extends ConsumerState<RewardRedeemScreen> {
  final _formKey = GlobalKey<FormState>();
  bool _isLoading = false;
  int _quantity = 1;

  final _addressCtrl = TextEditingController();
  final _cityCtrl = TextEditingController();
  final _pinCtrl = TextEditingController();
  final _phoneCtrl = TextEditingController();

  @override
  void dispose() {
    _addressCtrl.dispose();
    _cityCtrl.dispose();
    _pinCtrl.dispose();
    _phoneCtrl.dispose();
    super.dispose();
  }

  Future<void> _redeem() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final rewardId = widget.reward['rewardId']?.toString() ??
          widget.reward['id']?.toString() ?? '';
      final response = await api.postAuth(ApiEndpoints.rewardRedeem, {
        'rewardId': rewardId,
        'quantity': _quantity.toString(),
        'address': _addressCtrl.text.trim(),
        'city': _cityCtrl.text.trim(),
        'pincode': _pinCtrl.text.trim(),
        'mobile': _phoneCtrl.text.trim(),
      });
      if (mounted) {
        if (response.isSuccess) {
          showSuccessSnackbar(context, response.msg ?? 'Reward redeemed successfully');
          Navigator.pop(context, true);
        } else {
          showErrorSnackbar(context, response.msg ?? 'Redemption failed');
        }
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final reward = widget.reward;
    final name = reward['rewardName']?.toString() ?? reward['name']?.toString() ?? 'Reward';
    final points = reward['points']?.toString() ?? reward['rewardPoints']?.toString() ?? '0';
    final imageUrl = reward['image']?.toString() ?? reward['rewardImage']?.toString() ?? '';
    final description = reward['description']?.toString() ?? reward['rewardDescription']?.toString() ?? '';
    final pointsNum = int.tryParse(points) ?? 0;
    final totalPoints = pointsNum * _quantity;

    return Scaffold(
      appBar: AppBar(title: const Text('Redeem Reward')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Form(
            key: _formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                // Reward details card
                Card(
                  clipBehavior: Clip.antiAlias,
                  child: Column(
                    children: [
                      if (imageUrl.isNotEmpty)
                        SizedBox(
                          height: 200,
                          width: double.infinity,
                          child: Image.network(
                            imageUrl,
                            fit: BoxFit.cover,
                            errorBuilder: (_, __, ___) => Container(
                              color: Colors.grey.shade100,
                              child: const Center(child: Icon(Icons.card_giftcard, size: 64, color: AppTheme.textSecondary)),
                            ),
                          ),
                        ),
                      Padding(
                        padding: const EdgeInsets.all(16),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(name, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                            const SizedBox(height: 4),
                            Text('$points points per item',
                              style: const TextStyle(color: AppTheme.primaryColor, fontWeight: FontWeight.w600)),
                            if (description.isNotEmpty) ...[
                              const SizedBox(height: 8),
                              Text(description, style: const TextStyle(color: AppTheme.textSecondary)),
                            ],
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 20),

                // Quantity selector
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Row(
                      children: [
                        const Text('Quantity', style: TextStyle(fontSize: 16, fontWeight: FontWeight.w600)),
                        const Spacer(),
                        IconButton(
                          onPressed: _quantity > 1 ? () => setState(() => _quantity--) : null,
                          icon: const Icon(Icons.remove_circle_outline),
                          color: AppTheme.primaryColor,
                        ),
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                          decoration: BoxDecoration(
                            border: Border.all(color: Colors.grey.shade300),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Text('$_quantity', style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                        ),
                        IconButton(
                          onPressed: () => setState(() => _quantity++),
                          icon: const Icon(Icons.add_circle_outline),
                          color: AppTheme.primaryColor,
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 8),
                Card(
                  color: AppTheme.primaryColor.withOpacity(0.05),
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text('Total Points', style: TextStyle(fontSize: 16, fontWeight: FontWeight.w600)),
                        Text(
                          '$totalPoints pts',
                          style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: AppTheme.primaryColor),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 20),

                // Delivery address
                const Text('Delivery Address', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppTheme.primaryColor)),
                const SizedBox(height: 12),
                TextFormField(
                  controller: _addressCtrl,
                  maxLines: 2,
                  decoration: const InputDecoration(
                    labelText: 'Address',
                    prefixIcon: Icon(Icons.location_on),
                    alignLabelWithHint: true,
                  ),
                  validator: (v) => v == null || v.trim().isEmpty ? 'Required' : null,
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _cityCtrl,
                  decoration: const InputDecoration(
                    labelText: 'City',
                    prefixIcon: Icon(Icons.location_city),
                  ),
                  validator: (v) => v == null || v.trim().isEmpty ? 'Required' : null,
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
                  validator: (v) => v == null || v.trim().isEmpty ? 'Required' : null,
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _phoneCtrl,
                  decoration: const InputDecoration(
                    labelText: 'Contact Number',
                    prefixIcon: Icon(Icons.phone),
                  ),
                  keyboardType: TextInputType.phone,
                  maxLength: 10,
                  validator: (v) => v == null || v.trim().isEmpty ? 'Required' : null,
                ),
                const SizedBox(height: 24),
                ElevatedButton.icon(
                  onPressed: _redeem,
                  icon: const Icon(Icons.redeem),
                  label: const Text('Redeem Now'),
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 16),
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
