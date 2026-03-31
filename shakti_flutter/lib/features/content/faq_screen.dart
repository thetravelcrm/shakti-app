import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class FaqScreen extends ConsumerStatefulWidget {
  const FaqScreen({super.key});

  @override
  ConsumerState<FaqScreen> createState() => _FaqScreenState();
}

class _FaqScreenState extends ConsumerState<FaqScreen> {
  bool _isLoading = true;
  List<Map<String, dynamic>> _faqs = [];

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.faq);
      if (response.isSuccess && mounted) {
        setState(() {
          _faqs = response.data ?? response.list ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('FAQ')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _faqs.isEmpty && !_isLoading
            ? const Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.help_outline, size: 64, color: AppTheme.textSecondary),
                    SizedBox(height: 12),
                    Text('No FAQs available', style: TextStyle(color: AppTheme.textSecondary)),
                  ],
                ),
              )
            : RefreshIndicator(
                onRefresh: _load,
                child: SingleChildScrollView(
                  physics: const AlwaysScrollableScrollPhysics(),
                  padding: const EdgeInsets.all(12),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Padding(
                        padding: EdgeInsets.symmetric(horizontal: 4, vertical: 8),
                        child: Text(
                          'Frequently Asked Questions',
                          style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                        ),
                      ),
                      const SizedBox(height: 8),
                      ..._faqs.asMap().entries.map((entry) {
                        final faq = entry.value;
                        final question = faq['question']?.toString() ??
                            faq['title']?.toString() ?? 'Question';
                        final answer = faq['answer']?.toString() ??
                            faq['description']?.toString() ?? '';

                        return Card(
                          margin: const EdgeInsets.only(bottom: 8),
                          clipBehavior: Clip.antiAlias,
                          child: ExpansionTile(
                            tilePadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
                            childrenPadding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
                            leading: CircleAvatar(
                              radius: 16,
                              backgroundColor: AppTheme.primaryColor.withOpacity(0.1),
                              child: Text(
                                '${entry.key + 1}',
                                style: const TextStyle(
                                  color: AppTheme.primaryColor,
                                  fontWeight: FontWeight.bold,
                                  fontSize: 13,
                                ),
                              ),
                            ),
                            title: Text(
                              question,
                              style: const TextStyle(fontWeight: FontWeight.w600, fontSize: 14),
                            ),
                            children: [
                              const Divider(),
                              const SizedBox(height: 4),
                              Text(
                                answer,
                                style: const TextStyle(
                                  color: AppTheme.textSecondary,
                                  height: 1.5,
                                ),
                              ),
                            ],
                          ),
                        );
                      }),
                    ],
                  ),
                ),
              ),
      ),
    );
  }
}
