import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class NotificationListScreen extends ConsumerStatefulWidget {
  const NotificationListScreen({super.key});

  @override
  ConsumerState<NotificationListScreen> createState() => _NotificationListScreenState();
}

class _NotificationListScreenState extends ConsumerState<NotificationListScreen> {
  bool _isLoading = true;
  List<Map<String, dynamic>> _notifications = [];

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.notificationList);
      if (response.isSuccess && mounted) {
        setState(() {
          _notifications = response.data ?? response.list ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  IconData _notifIcon(String? type) {
    if (type == null) return Icons.notifications;
    final t = type.toLowerCase();
    if (t.contains('purchase')) return Icons.shopping_cart;
    if (t.contains('reward') || t.contains('redeem')) return Icons.card_giftcard;
    if (t.contains('point')) return Icons.stars;
    if (t.contains('site')) return Icons.location_on;
    return Icons.notifications;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Notifications')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _notifications.isEmpty && !_isLoading
            ? const Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.notifications_off, size: 64, color: AppTheme.textSecondary),
                    SizedBox(height: 12),
                    Text('No notifications', style: TextStyle(color: AppTheme.textSecondary, fontSize: 16)),
                  ],
                ),
              )
            : RefreshIndicator(
                onRefresh: _load,
                child: ListView.builder(
                  padding: const EdgeInsets.all(12),
                  itemCount: _notifications.length,
                  itemBuilder: (context, index) {
                    final n = _notifications[index];
                    final title = n['title']?.toString() ??
                        n['notificationTitle']?.toString() ?? 'Notification';
                    final message = n['message']?.toString() ??
                        n['body']?.toString() ??
                        n['description']?.toString() ?? '';
                    final date = n['date']?.toString() ??
                        n['createdDate']?.toString() ?? '';
                    final type = n['type']?.toString() ??
                        n['notificationType']?.toString();
                    final isRead = n['isRead']?.toString() == '1' ||
                        n['isRead']?.toString() == 'true';

                    return Card(
                      margin: const EdgeInsets.only(bottom: 8),
                      color: isRead ? null : AppTheme.primaryColor.withOpacity(0.03),
                      child: ListTile(
                        contentPadding: const EdgeInsets.all(12),
                        leading: Container(
                          padding: const EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: (isRead ? Colors.grey : AppTheme.primaryColor).withOpacity(0.1),
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: Icon(
                            _notifIcon(type),
                            color: isRead ? Colors.grey : AppTheme.primaryColor,
                          ),
                        ),
                        title: Text(
                          title,
                          style: TextStyle(
                            fontWeight: isRead ? FontWeight.normal : FontWeight.w600,
                            fontSize: 14,
                          ),
                        ),
                        subtitle: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            if (message.isNotEmpty) ...[
                              const SizedBox(height: 4),
                              Text(
                                message,
                                maxLines: 2,
                                overflow: TextOverflow.ellipsis,
                                style: const TextStyle(fontSize: 13),
                              ),
                            ],
                            if (date.isNotEmpty) ...[
                              const SizedBox(height: 4),
                              Text(
                                date,
                                style: const TextStyle(fontSize: 11, color: AppTheme.textSecondary),
                              ),
                            ],
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
