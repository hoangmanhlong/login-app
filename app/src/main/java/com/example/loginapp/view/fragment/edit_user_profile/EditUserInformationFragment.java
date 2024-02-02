package com.example.loginapp.view.fragment.edit_user_profile;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.loginapp.data.Constant;
import com.example.loginapp.databinding.FragmentEditUserInformationBinding;
import com.example.loginapp.model.MyOpenDocumentContract;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.presenter.EditUserProfilePresenter;
import com.example.loginapp.view.AppMessage;
import com.example.loginapp.view.AppAnimationState;
import com.example.loginapp.view.HideKeyboard;
import com.example.loginapp.view.LoadingDialog;
import com.example.loginapp.view.state.DeliveryAddressEditTextObserver;
import com.google.android.material.textfield.TextInputEditText;


public class EditUserInformationFragment extends Fragment implements EditUserProfileView {

    private FragmentEditUserInformationBinding binding;

    private final EditUserProfilePresenter presenter = new EditUserProfilePresenter(this);

    private Uri photoUrl = null;

    private Dialog dialog;

    private TextInputEditText[] editTexts;

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
        HideKeyboard.setupHideKeyboard(view, requireActivity());
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        binding.setUserData((UserData) getArguments().getSerializable(Constant.USER_KEY_NAME));
        editTexts = new TextInputEditText[]{
                binding.usernameInput,
                binding.phoneNumberInput,
                binding.addressInput,
        };
        onInputState();
    }

    private final ActivityResultLauncher<String[]> openDocument = registerForActivityResult(
            new MyOpenDocumentContract(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        binding.userImage.setImageURI(uri);
                        photoUrl = uri;
                    } else {
                        onMessage("No file selected");
                    }
                }
            }
    );

    public void onEditImage() {
        openDocument.launch(new String[]{"image/*"});
    }

    public void saveUserData() {
        String username = binding.usernameInput.getText().toString().trim();
        String phoneNumber = binding.phoneNumberInput.getText().toString().trim();
        String address = binding.addressInput.getText().toString().trim();
        presenter.saveUserData(photoUrl, username, phoneNumber, address);
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

    public void onInputState() {
        Button button = binding.saveButton;
        editTexts[0].addTextChangedListener(new DeliveryAddressEditTextObserver(button, binding.userNameLayout, editTexts));
        editTexts[1].addTextChangedListener(new DeliveryAddressEditTextObserver(button, binding.phoneNumberLayout, editTexts));
        editTexts[2].addTextChangedListener(new DeliveryAddressEditTextObserver(button, binding.addressLayout, editTexts));
    }
}