import 'package:flutter/material.dart';
import '../../core/theme.dart';
import 'purchase_report_screen.dart';
import 'redemption_report_screen.dart';
import 'earn_point_report_screen.dart';
import 'point_status_screen.dart';
import 'site_report_screen.dart';
import 'dealer_report_screen.dart';

class ReportsHubScreen extends StatelessWidget {
  const ReportsHubScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final reportItems = <_ReportItem>[
      _ReportItem(
        title: 'Purchase Status',
        icon: Icons.shopping_cart,
        color: Colors.blue,
        screen: const PurchaseReportScreen(),
      ),
      _ReportItem(
        title: 'Redemption History',
        icon: Icons.redeem,
        color: Colors.orange,
        screen: const RedemptionReportScreen(),
      ),
      _ReportItem(
        title: 'Earn Points',
        icon: Icons.arrow_upward,
        color: Colors.green,
        screen: const EarnPointReportScreen(),
      ),
      _ReportItem(
        title: 'Point Status',
        icon: Icons.stars,
        color: Colors.indigo,
        screen: const PointStatusScreen(),
      ),
      _ReportItem(
        title: 'My Sites',
        icon: Icons.location_on,
        color: Colors.purple,
        screen: const SiteReportScreen(),
      ),
      _ReportItem(
        title: 'My Dealers',
        icon: Icons.store,
        color: Colors.teal,
        screen: const DealerReportScreen(),
      ),
    ];

    return Scaffold(
      appBar: AppBar(title: const Text('Reports')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Reports & Analytics',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 4),
            const Text(
              'View your detailed reports and analytics',
              style: TextStyle(color: AppTheme.textSecondary),
            ),
            const SizedBox(height: 20),
            Expanded(
              child: GridView.builder(
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 2,
                  mainAxisSpacing: 14,
                  crossAxisSpacing: 14,
                  childAspectRatio: 1.2,
                ),
                itemCount: reportItems.length,
                itemBuilder: (context, index) {
                  final item = reportItems[index];
                  return InkWell(
                    onTap: () => Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => item.screen),
                    ),
                    borderRadius: BorderRadius.circular(14),
                    child: Card(
                      elevation: 2,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
                      child: Padding(
                        padding: const EdgeInsets.all(16),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Container(
                              padding: const EdgeInsets.all(12),
                              decoration: BoxDecoration(
                                color: item.color.withOpacity(0.1),
                                borderRadius: BorderRadius.circular(14),
                              ),
                              child: Icon(item.icon, color: item.color, size: 32),
                            ),
                            const SizedBox(height: 12),
                            Text(
                              item.title,
                              textAlign: TextAlign.center,
                              style: const TextStyle(
                                fontWeight: FontWeight.w600,
                                fontSize: 14,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _ReportItem {
  final String title;
  final IconData icon;
  final Color color;
  final Widget screen;

  const _ReportItem({
    required this.title,
    required this.icon,
    required this.color,
    required this.screen,
  });
}
