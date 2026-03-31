import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:url_launcher/url_launcher.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class VideosScreen extends ConsumerStatefulWidget {
  const VideosScreen({super.key});

  @override
  ConsumerState<VideosScreen> createState() => _VideosScreenState();
}

class _VideosScreenState extends ConsumerState<VideosScreen> {
  bool _isLoading = true;
  List<Map<String, dynamic>> _videos = [];

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.videos);
      if (response.isSuccess && mounted) {
        setState(() {
          _videos = response.data ?? response.list ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  Future<void> _launchUrl(String? url) async {
    if (url == null || url.isEmpty) return;
    final uri = Uri.tryParse(url);
    if (uri != null && await canLaunchUrl(uri)) {
      await launchUrl(uri, mode: LaunchMode.externalApplication);
    } else if (mounted) {
      showErrorSnackbar(context, 'Could not open video');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Videos')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _videos.isEmpty && !_isLoading
            ? const Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.play_circle_outline, size: 64, color: AppTheme.textSecondary),
                    SizedBox(height: 12),
                    Text('No videos available', style: TextStyle(color: AppTheme.textSecondary)),
                  ],
                ),
              )
            : RefreshIndicator(
                onRefresh: _load,
                child: ListView.builder(
                  padding: const EdgeInsets.all(12),
                  itemCount: _videos.length,
                  itemBuilder: (context, index) {
                    final video = _videos[index];
                    final title = video['title']?.toString() ??
                        video['videoTitle']?.toString() ?? 'Video';
                    final thumbnail = video['thumbnail']?.toString() ??
                        video['videoThumbnail']?.toString() ??
                        video['image']?.toString() ?? '';
                    final url = video['url']?.toString() ??
                        video['videoUrl']?.toString() ??
                        video['link']?.toString() ?? '';
                    final description = video['description']?.toString() ?? '';

                    return Card(
                      clipBehavior: Clip.antiAlias,
                      margin: const EdgeInsets.only(bottom: 12),
                      child: InkWell(
                        onTap: () => _launchUrl(url),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            if (thumbnail.isNotEmpty)
                              Stack(
                                alignment: Alignment.center,
                                children: [
                                  SizedBox(
                                    height: 180,
                                    width: double.infinity,
                                    child: Image.network(
                                      thumbnail,
                                      fit: BoxFit.cover,
                                      errorBuilder: (_, __, ___) => Container(
                                        color: Colors.grey.shade200,
                                        child: const Center(
                                          child: Icon(Icons.play_circle, size: 48, color: AppTheme.textSecondary),
                                        ),
                                      ),
                                    ),
                                  ),
                                  Container(
                                    padding: const EdgeInsets.all(12),
                                    decoration: BoxDecoration(
                                      color: Colors.black54,
                                      borderRadius: BorderRadius.circular(50),
                                    ),
                                    child: const Icon(Icons.play_arrow, color: Colors.white, size: 32),
                                  ),
                                ],
                              )
                            else
                              Container(
                                height: 120,
                                color: Colors.grey.shade200,
                                child: const Center(
                                  child: Icon(Icons.play_circle, size: 48, color: AppTheme.textSecondary),
                                ),
                              ),
                            Padding(
                              padding: const EdgeInsets.all(14),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    title,
                                    style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
                                  ),
                                  if (description.isNotEmpty) ...[
                                    const SizedBox(height: 4),
                                    Text(
                                      description,
                                      maxLines: 2,
                                      overflow: TextOverflow.ellipsis,
                                      style: const TextStyle(color: AppTheme.textSecondary, fontSize: 13),
                                    ),
                                  ],
                                ],
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
