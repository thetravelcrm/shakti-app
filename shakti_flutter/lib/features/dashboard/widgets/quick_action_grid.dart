import 'package:flutter/material.dart';
import '../../../core/theme.dart';

class QuickActionItem {
  final String label;
  final IconData icon;
  final VoidCallback onTap;
  final Color? color;

  const QuickActionItem({
    required this.label,
    required this.icon,
    required this.onTap,
    this.color,
  });
}

class QuickActionGrid extends StatelessWidget {
  final List<QuickActionItem> items;

  const QuickActionGrid({super.key, required this.items});

  @override
  Widget build(BuildContext context) {
    return GridView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 3,
        mainAxisSpacing: 12,
        crossAxisSpacing: 12,
        childAspectRatio: 1.0,
      ),
      itemCount: items.length,
      itemBuilder: (context, index) {
        final item = items[index];
        return _ActionTile(item: item);
      },
    );
  }
}

class _ActionTile extends StatelessWidget {
  final QuickActionItem item;

  const _ActionTile({required this.item});

  @override
  Widget build(BuildContext context) {
    final tileColor = item.color ?? AppTheme.primaryColor;

    return InkWell(
      onTap: item.onTap,
      borderRadius: BorderRadius.circular(14),
      child: Card(
        elevation: 2,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
        child: Padding(
          padding: const EdgeInsets.all(10),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Container(
                padding: const EdgeInsets.all(10),
                decoration: BoxDecoration(
                  color: tileColor.withOpacity(0.1),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Icon(item.icon, color: tileColor, size: 28),
              ),
              const SizedBox(height: 8),
              Text(
                item.label,
                textAlign: TextAlign.center,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(
                  fontSize: 11,
                  fontWeight: FontWeight.w600,
                  color: AppTheme.textPrimary,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
