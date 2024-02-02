package com.example.loginapp.view.fragment.delivery_address_detail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.R;
import com.example.loginapp.data.Constant;
import com.example.loginapp.databinding.FragmentDeliveryAddressDetailBinding;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.presenter.DeliveryAddressDetailPresenter;
import com.example.loginapp.view.AppMessage;
import com.example.loginapp.view.AppAnimationState;
import com.example.loginapp.view.HideKeyboard;
import com.example.loginapp.view.LoadingDialog;
import com.example.loginapp.view.state.DeliveryAddressEditTextObserver;
import com.google.android.material.textfield.TextInputEditText;

public class DeliveryAddressDetailFragment extends Fragment implements DeliveryAddressDetailView {

    private final DeliveryAddressDetailPresenter presenter = new DeliveryAddressDetailPresenter(this);

    private FragmentDeliveryAddressDetailBinding binding;

    private Dialog dialog;

    private TextInputEditText[] editTexts;

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
        initView();
        getArgument();
    }

    private void getArgument() {
        if (getArguments() != null) {
            DeliveryAddress deliveryAddress = (DeliveryAddress) getArguments().getSerializable(Constant.DELIVERY_ADDRESS_KEY);
            binding.setDeliveryAddress(deliveryAddress);
            presenter.setCurrentDeliveryAddress(deliveryAddress);
        } else {
            binding.fragmentNameTextView.setText("Add New Address");
            binding.submitButton.setEnabled(false);
            binding.deleteButton.setVisibility(View.GONE);
        }
    }

    private void initView() {
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        editTexts = new TextInputEditText[]{
                binding.recipientNameEditText,
                binding.phoneNumberEditText,
                binding.addressEditText,
                binding.provinceEditText,
                binding.postalCodeEditText,
                binding.countryEditText
        };
        onInputState();
    }

    public void onDeleteButtonClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.logout).setMessage(R.string.delete_delivery_address)
                .setPositiveButton(
                        R.string.delete,
                        (dialog, which) -> {
                            presenter.deleteDeliveryAddress();
                        }
                )
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

    public void onSubmitButtonClick() {
        String name = editTexts[0].getText().toString();
        String phoneNumber = editTexts[1].getText().toString();
        String address = editTexts[2].getText().toString();
        String province = editTexts[3].getText().toString();
        String postalCode = editTexts[4].getText().toString();
        String country = editTexts[5].getText().toString();
        String shippingAddress = binding.shippingOptionsEditText.getText().toString();
        Boolean isAddressDefault = binding.isDefaultAddressSwitch.isChecked();
        presenter.updateDeliveryAddress(name, phoneNumber, address, province, postalCode, country, shippingAddress, isAddressDefault);
    }

    public void onInputState() {
        Button button = binding.submitButton;
        editTexts[0].addTextChangedListener(new DeliveryAddressEditTextObserver(button, binding.recipientNameLayout, editTexts));
        editTexts[1].addTextChangedListener(new DeliveryAddressEditTextObserver(button, binding.phoneNumberLayout, editTexts));
        editTexts[2].addTextChangedListener(new DeliveryAddressEditTextObserver(button, binding.addressLayout, editTexts));
        editTexts[3].addTextChangedListener(new DeliveryAddressEditTextObserver(button, binding.provinceLayout, editTexts));
        editTexts[4].addTextChangedListener(new DeliveryAddressEditTextObserver(button, binding.postalCodeLayout, editTexts));
        editTexts[5].addTextChangedListener(new DeliveryAddressEditTextObserver(button, binding.countryLayout, editTexts));
    }
}