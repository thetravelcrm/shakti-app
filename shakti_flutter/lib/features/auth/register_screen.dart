import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../core/auth/auth_provider.dart';
import '../../core/api/api_endpoints.dart';
import '../../core/theme.dart';
import '../../shared/widgets/loading_overlay.dart';

class RegisterScreen extends ConsumerStatefulWidget {
  const RegisterScreen({super.key});

  @override
  ConsumerState<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends ConsumerState<RegisterScreen> {
  final _formKey = GlobalKey<FormState>();
  bool _isLoading = false;
  int _currentStep = 0;

  // Text controllers
  final _fNameCtrl = TextEditingController();
  final _lNameCtrl = TextEditingController();
  final _mobileCtrl = TextEditingController();
  final _whatsappCtrl = TextEditingController();
  final _emailCtrl = TextEditingController();
  final _aadhaarCtrl = TextEditingController();
  final _contactNameCtrl = TextEditingController();

  // Master data
  List<Map<String, dynamic>> _states = [];
  List<Map<String, dynamic>> _districts = [];
  List<Map<String, dynamic>> _blocks = [];
  List<Map<String, dynamic>> _agentTypes = [];
  List<Map<String, dynamic>> _dealers = [];

  // Selected values
  String? _selectedState;
  String? _selectedDistrict;
  String? _selectedBlock;
  String? _selectedAgentType;
  String? _selectedDealer;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
  }

  @override
  void dispose() {
    _fNameCtrl.dispose();
    _lNameCtrl.dispose();
    _mobileCtrl.dispose();
    _whatsappCtrl.dispose();
    _emailCtrl.dispose();
    _aadhaarCtrl.dispose();
    _contactNameCtrl.dispose();
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final results = await Future.wait([
        api.post(ApiEndpoints.stateList, {}, injectAuth: false),
        api.post(ApiEndpoints.agentTypeList, {}, injectAuth: false),
      ]);
      if (mounted) {
        setState(() {
          _states = results[0].data ?? results[0].list ?? [];
          _agentTypes = results[1].data ?? results[1].list ?? [];
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
    final response = await api.post(ApiEndpoints.districtList, {'stateId': stateId}, injectAuth: false);
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
    final response = await api.post(ApiEndpoints.blockList, {'districtId': districtId}, injectAuth: false);
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
    final response = await api.post(ApiEndpoints.dealerList, body, injectAuth: false);
    if (response.isSuccess && mounted) {
      setState(() => _dealers = response.data ?? response.list ?? []);
    }
  }

  Future<void> _register() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _isLoading = true);
    try {
      final api = ref.read(apiProvider);
      final response = await api.post(ApiEndpoints.userRegistration, {
        'fName': _fNameCtrl.text.trim(),
        'lName': _lNameCtrl.text.trim(),
        'mobileNo': _mobileCtrl.text.trim(),
        'whatsappNo': _whatsappCtrl.text.trim(),
        'emailID': _emailCtrl.text.trim(),
        'stateId': _selectedState ?? '',
        'districtId': _selectedDistrict ?? '',
        'blockId': _selectedBlock ?? '',
        'agentType': _selectedAgentType ?? '',
        'dealerId': _selectedDealer ?? '',
        'aadhaarNumber': _aadhaarCtrl.text.trim(),
        'companyContactName': _contactNameCtrl.text.trim(),
      }, injectAuth: false);
      if (mounted) {
        if (response.isSuccess) {
          showSuccessSnackbar(context, response.msg ?? 'Registration successful');
          Navigator.pop(context, true);
        } else {
          showErrorSnackbar(context, response.msg ?? 'Registration failed');
        }
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Register')),
      body: LoadingOverlay(
        isLoading: _isLoading,
        child: Form(
          key: _formKey,
          child: Stepper(
            currentStep: _currentStep,
            onStepContinue: () {
              if (_currentStep < 2) {
                setState(() => _currentStep++);
              } else {
                _register();
              }
            },
            onStepCancel: () {
              if (_currentStep > 0) {
                setState(() => _currentStep--);
              }
            },
            controlsBuilder: (context, details) {
              return Padding(
                padding: const EdgeInsets.only(top: 16),
                child: Row(
                  children: [
                    ElevatedButton(
                      onPressed: details.onStepContinue,
                      child: Text(_currentStep == 2 ? 'Register' : 'Continue'),
                    ),
                    if (_currentStep > 0) ...[
                      const SizedBox(width: 12),
                      OutlinedButton(
                        onPressed: details.onStepCancel,
                        child: const Text('Back'),
                      ),
                    ],
                  ],
                ),
              );
            },
            steps: [
              Step(
                title: const Text('Personal Info'),
                isActive: _currentStep >= 0,
                state: _currentStep > 0 ? StepState.complete : StepState.indexed,
                content: Column(
                  children: [
                    TextFormField(
                      controller: _fNameCtrl,
                      decoration: const InputDecoration(
                        labelText: 'First Name',
                        prefixIcon: Icon(Icons.person),
                      ),
                      validator: (v) => v == null || v.trim().isEmpty ? 'Required' : null,
                    ),
                    const SizedBox(height: 14),
                    TextFormField(
                      controller: _lNameCtrl,
                      decoration: const InputDecoration(
                        labelText: 'Last Name',
                        prefixIcon: Icon(Icons.person_outline),
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
                      controller: _whatsappCtrl,
                      decoration: const InputDecoration(
                        labelText: 'WhatsApp Number',
                        prefixIcon: Icon(Icons.chat),
                      ),
                      keyboardType: TextInputType.phone,
                      maxLength: 10,
                    ),
                    const SizedBox(height: 14),
                    TextFormField(
                      controller: _emailCtrl,
                      decoration: const InputDecoration(
                        labelText: 'Email ID',
                        prefixIcon: Icon(Icons.email),
                      ),
                      keyboardType: TextInputType.emailAddress,
                    ),
                  ],
                ),
              ),
              Step(
                title: const Text('Location'),
                isActive: _currentStep >= 1,
                state: _currentStep > 1 ? StepState.complete : StepState.indexed,
                content: Column(
                  children: [
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
                  ],
                ),
              ),
              Step(
                title: const Text('Business Details'),
                isActive: _currentStep >= 2,
                content: Column(
                  children: [
                    _buildDropdown(
                      label: 'Agent Type',
                      value: _selectedAgentType,
                      items: _agentTypes,
                      idKey: 'agentTypeId',
                      nameKey: 'agentTypeName',
                      onChanged: (v) => setState(() => _selectedAgentType = v),
                      isRequired: true,
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
                    const SizedBox(height: 14),
                    TextFormField(
                      controller: _aadhaarCtrl,
                      decoration: const InputDecoration(
                        labelText: 'Aadhaar Number',
                        prefixIcon: Icon(Icons.credit_card),
                      ),
                      keyboardType: TextInputType.number,
                      maxLength: 12,
                    ),
                    const SizedBox(height: 14),
                    TextFormField(
                      controller: _contactNameCtrl,
                      decoration: const InputDecoration(
                        labelText: 'Company Contact Name',
                        prefixIcon: Icon(Icons.business),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
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
