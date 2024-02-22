package com.example.loginapp.view.fragments.product_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loginapp.App;
import com.example.loginapp.R;
import com.example.loginapp.adapter.comment_adapter.CommentAdapter;
import com.example.loginapp.adapter.product_adapter.OnProductClickListener;
import com.example.loginapp.adapter.product_adapter.ProductAdapter;
import com.example.loginapp.adapter.product_images_adapter.OnImageClickListener;
import com.example.loginapp.adapter.product_images_adapter.ProductImageAdapter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.databinding.FragmentProductBinding;
import com.example.loginapp.model.entity.Comment;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.presenter.ProductPresenter;
import com.example.loginapp.view.commonUI.AppAnimationState;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.commonUI.LoginRemindDialog;
import com.example.loginapp.view.fragments.bottom_sheet.ModalBottomSheetFragment;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ProductFragment extends Fragment implements ProductView, OnImageClickListener, OnProductClickListener {

    private final ProductPresenter presenter = new ProductPresenter(this);

    private final ProductImageAdapter productImageAdapter = new ProductImageAdapter(this);

    private final CommentAdapter commentAdapter = new CommentAdapter();

    private FragmentProductBinding binding;

    private CheckBox btFavorite;

    private final ProductAdapter similarProductsAdapter = new ProductAdapter(this);

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
        AppAnimationState.setBottomActionView(binding.bottomActionView, true);
        getDataShared();
        RecyclerView recyclerView = binding.productImageRecyclerview;
        RecyclerView similarProductsRecyclerView = binding.similarProductRecyclerview;
        RecyclerView commentRecyclerView = binding.commentRecyclerView;

        recyclerView.setAdapter(productImageAdapter);
        commentRecyclerView.setAdapter(commentAdapter);
        similarProductsRecyclerView.setAdapter(similarProductsAdapter);

        btFavorite = binding.favoriteBtn;

        presenter.initData();
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
        if (FirebaseAuth.getInstance().getCurrentUser() != null)  {
            Product product = presenter.getProduct();
            if (product != null) {
                ModalBottomSheetFragment bottomSheet = new ModalBottomSheetFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.PRODUCT_KEY, product);

                bottomSheet.setArguments(bundle);
                bottomSheet.show(getChildFragmentManager(), ModalBottomSheetFragment.TAG);
            }
        } else {
            LoginRemindDialog.show(this, requireContext());
        }
    }

    public void onNavigateUp() {
        NavHostFragment.findNavController(this).navigateUp();
    }

    public void onFavoriteButtonClick() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)  {
            presenter.updateFavorite();
        } else {
            binding.favoriteBtn.setChecked(false);
            LoginRemindDialog.show(this, requireContext());
        }
    }

    public void onCartBtnClick() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)  {
            binding.tvAddToCart.setVisibility(View.VISIBLE);
            presenter.addProductToCart();
        } else {
            LoginRemindDialog.show(this, requireContext());
        }
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void enableFavorite(Boolean isEnable) {
        btFavorite.setChecked(isEnable);
    }

    @Override
    public void saveToBasketSuccess() {
        binding.tvAddToCart.setVisibility(View.GONE);
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
    public void getSimilarProducts(List<Product> products) {
        similarProductsAdapter.submitList(products);
    }

    @Override
    public void onImageClick(String url) {
        Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(binding.imThumbnail);
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        NavHostFragment.findNavController(this).navigate(R.id.action_global_productFragment, bundle);
    }
}