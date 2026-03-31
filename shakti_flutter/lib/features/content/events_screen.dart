import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class EventsScreen extends ConsumerStatefulWidget {
  const EventsScreen({super.key});

  @override
  ConsumerState<EventsScreen> createState() => _EventsScreenState();
}

class _EventsScreenState extends ConsumerState<EventsScreen> {
  bool _isLoading = true;
  List<Map<String, dynamic>> _events = [];

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.events);
      if (response.isSuccess && mounted) {
        setState(() {
          _events = response.data ?? response.list ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Events')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _events.isEmpty && !_isLoading
            ? const Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.event_busy, size: 64, color: AppTheme.textSecondary),
                    SizedBox(height: 12),
                    Text('No events available', style: TextStyle(color: AppTheme.textSecondary)),
                  ],
                ),
              )
            : RefreshIndicator(
                onRefresh: _load,
                child: ListView.builder(
                  padding: const EdgeInsets.all(12),
                  itemCount: _events.length,
                  itemBuilder: (context, index) {
                    final event = _events[index];
                    final title = event['title']?.toString() ??
                        event['eventTitle']?.toString() ?? 'Event';
                    final date = event['date']?.toString() ??
                        event['eventDate']?.toString() ?? '';
                    final description = event['description']?.toString() ??
                        event['eventDescription']?.toString() ?? '';
                    final image = event['image']?.toString() ??
                        event['eventImage']?.toString() ?? '';
                    final venue = event['venue']?.toString() ??
                        event['location']?.toString() ?? '';

                    return Card(
                      clipBehavior: Clip.antiAlias,
                      margin: const EdgeInsets.only(bottom: 12),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          if (image.isNotEmpty)
                            SizedBox(
                              height: 160,
                              width: double.infinity,
                              child: Image.network(
                                image,
                                fit: BoxFit.cover,
                                errorBuilder: (_, __, ___) => Container(
                                  color: Colors.grey.shade100,
                                  child: const Center(
                                    child: Icon(Icons.event, size: 48, color: AppTheme.textSecondary),
                                  ),
                                ),
                              ),
                            ),
                          Padding(
                            padding: const EdgeInsets.all(14),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  title,
                                  style: const TextStyle(fontSize: 17, fontWeight: FontWeight.bold),
                                ),
                                const SizedBox(height: 8),
                                if (date.isNotEmpty)
                                  Row(
                                    children: [
                                      const Icon(Icons.calendar_today, size: 14, color: AppTheme.primaryColor),
                                      const SizedBox(width: 6),
                                      Text(date, style: const TextStyle(color: AppTheme.primaryColor, fontSize: 13, fontWeight: FontWeight.w500)),
                                    ],
                                  ),
                                if (venue.isNotEmpty) ...[
                                  const SizedBox(height: 4),
                                  Row(
                                    children: [
                                      const Icon(Icons.location_on, size: 14, color: AppTheme.textSecondary),
                                      const SizedBox(width: 6),
                                      Expanded(
                                        child: Text(venue, style: const TextStyle(color: AppTheme.textSecondary, fontSize: 13)),
                                      ),
                                    ],
                                  ),
                                ],
                                if (description.isNotEmpty) ...[
                                  const SizedBox(height: 10),
                                  Text(
                                    description,
                                    style: const TextStyle(color: AppTheme.textSecondary, height: 1.4),
                                  ),
                                ],
                              ],
                            ),
                          ),
                        ],
                      ),
                    );
                  },
                ),
              ),
      ),
    );
  }
}
