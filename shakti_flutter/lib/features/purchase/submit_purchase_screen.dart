import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class SubmitPurchaseScreen extends ConsumerStatefulWidget {
  const SubmitPurchaseScreen({super.key});

  @override
  ConsumerState<SubmitPurchaseScreen> createState() => _SubmitPurchaseScreenState();
}

class _SubmitPurchaseScreenState extends ConsumerState<SubmitPurchaseScreen> {
  final _formKey = GlobalKey<FormState>();
  bool _isLoading = false;

  // Master data lists
  List<Map<String, dynamic>> _states = [];
  List<Map<String, dynamic>> _districts = [];
  List<Map<String, dynamic>> _blocks = [];
  List<Map<String, dynamic>> _siteTypes = [];
  List<Map<String, dynamic>> _constructionStages = [];
  List<Map<String, dynamic>> _dealers = [];
  List<Map<String, dynamic>> _brands = [];
  List<Map<String, dynamic>> _products = [];

  // Selected values
  String? _selectedState;
  String? _selectedDistrict;
  String? _selectedBlock;
  String? _selectedSiteType;
  String? _selectedConstructionStage;
  String? _selectedDealer;
  String? _selectedBrand;
  String? _selectedProduct;

  // Text controllers
  final _ownerNameCtrl = TextEditingController();
  final _mobileCtrl = TextEditingController();
  final _quantityCtrl = TextEditingController();
  final _dateCtrl = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadInitialData();
  }

  @override
  void dispose() {
    _ownerNameCtrl.dispose();
    _mobileCtrl.dispose();
    _quantityCtrl.dispose();
    _dateCtrl.dispose();
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final results = await Future.wait([
        api.postAuth(ApiEndpoints.stateList),
        api.postAuth(ApiEndpoints.siteTypeList),
        api.postAuth(ApiEndpoints.constructionStageList),
        api.postAuth(ApiEndpoints.brandList),
      ]);

      if (mounted) {
        setState(() {
          _states = results[0].data ?? results[0].list ?? [];
          _siteTypes = results[1].data ?? results[1].list ?? [];
          _constructionStages = results[2].data ?? results[2].list ?? [];
          _brands = results[3].data ?? results[3].list ?? [];
        });
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  Future<void> _loadDistricts(String stateId) async {
    setState(() {
      _districts = [];
      _blocks = [];
      _dealers = [];
      _selectedDistrict = null;
      _selectedBlock = null;
      _selectedDealer = null;
    });
    final api = ref.read(apiProvider);
    final response = await api.postAuth(ApiEndpoints.districtList, {'stateId': stateId});
    if (response.isSuccess && mounted) {
      setState(() => _districts = response.data ?? response.list ?? []);
    }
  }

  Future<void> _loadBlocks(String districtId) async {
    setState(() {
      _blocks = [];
      _selectedBlock = null;
    });
    final api = ref.read(apiProvider);
    final response = await api.postAuth(ApiEndpoints.blockList, {'districtId': districtId});
    if (response.isSuccess && mounted) {
      setState(() => _blocks = response.data ?? response.list ?? []);
    }
  }

  Future<void> _loadDealers() async {
    setState(() {
      _dealers = [];
      _selectedDealer = null;
    });
    final api = ref.read(apiProvider);
    final body = <String, dynamic>{};
    if (_selectedState != null) body['stateId'] = _selectedState;
    if (_selectedDistrict != null) body['districtId'] = _selectedDistrict;
    final response = await api.postAuth(ApiEndpoints.dealerList, body);
    if (response.isSuccess && mounted) {
      setState(() => _dealers = response.data ?? response.list ?? []);
    }
  }

  Future<void> _loadProducts(String brandId) async {
    setState(() {
      _products = [];
      _selectedProduct = null;
    });
    final api = ref.read(apiProvider);
    final response = await api.postAuth(ApiEndpoints.productList, {'brandId': brandId});
    if (response.isSuccess && mounted) {
      setState(() => _products = response.data ?? response.list ?? response.productList ?? []);
    }
  }

  Future<void> _pickDate() async {
    final date = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime.now().subtract(const Duration(days: 30)),
      lastDate: DateTime.now(),
    );
    if (date != null) {
      _dateCtrl.text =
          '${date.day.toString().padLeft(2, '0')}/${date.month.toString().padLeft(2, '0')}/${date.year}';
    }
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.postAuth(ApiEndpoints.submitNewPurchase, {
        'stateId': _selectedState ?? '',
        'districtId': _selectedDistrict ?? '',
        'blockId': _selectedBlock ?? '',
        'siteTypeId': _selectedSiteType ?? '',
        'constructionStageId': _selectedConstructionStage ?? '',
        'dealerId': _selectedDealer ?? '',
        'brandId': _selectedBrand ?? '',
        'productId': _selectedProduct ?? '',
        'ownerName': _ownerNameCtrl.text.trim(),
        'ownerMobile': _mobileCtrl.text.trim(),
        'quantity': _quantityCtrl.text.trim(),
        'purchaseDate': _dateCtrl.text.trim(),
      });
      if (mounted) {
        if (response.isSuccess) {
          showSuccessSnackbar(context, response.msg ?? 'Purchase submitted');
          Navigator.pop(context, true);
        } else {
          showErrorSnackbar(context, response.msg ?? 'Submission failed');
        }
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Submit Purchase')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Form(
            key: _formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                const Text(
                  'New Purchase Entry',
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 4),
                const Text(
                  'Fill in the details below to submit a new purchase',
                  style: TextStyle(color: AppTheme.textSecondary),
                ),
                const SizedBox(height: 20),

                // Location section
                _sectionLabel('Location'),
                _buildDropdown(
                  label: 'State',
                  value: _selectedState,
                  items: _states,
                  idKey: 'stateId',
                  nameKey: 'stateName',
                  onChanged: (v) {
                    setState(() => _selectedState = v);
                    if (v != null) _loadDistricts(v);
                  },
                  isRequired: true,
                ),
                const SizedBox(height: 14),
                _buildDropdown(
                  label: 'District',
                  value: _selectedDistrict,
                  items: _districts,
                  idKey: 'districtId',
                  nameKey: 'districtName',
                  onChanged: (v) {
                    setState(() => _selectedDistrict = v);
                    if (v != null) {
                      _loadBlocks(v);
                      _loadDealers();
                    }
                  },
                  isRequired: true,
                ),
                const SizedBox(height: 14),
                _buildDropdown(
                  label: 'Block',
                  value: _selectedBlock,
                  items: _blocks,
                  idKey: 'blockId',
                  nameKey: 'blockName',
                  onChanged: (v) => setState(() => _selectedBlock = v),
                ),
                const SizedBox(height: 20),

                // Site section
                _sectionLabel('Site Details'),
                _buildDropdown(
                  label: 'Site Type',
                  value: _selectedSiteType,
                  items: _siteTypes,
                  idKey: 'siteTypeId',
                  nameKey: 'siteTypeName',
                  onChanged: (v) => setState(() => _selectedSiteType = v),
                  isRequired: true,
                ),
                const SizedBox(height: 14),
                _buildDropdown(
                  label: 'Construction Stage',
                  value: _selectedConstructionStage,
                  items: _constructionStages,
                  idKey: 'constructionStageId',
                  nameKey: 'constructionStageName',
                  onChanged: (v) => setState(() => _selectedConstructionStage = v),
                ),
                const SizedBox(height: 14),
                _buildDropdown(
                  label: 'Dealer',
                  value: _selectedDealer,
                  items: _dealers,
                  idKey: 'dealerId',
                  nameKey: 'dealerName',
                  onChanged: (v) => setState(() => _selectedDealer = v),
                  isRequired: true,
                ),
                const SizedBox(height: 20),

                // Product section
                _sectionLabel('Product Details'),
                _buildDropdown(
                  label: 'Brand',
                  value: _selectedBrand,
                  items: _brands,
                  idKey: 'brandId',
                  nameKey: 'brandName',
                  onChanged: (v) {
                    setState(() => _selectedBrand = v);
                    if (v != null) _loadProducts(v);
                  },
                  isRequired: true,
                ),
                const SizedBox(height: 14),
                _buildDropdown(
                  label: 'Product',
                  value: _selectedProduct,
                  items: _products,
                  idKey: 'productId',
                  nameKey: 'productName',
                  onChanged: (v) => setState(() => _selectedProduct = v),
                  isRequired: true,
                ),
                const SizedBox(height: 20),

                // Owner section
                _sectionLabel('Owner Details'),
                TextFormField(
                  controller: _ownerNameCtrl,
                  decoration: const InputDecoration(
                    labelText: 'Owner Name',
                    prefixIcon: Icon(Icons.person),
                  ),
                  validator: (v) => v == null || v.trim().isEmpty ? 'Required' : null,
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _mobileCtrl,
                  decoration: const InputDecoration(
                    labelText: 'Mobile Number',
                    prefixIcon: Icon(Icons.phone),
                  ),
                  keyboardType: TextInputType.phone,
                  maxLength: 10,
                  validator: (v) {
                    if (v == null || v.trim().isEmpty) return 'Required';
                    if (v.length != 10) return 'Enter 10 digit mobile';
                    return null;
                  },
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _quantityCtrl,
                  decoration: const InputDecoration(
                    labelText: 'Quantity',
                    prefixIcon: Icon(Icons.numbers),
                  ),
                  keyboardType: TextInputType.number,
                  validator: (v) => v == null || v.trim().isEmpty ? 'Required' : null,
                ),
                const SizedBox(height: 14),
                TextFormField(
                  controller: _dateCtrl,
                  readOnly: true,
                  onTap: _pickDate,
                  decoration: const InputDecoration(
                    labelText: 'Purchase Date',
                    prefixIcon: Icon(Icons.calendar_today),
                    hintText: 'DD/MM/YYYY',
                  ),
                  validator: (v) => v == null || v.trim().isEmpty ? 'Required' : null,
                ),
                const SizedBox(height: 28),
                ElevatedButton.icon(
                  onPressed: _submit,
                  icon: const Icon(Icons.send),
                  label: const Text('Submit Purchase'),
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

  Widget _sectionLabel(String text) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: Text(
        text,
        style: const TextStyle(
          fontSize: 16,
          fontWeight: FontWeight.bold,
          color: AppTheme.primaryColor,
        ),
      ),
    );
  }

  Widget _buildDropdown({
    required String label,
    required String? value,
    required List<Map<String, dynamic>> items,
    required String idKey,
    required String nameKey,
    required ValueChanged<String?> onChanged,
    bool isRequired = false,
  }) {
    // Ensure value exists in items
    final validValue = items.any((e) => e[idKey]?.toString() == value) ? value : null;

    return DropdownButtonFormField<String>(
      value: validValue,
      isExpanded: true,
      decoration: InputDecoration(
        labelText: label,
        prefixIcon: const Icon(Icons.arrow_drop_down_circle_outlined),
      ),
      items: items.map((item) {
        return DropdownMenuItem<String>(
          value: item[idKey]?.toString() ?? '',
          child: Text(
            item[nameKey]?.toString() ?? item['name']?.toString() ?? '',
            overflow: TextOverflow.ellipsis,
          ),
        );
      }).toList(),
      onChanged: onChanged,
      validator: isRequired
          ? (v) => v == null || v.isEmpty ? 'Select $label' : null
          : null,
    );
  }
}
