package com.example.loginapp.view.fragments.edit_user_profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.loginapp.R;
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

    // Hold a reference to the current animator so that it can be canceled
    // mid-way.
    private Animator currentAnimator;

    // The system "short" animation time duration in milliseconds. This duration
    // is ideal for subtle animations or animations that occur frequently.
    private int shortAnimationDuration;

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
        presenter.setUserData((UserData) getArguments().getSerializable(Constant.USER_KEY_NAME));
        onInputState();

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    private final ActivityResultLauncher<String[]> openDocument =
            registerForActivityResult(new MyOpenDocumentContract(),
            uri -> {
                if (uri != null) presenter.setPhotoUri(uri);
                else onMessage("No file selected");
            }
    );

    public void onImageClick() {
        String imageUrl = presenter.getUserData().getPhotoUrl();
        if (imageUrl != null && !imageUrl.isEmpty())
            zoomImageFromThumb(binding.tvUserAvatar, presenter.getUserData().getPhotoUrl());
    }

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
        binding.tvUserAvatar.setImageURI(photoUri);
    }

    @Override
    public void bindUserData(UserData userData) {
        binding.setUserData(userData);
    }

    public void onInputState() {
        etName.addTextChangedListener(new TextEditTextObserver(nameTextInputLayout));
        etPhoneNumber.addTextChangedListener(new TextEditTextObserver(phoneNumberTextInputLayout));
        etAddress.addTextChangedListener(new TextEditTextObserver(addressTextInputLayout));
    }

    private void zoomImageFromThumb(final View thumbView, String imageUrl) {
        // If there's an animation in progress, cancel it immediately and
        // proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.

//        binding.expandedImage.setImageResource(imageResId);

        Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
                    .override(Target.SIZE_ORIGINAL)
                    .into(binding.expandedImage);


        // Calculate the starting and ending bounds for the zoomed-in image.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the
        // container view. Set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        binding.editProfileContent
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Using the "center crop" technique, adjust the start bounds to be the
        // same aspect ratio as the final bounds. This prevents unwanted
        // stretching during the animation. Calculate the start scaling factor.
        // The end scaling factor is always 1.0.
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally.
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically.
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it positions the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);

        animateZoomToLargeImage(startBounds, finalBounds, startScale);

        setDismissLargeImageAnimation(thumbView, startBounds, startScale);
    }

    private void animateZoomToLargeImage(Rect startBounds, Rect finalBounds, Float startScale) {

        binding.expandedImage.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the
        // top-left corner of the zoomed-in view. The default is the center of
        // the view.
        binding.expandedImage.setPivotX(0f);
        binding.expandedImage.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties: X, Y, SCALE_X, and SCALE_Y.
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(binding.expandedImage, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(binding.expandedImage, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(binding.expandedImage, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(binding.expandedImage,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;
    }

    private void setDismissLargeImageAnimation(View thumbView, Rect startBounds, Float startScale) {
        // When the zoomed-in image is tapped, it zooms down to the original
        // bounds and shows the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        binding.expandedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning and sizing properties in
                // parallel, back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                                .ofFloat(binding.expandedImage, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(binding.expandedImage,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(binding.expandedImage,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(binding.expandedImage,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        binding.expandedImage.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        binding.expandedImage.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }
}