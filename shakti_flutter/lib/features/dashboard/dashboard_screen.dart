import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/api/api_response.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';
import 'widgets/stats_card.dart';
import 'widgets/quick_action_grid.dart';
import '../profile/view_profile_screen.dart';
import '../profile/edit_profile_screen.dart';
import '../profile/kyc_update_screen.dart';
import '../auth/change_password_screen.dart';
import '../purchase/submit_purchase_screen.dart';
import '../reports/reports_hub_screen.dart';
import '../sites/site_list_screen.dart';
import '../rewards/reward_list_screen.dart';
import '../reports/point_status_screen.dart';
import '../feedback/feedback_screen.dart';
import '../content/videos_screen.dart';
import '../notifications/notification_list_screen.dart';
import '../settings/terms_screen.dart';
import '../auth/login_screen.dart';

class DashboardScreen extends ConsumerStatefulWidget {
  const DashboardScreen({super.key});

  @override
  ConsumerState<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends ConsumerState<DashboardScreen> {
  int _currentTab = 0;
  bool _isLoading = false;

  // Dashboard data
  Map<String, dynamic>? _dashData;
  List<Map<String, dynamic>> _recentPurchases = [];
  List<Map<String, dynamic>> _recentSites = [];

  // Feedback form
  final _feedbackFormKey = GlobalKey<FormState>();
  String? _feedbackCategory;
  final _feedbackController = TextEditingController();
  List<Map<String, dynamic>> _feedbackCategories = [];

  @override
  void initState() {
    super.initState();
    _loadDashboard();
  }

  @override
  void dispose() {
    _feedbackController.dispose();
    super.dispose();
  }

  Future<void> _loadDashboard() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final role = ref.read(authProvider).role;

      String endpoint;
      switch (role) {
        case UserRole.dealer:
          endpoint = ApiEndpoints.dashBoardDealer;
          break;
        case UserRole.subAdmin:
          endpoint = ApiEndpoints.dashBoardSubAdmin;
          break;
        default:
          endpoint = ApiEndpoints.dashBoardAgent;
      }

      final response = await api.postAuth(endpoint);
      if (response.isSuccess && mounted) {
        setState(() {
          _dashData = response.dashData ?? response.raw;
          _recentPurchases = response.purchaseList ?? [];
          _recentSites = response.siteList ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  Future<void> _loadFeedbackCategories() async {
    if (_feedbackCategories.isNotEmpty) return;
    final api = ref.read(apiProvider);
    final response = await api.postAuth(ApiEndpoints.feedBackCategoryList);
    if (response.isSuccess && mounted) {
      setState(() {
        _feedbackCategories = response.data ?? response.list ?? [];
      });
    }
  }

  Future<void> _submitFeedback() async {
    if (!_feedbackFormKey.currentState!.validate()) return;
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.feedBack, {
        'category': _feedbackCategory ?? '',
        'description': _feedbackController.text.trim(),
      });
      if (mounted) {
        if (response.isSuccess) {
          showSuccessSnackbar(context, response.msg ?? 'Feedback submitted');
          _feedbackController.clear();
          setState(() => _feedbackCategory = null);
        } else {
          showErrorSnackbar(context, response.msg ?? 'Failed to submit');
        }
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  Future<void> _logout() async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Logout'),
        content: const Text('Are you sure you want to logout?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx, false), child: const Text('Cancel')),
          TextButton(onPressed: () => Navigator.pop(ctx, true), child: const Text('Logout')),
        ],
      ),
    );
    if (confirmed == true && mounted) {
      await ref.read(authProvider.notifier).logout();
      if (mounted) {
        Navigator.of(context).pushAndRemoveUntil(
          MaterialPageRoute(builder: (_) => const LoginScreen()),
          (_) => false,
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final auth = ref.watch(authProvider);

    return LoadingOverlay(
      isLoading: _isLoading,
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Shakti'),
          actions: [
            IconButton(
              icon: const Icon(Icons.notifications_outlined),
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const NotificationListScreen()),
              ),
            ),
          ],
        ),
        drawer: _buildDrawer(auth),
        body: IndexedStack(
          index: _currentTab,
          children: [
            _buildHomeTab(),
            _buildProfileTab(),
            _buildFeedbackTab(),
          ],
        ),
        bottomNavigationBar: BottomNavigationBar(
          currentIndex: _currentTab,
          onTap: (i) {
            setState(() => _currentTab = i);
            if (i == 2) _loadFeedbackCategories();
          },
          items: const [
            BottomNavigationBarItem(icon: Icon(Icons.home), label: 'Home'),
            BottomNavigationBarItem(icon: Icon(Icons.person), label: 'Profile'),
            BottomNavigationBarItem(icon: Icon(Icons.feedback), label: 'Feedback'),
          ],
        ),
      ),
    );
  }

  Widget _buildDrawer(AuthState auth) {
    return NavigationDrawer(
      children: [
        UserAccountsDrawerHeader(
          decoration: const BoxDecoration(color: AppTheme.primaryColor),
          accountName: Text(auth.name ?? 'User'),
          accountEmail: Text(auth.userCode ?? ''),
          currentAccountPicture: CircleAvatar(
            backgroundColor: Colors.white,
            backgroundImage: auth.photo != null && auth.photo!.isNotEmpty
                ? NetworkImage(auth.photo!)
                : null,
            child: auth.photo == null || auth.photo!.isEmpty
                ? Text(
                    (auth.name ?? 'U').substring(0, 1).toUpperCase(),
                    style: const TextStyle(fontSize: 28, color: AppTheme.primaryColor),
                  )
                : null,
          ),
        ),
        _drawerItem(Icons.account_circle, 'My Account', () {
          Navigator.pop(context);
          Navigator.push(context, MaterialPageRoute(builder: (_) => const ViewProfileScreen()));
        }),
        _drawerItem(Icons.play_circle, 'Videos', () {
          Navigator.pop(context);
          Navigator.push(context, MaterialPageRoute(builder: (_) => const VideosScreen()));
        }),
        _drawerItem(Icons.card_giftcard, 'Wish Karo', () {
          Navigator.pop(context);
        }),
        _drawerItem(Icons.engineering, 'Request Tech Express', () {
          Navigator.pop(context);
        }),
        _drawerItem(Icons.feedback, 'Feedback', () {
          Navigator.pop(context);
          setState(() => _currentTab = 2);
          _loadFeedbackCategories();
        }),
        _drawerItem(Icons.language, 'Language', () {
          Navigator.pop(context);
        }),
        _drawerItem(Icons.phone, 'Contact Us', () {
          Navigator.pop(context);
          Navigator.push(context, MaterialPageRoute(
            builder: (_) => const TermsScreen(type: 'contact'),
          ));
        }),
        _drawerItem(Icons.description, 'Terms & Conditions', () {
          Navigator.pop(context);
          Navigator.push(context, MaterialPageRoute(
            builder: (_) => const TermsScreen(type: 'terms'),
          ));
        }),
        _drawerItem(Icons.privacy_tip, 'Privacy Policy', () {
          Navigator.pop(context);
          Navigator.push(context, MaterialPageRoute(
            builder: (_) => const TermsScreen(type: 'privacy'),
          ));
        }),
        const Divider(),
        _drawerItem(Icons.logout, 'Logout', _logout),
      ],
    );
  }

  Widget _drawerItem(IconData icon, String title, VoidCallback onTap) {
    return ListTile(
      leading: Icon(icon, color: AppTheme.primaryColor),
      title: Text(title),
      onTap: onTap,
    );
  }

  Widget _buildHomeTab() {
    final earned = _dashData?['totalEarn']?.toString() ?? '0';
    final redeemed = _dashData?['totalRedeem']?.toString() ?? '0';
    final remaining = _dashData?['balancePoints']?.toString() ?? '0';

    return RefreshIndicator(
      onRefresh: _loadDashboard,
      child: SingleChildScrollView(
        physics: const AlwaysScrollableScrollPhysics(),
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Stats row
            Row(
              children: [
                Expanded(
                  child: StatsCard(
                    label: 'Earned',
                    value: earned,
                    icon: Icons.arrow_upward,
                    color: Colors.green.shade600,
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: StatsCard(
                    label: 'Redeemed',
                    value: redeemed,
                    icon: Icons.arrow_downward,
                    color: Colors.orange.shade700,
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: StatsCard(
                    label: 'Balance',
                    value: remaining,
                    icon: Icons.account_balance_wallet,
                    color: AppTheme.primaryColor,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 24),

            // Quick actions
            const Text(
              'Quick Actions',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 12),
            QuickActionGrid(
              items: [
                QuickActionItem(
                  label: 'Submit\nPurchase',
                  icon: Icons.add_shopping_cart,
                  color: Colors.blue,
                  onTap: () => Navigator.push(context,
                    MaterialPageRoute(builder: (_) => const SubmitPurchaseScreen())),
                ),
                QuickActionItem(
                  label: 'Reports',
                  icon: Icons.bar_chart,
                  color: Colors.teal,
                  onTap: () => Navigator.push(context,
                    MaterialPageRoute(builder: (_) => const ReportsHubScreen())),
                ),
                QuickActionItem(
                  label: 'My Sites',
                  icon: Icons.location_on,
                  color: Colors.purple,
                  onTap: () => Navigator.push(context,
                    MaterialPageRoute(builder: (_) => const SiteListScreen())),
                ),
                QuickActionItem(
                  label: 'Rewards',
                  icon: Icons.card_giftcard,
                  color: Colors.amber.shade700,
                  onTap: () => Navigator.push(context,
                    MaterialPageRoute(builder: (_) => const RewardListScreen())),
                ),
                QuickActionItem(
                  label: 'Point\nStatus',
                  icon: Icons.stars,
                  color: Colors.indigo,
                  onTap: () => Navigator.push(context,
                    MaterialPageRoute(builder: (_) => const PointStatusScreen())),
                ),
                QuickActionItem(
                  label: 'Notifications',
                  icon: Icons.notifications,
                  color: Colors.red.shade400,
                  onTap: () => Navigator.push(context,
                    MaterialPageRoute(builder: (_) => const NotificationListScreen())),
                ),
              ],
            ),
            const SizedBox(height: 24),

            // Recent purchases
            if (_recentPurchases.isNotEmpty) ...[
              const Text(
                'Recent Purchases',
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 8),
              ..._recentPurchases.take(5).map((p) => Card(
                margin: const EdgeInsets.only(bottom: 8),
                child: ListTile(
                  leading: const CircleAvatar(
                    backgroundColor: AppTheme.primaryColor,
                    child: Icon(Icons.shopping_bag, color: Colors.white, size: 20),
                  ),
                  title: Text(p['productName']?.toString() ?? p['product']?.toString() ?? 'Purchase'),
                  subtitle: Text(p['date']?.toString() ?? p['purchaseDate']?.toString() ?? ''),
                  trailing: Text(
                    '${p['quantity']?.toString() ?? '0'} qty',
                    style: const TextStyle(fontWeight: FontWeight.w600),
                  ),
                ),
              )),
            ],
            const SizedBox(height: 16),

            // Recent sites
            if (_recentSites.isNotEmpty) ...[
              const Text(
                'Recent Sites',
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 8),
              ..._recentSites.take(5).map((s) => Card(
                margin: const EdgeInsets.only(bottom: 8),
                child: ListTile(
                  leading: const CircleAvatar(
                    backgroundColor: Colors.purple,
                    child: Icon(Icons.location_on, color: Colors.white, size: 20),
                  ),
                  title: Text(s['siteName']?.toString() ?? 'Site'),
                  subtitle: Text(s['district']?.toString() ?? ''),
                  trailing: Chip(
                    label: Text(
                      s['siteStatus']?.toString() ?? '',
                      style: const TextStyle(fontSize: 11),
                    ),
                  ),
                ),
              )),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildProfileTab() {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          const SizedBox(height: 16),
          const CircleAvatar(
            radius: 48,
            backgroundColor: AppTheme.primaryColor,
            child: Icon(Icons.person, size: 48, color: Colors.white),
          ),
          const SizedBox(height: 12),
          Text(
            ref.watch(authProvider).name ?? 'User',
            style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
          ),
          Text(
            ref.watch(authProvider).userCode ?? '',
            style: const TextStyle(color: AppTheme.textSecondary),
          ),
          const SizedBox(height: 32),
          _profileButton(Icons.visibility, 'View Profile', () {
            Navigator.push(context, MaterialPageRoute(builder: (_) => const ViewProfileScreen()));
          }),
          _profileButton(Icons.edit, 'Edit Profile', () {
            Navigator.push(context, MaterialPageRoute(builder: (_) => const EditProfileScreen()));
          }),
          _profileButton(Icons.verified_user, 'KYC Update', () {
            Navigator.push(context, MaterialPageRoute(builder: (_) => const KycUpdateScreen()));
          }),
          _profileButton(Icons.lock, 'Change Password', () {
            Navigator.push(context, MaterialPageRoute(builder: (_) => const ChangePasswordScreen()));
          }),
          const SizedBox(height: 16),
          SizedBox(
            width: double.infinity,
            child: OutlinedButton.icon(
              onPressed: _logout,
              icon: const Icon(Icons.logout),
              label: const Text('Logout'),
              style: OutlinedButton.styleFrom(
                foregroundColor: Colors.red,
                side: const BorderSide(color: Colors.red),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _profileButton(IconData icon, String title, VoidCallback onTap) {
    return Card(
      margin: const EdgeInsets.only(bottom: 10),
      child: ListTile(
        leading: Icon(icon, color: AppTheme.primaryColor),
        title: Text(title),
        trailing: const Icon(Icons.chevron_right),
        onTap: onTap,
      ),
    );
  }

  Widget _buildFeedbackTab() {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Form(
        key: _feedbackFormKey,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Submit Feedback',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            const Text(
              'We value your feedback. Please share your thoughts with us.',
              style: TextStyle(color: AppTheme.textSecondary),
            ),
            const SizedBox(height: 24),
            DropdownButtonFormField<String>(
              value: _feedbackCategory,
              decoration: const InputDecoration(
                labelText: 'Category',
                prefixIcon: Icon(Icons.category),
              ),
              items: _feedbackCategories.map((c) {
                final id = c['id']?.toString() ?? c['categoryId']?.toString() ?? '';
                final name = c['name']?.toString() ?? c['categoryName']?.toString() ?? '';
                return DropdownMenuItem(value: id, child: Text(name));
              }).toList(),
              onChanged: (v) => setState(() => _feedbackCategory = v),
              validator: (v) => v == null || v.isEmpty ? 'Select a category' : null,
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _feedbackController,
              maxLines: 5,
              decoration: const InputDecoration(
                labelText: 'Description',
                hintText: 'Enter your feedback here...',
                alignLabelWithHint: true,
                prefixIcon: Padding(
                  padding: EdgeInsets.only(bottom: 80),
                  child: Icon(Icons.message),
                ),
              ),
              validator: (v) => v == null || v.trim().isEmpty ? 'Enter description' : null,
            ),
            const SizedBox(height: 24),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton.icon(
                onPressed: _submitFeedback,
                icon: const Icon(Icons.send),
                label: const Text('Submit Feedback'),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
