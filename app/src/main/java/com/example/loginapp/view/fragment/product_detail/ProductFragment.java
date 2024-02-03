package com.example.loginapp.view.fragment.product_detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loginapp.R;
import com.example.loginapp.adapter.comment_adapter.CommentAdapter;
import com.example.loginapp.adapter.product_images_adapter.OnImageClickListener;
import com.example.loginapp.adapter.product_images_adapter.ProductImageAdapter;
import com.example.loginapp.data.Constant;
import com.example.loginapp.databinding.FragmentProductBinding;
import com.example.loginapp.model.entity.Comment;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.presenter.ProductPresenter;
import com.example.loginapp.view.AppAnimationState;
import com.example.loginapp.view.AppMessage;
import com.example.loginapp.view.fragment.bottom_sheet.ModalBottomSheetFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ProductFragment extends Fragment implements ProductView, OnImageClickListener {

    private final ProductPresenter presenter = new ProductPresenter(this);

    private final ProductImageAdapter productImageAdapter = new ProductImageAdapter(this);

    private final CommentAdapter commentAdapter = new CommentAdapter();

    private FragmentProductBinding binding;

    private ImageButton btFavorite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.setFragment(this);
        AppAnimationState.setBottomActionView(binding.bottomActionView, requireActivity(), true);
        getDataShared();
        RecyclerView recyclerView = binding.productImageRecyclerview;
        recyclerView.setAdapter(productImageAdapter);

        RecyclerView commentRecyclerView = binding.commentRecyclerView;
        commentRecyclerView.setAdapter(commentAdapter);

        btFavorite = binding.favoriteBtn;
    }

    private void getDataShared() {
        if (getArguments() != null) {
            Product product = (Product) getArguments().getSerializable(Constant.PRODUCT_KEY);
            if (product != null) {
                presenter.setProduct(product);
            }
        }
    }

    public void showBottomSheet() {
        Product product = presenter.getProduct();
        if (product != null) {
            ModalBottomSheetFragment modalBottomSheetFragment = new ModalBottomSheetFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.PRODUCT_KEY, product);
            modalBottomSheetFragment.setArguments(bundle);
            modalBottomSheetFragment.show(getChildFragmentManager(), ModalBottomSheetFragment.TAG);
        }
    }

    public void onNavigateUp() {
        Navigation.findNavController(binding.getRoot()).navigateUp();
    }

    public void onFavoriteButtonClick() {
        presenter.updateFavorite();
    }

    public void onCartBtnClick() {
        binding.tvAddToCart.setVisibility(View.VISIBLE);
        presenter.addProductToCart();
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void enableFavorite(Boolean isEnable) {
        if (isEnable) btFavorite.setImageResource(R.drawable.ic_favorite);
        else btFavorite.setImageResource(R.drawable.ic_favorite_gray);
    }

    @Override
    public void saveToBasketSuccess() {
        binding.tvAddToCart.setVisibility(View.GONE);
        EventBus.getDefault().postSticky(new NewProductInBasketMessage(true));
    }

    @Override
    public void getComments(List<Comment> comments) {
        commentAdapter.submitList(comments);
    }

    @Override
    public void getCommentCount(String number) {

    }

    @Override
    public void bindProduct(Product product) {
        binding.setProduct(product);
        productImageAdapter.submitList(product.getImages());
    }

    @Override
    public void hasNewFavoriteProduct() {
        EventBus.getDefault().postSticky(new NewProductInWishlistMessage(true));
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