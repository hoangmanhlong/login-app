package com.example.loginapp.view.fragments.delivery_address_detail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentDeliveryAddressDetailBinding;
import com.example.loginapp.model.entity.DeliveryAddress;
import com.example.loginapp.presenter.DeliveryAddressDetailPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class DeliveryAddressDetailFragment extends Fragment implements DeliveryAddressDetailView {

    private static final String TAG = DeliveryAddressDetailFragment.class.getSimpleName();

    private DeliveryAddressDetailPresenter presenter;

    private FragmentDeliveryAddressDetailBinding binding;

    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DeliveryAddressDetailPresenter(this);
    }

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
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        onInputState();
        getDataShared();
        presenter.fetchProvinces();
    }

    private void getDataShared() {
        @StringRes int fragmentLabel;
        if (getArguments() != null) {
            DeliveryAddress deliveryAddress = (DeliveryAddress) getArguments().getSerializable(Constant.DELIVERY_ADDRESS_KEY);
            presenter.setDeliveryAddress(deliveryAddress);
            fragmentLabel = R.string.edit_address;
        } else {
            binding.btDeleteAddress.setVisibility(View.GONE);
            fragmentLabel = R.string.add_new_address;
        }
        binding.setFragmentLabel(fragmentLabel);
    }

    public void onDeleteButtonClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete).setMessage(R.string.delete_delivery_address)
                .setPositiveButton(R.string.ok, (dialog, which) -> presenter.deleteDeliveryAddress())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public void onNavigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
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
    public void bindAddress(DeliveryAddress deliveryAddress) {
        Log.d(TAG, "bindAddress: " + deliveryAddress);
        binding.setDeliveryAddress(deliveryAddress);
        binding.tvProvince.setText(deliveryAddress.getProvince());
    }

    @Override
    public void bindProvinces(String[] provinces) {
        requireActivity().runOnUiThread(() ->
                ((MaterialAutoCompleteTextView) binding.tvProvince).setSimpleItems(provinces)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
        dialog = null;
        System.gc();
    }

    @Override
    public void isCheckoutButtonVisible(boolean visible) {
        binding.btSave.setEnabled(visible);
    }

    public void onSaveButtonClick() {
        presenter.onSaveButtonClick(binding.isDefaultAddressSwitch.isChecked());
    }

    public void onInputState() {
        binding.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setName(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setPhoneNumber(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setAddress(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.tvProvince.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setProvince(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etPostalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setPostalCode(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etShippingOptions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setShippingOption(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}