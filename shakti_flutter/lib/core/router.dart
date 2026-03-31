import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'auth/auth_provider.dart';
import '../features/splash/splash_screen.dart';
import '../features/auth/login_screen.dart';
import '../features/dashboard/dashboard_screen.dart';
import '../features/profile/view_profile_screen.dart';
import '../features/profile/edit_profile_screen.dart';
import '../features/profile/kyc_update_screen.dart';
import '../features/auth/change_password_screen.dart';
import '../features/purchase/submit_purchase_screen.dart';
import '../features/sites/site_list_screen.dart';
import '../features/rewards/reward_list_screen.dart';
import '../features/rewards/reward_redeem_screen.dart';
import '../features/reports/reports_hub_screen.dart';
import '../features/reports/purchase_report_screen.dart';
import '../features/reports/redemption_report_screen.dart';
import '../features/reports/earn_point_report_screen.dart';
import '../features/reports/point_status_screen.dart';
import '../features/reports/dealer_report_screen.dart';
import '../features/reports/site_report_screen.dart';
import '../features/feedback/feedback_screen.dart';
import '../features/content/videos_screen.dart';
import '../features/content/faq_screen.dart';
import '../features/content/events_screen.dart';
import '../features/notifications/notification_list_screen.dart';
import '../features/settings/terms_screen.dart';
import '../features/auth/register_screen.dart';

final routerProvider = Provider<GoRouter>((ref) {
  final auth = ref.watch(authProvider);

  return GoRouter(
    initialLocation: '/splash',
    routes: [
      GoRoute(path: '/splash', builder: (_, __) => const SplashScreen()),
      GoRoute(path: '/login', builder: (_, __) => const LoginScreen()),
      GoRoute(path: '/register', builder: (_, __) => const RegisterScreen()),
      GoRoute(path: '/dashboard', builder: (_, __) => const DashboardScreen()),
      GoRoute(path: '/profile', builder: (_, __) => const ViewProfileScreen()),
      GoRoute(path: '/profile/edit', builder: (_, __) => const EditProfileScreen()),
      GoRoute(path: '/profile/kyc', builder: (_, __) => const KycUpdateScreen()),
      GoRoute(path: '/change-password', builder: (_, __) => const ChangePasswordScreen()),
      GoRoute(path: '/purchase/new', builder: (_, __) => const SubmitPurchaseScreen()),
      GoRoute(path: '/sites', builder: (_, __) => const SiteListScreen()),
      GoRoute(path: '/rewards', builder: (_, __) => const RewardListScreen()),
      GoRoute(
        path: '/rewards/redeem',
        builder: (_, state) => RewardRedeemScreen(
          reward: state.extra as Map<String, dynamic>? ?? {},
        ),
      ),
      GoRoute(path: '/reports', builder: (_, __) => const ReportsHubScreen()),
      GoRoute(path: '/reports/purchase', builder: (_, __) => const PurchaseReportScreen()),
      GoRoute(path: '/reports/redemption', builder: (_, __) => const RedemptionReportScreen()),
      GoRoute(path: '/reports/earn-points', builder: (_, __) => const EarnPointReportScreen()),
      GoRoute(path: '/reports/point-status', builder: (_, __) => const PointStatusScreen()),
      GoRoute(path: '/reports/dealers', builder: (_, __) => const DealerReportScreen()),
      GoRoute(path: '/reports/sites', builder: (_, __) => const SiteReportScreen()),
      GoRoute(path: '/feedback', builder: (_, __) => const FeedbackScreen()),
      GoRoute(path: '/videos', builder: (_, __) => const VideosScreen()),
      GoRoute(path: '/faq', builder: (_, __) => const FaqScreen()),
      GoRoute(path: '/events', builder: (_, __) => const EventsScreen()),
      GoRoute(path: '/notifications', builder: (_, __) => const NotificationListScreen()),
      GoRoute(
        path: '/terms',
        builder: (_, state) => TermsScreen(
          type: state.extra as String? ?? 'terms',
        ),
      ),
    ],
  );
});
