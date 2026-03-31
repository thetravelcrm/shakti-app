import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class TermsScreen extends ConsumerStatefulWidget {
  final String type;

  const TermsScreen({super.key, required this.type});

  @override
  ConsumerState<TermsScreen> createState() => _TermsScreenState();
}

class _TermsScreenState extends ConsumerState<TermsScreen> {
  bool _isLoading = true;
  String _content = '';
  String _pageTitle = '';

  @override
  void initState() {
    super.initState();
    _resolveTitle();
    _load();
  }

  void _resolveTitle() {
    switch (widget.type) {
      case 'terms':
        _pageTitle = 'Terms & Conditions';
        break;
      case 'privacy':
        _pageTitle = 'Privacy Policy';
        break;
      case 'contact':
        _pageTitle = 'Contact Us';
        break;
      default:
        _pageTitle = 'Information';
    }
  }

  String get _endpoint {
    switch (widget.type) {
      case 'terms':
        return ApiEndpoints.termsCondition;
      case 'privacy':
        return ApiEndpoints.privacyPolicy;
      case 'contact':
        return ApiEndpoints.contactUs;
      default:
        return ApiEndpoints.termsCondition;
    }
  }

  Future<void> _load() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(_endpoint);
      if (response.isSuccess && mounted) {
        // Try multiple common field names for HTML content
        final data = response.data;
        String content = '';
        if (data != null && data.isNotEmpty) {
          final first = data.first;
          content = first['content']?.toString() ??
              first['htmlContent']?.toString() ??
              first['description']?.toString() ??
              first['body']?.toString() ??
              first['text']?.toString() ?? '';
        }
        if (content.isEmpty) {
          content = response.raw['content']?.toString() ??
              response.raw['htmlContent']?.toString() ??
              response.raw['description']?.toString() ??
              response.msg ?? '';
        }
        setState(() => _content = content);
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(_pageTitle)),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: _content.isEmpty && !_isLoading
            ? Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    const Icon(Icons.description_outlined, size: 64, color: AppTheme.textSecondary),
                    const SizedBox(height: 12),
                    Text('No content available',
                        style: const TextStyle(color: AppTheme.textSecondary)),
                  ],
                ),
              )
            : SingleChildScrollView(
                padding: const EdgeInsets.all(16),
                child: Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          _pageTitle,
                          style: const TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                            color: AppTheme.primaryColor,
                          ),
                        ),
                        const Divider(height: 24),
                        // Render the content as-is (strip basic HTML tags for readability)
                        SelectableText(
                          _stripHtml(_content),
                          style: const TextStyle(
                            fontSize: 14,
                            height: 1.6,
                            color: AppTheme.textPrimary,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
      ),
    );
  }

  /// Basic HTML tag removal for plain text display
  String _stripHtml(String html) {
    return html
        .replaceAll(RegExp(r'<br\s*/?>'), '\n')
        .replaceAll(RegExp(r'<p[^>]*>'), '\n')
        .replaceAll(RegExp(r'</p>'), '\n')
        .replaceAll(RegExp(r'<li[^>]*>'), '\n - ')
        .replaceAll(RegExp(r'<[^>]+>'), '')
        .replaceAll(RegExp(r'&nbsp;'), ' ')
        .replaceAll(RegExp(r'&amp;'), '&')
        .replaceAll(RegExp(r'&lt;'), '<')
        .replaceAll(RegExp(r'&gt;'), '>')
        .replaceAll(RegExp(r'&quot;'), '"')
        .replaceAll(RegExp(r'\n{3,}'), '\n\n')
        .trim();
  }
}
