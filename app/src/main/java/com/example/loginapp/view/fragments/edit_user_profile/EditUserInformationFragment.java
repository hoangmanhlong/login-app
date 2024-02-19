package com.example.loginapp.view.fragments.edit_user_profile;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.databinding.FragmentEditUserInformationBinding;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.presenter.EditUserProfilePresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.utils.MyOpenDocumentContract;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.example.loginapp.view.commonUI.LoadingDialog;
import com.example.loginapp.view.state.TextEditTextObserver;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class EditUserInformationFragment extends Fragment implements EditUserProfileView {

    private FragmentEditUserInformationBinding binding;

    private final EditUserProfilePresenter presenter = new EditUserProfilePresenter(this);

    private Dialog dialog;

    private TextInputEditText etName, etPhoneNumber, etAddress;

    private TextInputLayout nameTextInputLayout, addressTextInputLayout, phoneNumberTextInputLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditUserInformationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);

        etAddress = binding.etAddress;
        etName = binding.etName;
        etPhoneNumber = binding.etPhoneNumber;
        nameTextInputLayout = binding.nameTextInputLayout;
        phoneNumberTextInputLayout = binding.phoneNumberTextInputLayout;
        addressTextInputLayout = binding.addressTextInputLayout;

        HideKeyboard.setupHideKeyboard(view, requireActivity());
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        binding.setUserData((UserData) getArguments().getSerializable(Constant.USER_KEY_NAME));
        onInputState();
    }

    private final ActivityResultLauncher<String[]> openDocument = registerForActivityResult(new MyOpenDocumentContract(),
            uri -> {
                if (uri != null) presenter.setPhotoUri(uri);
                else onMessage("No file selected");
            }
    );

    public void onEditImage() {
        openDocument.launch(new String[]{"image/*"});
    }

    public void saveUserData() {
        String name = etName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        boolean isAllValid = true;
        if (name.isEmpty()) {
            isAllValid = false;
            nameTextInputLayout.setError("Invalid Name");
        }
        if (phoneNumber.length() != 10) {
            isAllValid = false;
            phoneNumberTextInputLayout.setError("Invalid phone number");
        }
        if (address.isEmpty()) {
            isAllValid = false;
            addressTextInputLayout.setError("Invalid address");
        }
        if (isAllValid) presenter.saveUserData(name, phoneNumber, address);
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void showProcessBar(Boolean show) {
        if (show) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    @Override
    public void bindPhotoSelected(Uri photoUri) {
        binding.userImage.setImageURI(photoUri);
    }

    public void onInputState() {
        etName.addTextChangedListener(new TextEditTextObserver(nameTextInputLayout));
        etPhoneNumber.addTextChangedListener(new TextEditTextObserver(phoneNumberTextInputLayout));
        etAddress.addTextChangedListener(new TextEditTextObserver(addressTextInputLayout));
    }
}