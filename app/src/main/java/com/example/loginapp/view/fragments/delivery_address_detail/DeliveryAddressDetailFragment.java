package com.example.loginapp.view.fragments.delivery_address_detail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentDeliveryAddressDetailBinding;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.presenter.DeliveryAddressDetailPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.example.loginapp.view.state.TextEditTextObserver;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class DeliveryAddressDetailFragment extends Fragment implements DeliveryAddressDetailView {

    private final DeliveryAddressDetailPresenter presenter = new DeliveryAddressDetailPresenter(this);

    private FragmentDeliveryAddressDetailBinding binding;

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDeliveryAddressDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        HideKeyboard.setupHideKeyboard(view, requireActivity());
        getArgument();
        initView();
    }

    private void getArgument() {
        @StringRes int fragmentLabel;
        if (getArguments() != null) {
            DeliveryAddress deliveryAddress = (DeliveryAddress) getArguments().getSerializable(Constant.DELIVERY_ADDRESS_KEY);
            presenter.setCurrentDeliveryAddress(deliveryAddress);
            fragmentLabel = R.string.edit_address;
        } else {
//            binding.btSubmit.setEnabled(false);
            binding.btDeleteAddress.setVisibility(View.GONE);
            fragmentLabel = R.string.add_new_address;
        }
        binding.setFragmentLabel(fragmentLabel);
    }

    private void initView() {
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        presenter.getProvince();
        onInputState();
    }

    public void onDeleteButtonClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete).setMessage(R.string.delete_delivery_address)
                .setPositiveButton(R.string.ok, (dialog, which) -> presenter.deleteDeliveryAddress())
                .setNegativeButton(R.string.negative_button_title, (dialog, which) -> {
                })
                .setCancelable(false)
                .show();
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    @Override
    public void isLoading(Boolean loading) {
        if (loading) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void navigateUp() {
        onNavigateUp();
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void bindAddress(DeliveryAddress deliveryAddress) {
        binding.setDeliveryAddress(deliveryAddress);
        binding.tvProvince.setText(deliveryAddress.getProvince());
    }

    @Override
    public void bindProvinces(String[] provinces) {
        ((MaterialAutoCompleteTextView) binding.tvProvince).setSimpleItems(provinces);
    }

    public void onSubmitButtonClick() {
        String name = binding.etName.getText().toString();
        String phoneNumber = binding.etPhoneNumber.getText().toString();
        String address = binding.etAddress.getText().toString();
        String postalCode = binding.etPostalCode.getText().toString();
        String country = binding.etCountry.getText().toString();
        String shippingOptions = binding.etShippingOptions.getText().toString();
        String province = binding.tvProvince.getText().toString();
        boolean isAllCorrect = true;
        if (province.isEmpty()) {
            isAllCorrect = false;
            binding.provinceLayout.setError("Please select province");
        }
        if (name.isEmpty()) {
            isAllCorrect = false;
            binding.recipientNameLayout.setError("Invalid full name");
        }
        if (phoneNumber.length() != 10) {
            isAllCorrect = false;
            binding.phoneNumberLayout.setError("Invalid phone number");
        }

        if (address.isEmpty()) {
            isAllCorrect = false;
            binding.addressLayout.setError("Invalid address");
        }

        if (postalCode.length() != 4) {
            isAllCorrect = false;
            binding.postalCodeLayout.setError("Invalid postal code");
        }
        if (isAllCorrect) {
            Boolean isAddressDefault = binding.isDefaultAddressSwitch.isChecked();
            presenter.updateDeliveryAddress(name, phoneNumber, address, province, postalCode, country, shippingOptions, isAddressDefault);
        }
    }

    public void onInputState() {
        binding.etName.addTextChangedListener(new TextEditTextObserver(binding.recipientNameLayout));
        binding.etPhoneNumber.addTextChangedListener(new TextEditTextObserver(binding.phoneNumberLayout));
        binding.etAddress.addTextChangedListener(new TextEditTextObserver(binding.addressLayout));
        binding.tvProvince.addTextChangedListener(new TextEditTextObserver(binding.provinceLayout));
        binding.etPostalCode.addTextChangedListener(new TextEditTextObserver(binding.postalCodeLayout));
    }
}