package com.example.loginapp.view.fragment.product_detail;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loginapp.R;
import com.example.loginapp.adapter.product_images_adapter.OnImageClickListener;
import com.example.loginapp.adapter.product_images_adapter.ProductImageAdapter;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.databinding.FragmentProductBinding;
import com.example.loginapp.presenter.ProductPresenter;
import com.example.loginapp.view.LoadingDialog;
import com.example.loginapp.view.fragment.bottom_sheet.ModalBottomSheetFragment;

public class ProductFragment extends Fragment implements ProductView, OnImageClickListener {

    public static final String PRODUCT_KEY = "AA#23";

    private final ProductPresenter presenter = new ProductPresenter(this);

    private final ProductImageAdapter productImageAdapter = new ProductImageAdapter(this);

    private FragmentProductBinding binding;

    private ImageButton btFavorite;

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        dialog = LoadingDialog.getLoadingDialog(requireContext());
        RecyclerView recyclerView = binding.productImageRecyclerview;
        recyclerView.setAdapter(productImageAdapter);

        binding.setFragment(this);
        btFavorite = binding.favoriteBtn;
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
        if (getArguments() != null) {
            int productId = getArguments().getInt("productId");
            presenter.getProduct(productId);
        }
    }

    public void showBottomSheet() {
        Product product = presenter.currentProduct;
        if (product != null) {
            ModalBottomSheetFragment modalBottomSheetFragment = new ModalBottomSheetFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(PRODUCT_KEY, presenter.currentProduct.getId());
            modalBottomSheetFragment.setArguments(bundle);
            modalBottomSheetFragment.show(getChildFragmentManager(), ModalBottomSheetFragment.TAG);
        }
    }

    public void onNavigateIconClick() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    public void onFavoriteIconClick() {
        if (presenter.isFavorite) presenter.removeFavorite();
        else presenter.addFavoriteProduct();
    }

    public void onCartBtnClick() {
        binding.tvAddToCart.setVisibility(View.VISIBLE);
        presenter.addToCart();
    }

    @Override
    public void onLoadProduct(Product product) {
        binding.setProduct(product);
        productImageAdapter.submitList(product.getImages());

//        ConstraintLayout bottomActionView = binding.bottomActionView;
//        binding.bottomActionView.setVisibility(View.VISIBLE);
//
//        ObjectAnimator animator = ObjectAnimator
//            .ofFloat(bottomActionView, "translationY", bottomActionView.getHeight(), 0);
//        animator.setDuration(2000);
//
//        // Start the animation when the activity is created
//        animator.start();
    }

    @Override
    public void onMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void enableFavorite(Boolean isEnable) {
        if (isEnable) btFavorite.setImageResource(R.drawable.ic_favorite);
        else btFavorite.setImageResource(R.drawable.ic_favorite_gray);
    }

    @Override
    public void isLoading(Boolean loading) {
        if (loading) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void saveToBasketSuccess() {
        binding.tvAddToCart.setVisibility(View.GONE);
    }

    @Override
    public void onImageClick(String url) {
        Glide.with(requireContext())
            .load(url)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(binding.imThumbnail);
    }
}