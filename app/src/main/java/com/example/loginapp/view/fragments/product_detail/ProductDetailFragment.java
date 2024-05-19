package com.example.loginapp.view.fragments.product_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.loginapp.R;
import com.example.loginapp.adapter.comment_adapter.CommentAdapter;
import com.example.loginapp.adapter.product_adapter.OnProductClickListener;
import com.example.loginapp.adapter.product_adapter.ProductAdapter;
import com.example.loginapp.adapter.product_image_adapter.ImagesProductAdapter;
import com.example.loginapp.adapter.product_images_adapter.OnImageClickListener;
import com.example.loginapp.adapter.product_images_adapter.ProductImageAdapter;
import com.example.loginapp.databinding.FragmentProductDetailBinding;
import com.example.loginapp.model.entity.Comment;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.presenter.ProductPresenter;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.commonUI.AppMessage;
import com.example.loginapp.view.fragments.add_to_cart.AddProductToCartFragment;
import com.example.loginapp.view.fragments.bottom_sheet.SelectProductQuantityAndVoucherFragment;

import java.util.List;

public class ProductDetailFragment
        extends Fragment
        implements ProductView, OnImageClickListener, OnProductClickListener {

    private static final String TAG = "ProductDetailFragment";

    private ProductPresenter presenter;

    private ProductImageAdapter productImageAdapter;

    private CommentAdapter commentAdapter;

    private FragmentProductDetailBinding binding;

    private ViewPager productImagesViewPager;

    private NavController navController;

    private ImageView btFavorite;

    private ProductAdapter similarProductsAdapter;

    private ImagesProductAdapter imagesProductAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        presenter = new ProductPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        similarProductsAdapter = null;
        productImageAdapter = null;
        binding = null;
        commentAdapter = null;
        btFavorite = null;
        productImagesViewPager = null;
        imagesProductAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.clear();
        presenter = null;
        navController = null;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        similarProductsAdapter = new ProductAdapter(this);
        productImageAdapter = new ProductImageAdapter(this);
        imagesProductAdapter = new ImagesProductAdapter(requireContext());
        commentAdapter = new CommentAdapter();
        btFavorite = binding.favoriteBtn;
        productImagesViewPager = binding.viewPager;
        productImagesViewPager.setAdapter(imagesProductAdapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        RecyclerView recyclerView = binding.productImageRecyclerview;
        RecyclerView similarProductsRecyclerView = binding.similarProductRecyclerview;
        RecyclerView commentRecyclerView = binding.commentRecyclerView;

        recyclerView.setAdapter(productImageAdapter);
        commentRecyclerView.setAdapter(commentAdapter);
        similarProductsRecyclerView.setAdapter(similarProductsAdapter);

        presenter.initData();
    }

    @Override
    public void getDataShared() {
        if (getArguments() != null) {
            Product product = (Product) getArguments().getSerializable(Constant.PRODUCT_KEY);
            if (product != null) presenter.setProduct(product);
        }
    }

    public void onBuyNowButtonClick() {
        presenter.onBuyNowButtonClick();
    }

    public void onNavigateUp() {
        navController.navigateUp();
    }

    public void onFavoriteButtonClick() {
        presenter.updateFavorite();
    }

    public void onAddToCartButtonClick() {
        presenter.onAddToCartButtonClick();
    }

    @Override
    public void onMessage(String message) {
        AppMessage.showMessage(requireContext(), message);
    }

    @Override
    public void enableFavorite(Boolean isEnable) {
        btFavorite.setImageResource(isEnable ? R.drawable.ic_favorite_red_24 : R.drawable.ic_favorite_outline);
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
        imagesProductAdapter.setImages(product.getImages());
    }

    @Override
    public void getSimilarProducts(List<Product> products) {
        similarProductsAdapter.submitList(products);
    }

    @Override
    public void showSelectProductQuantityAndVoucherFragment(Product product) {
        SelectProductQuantityAndVoucherFragment bottomSheet = new SelectProductQuantityAndVoucherFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        bottomSheet.setArguments(bundle);
        bottomSheet.show(getChildFragmentManager(), SelectProductQuantityAndVoucherFragment.TAG);
    }

    @Override
    public void showAddProductToCartFragment(Product product) {
        AddProductToCartFragment addProductToCartFragment = new AddProductToCartFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        addProductToCartFragment.setArguments(bundle);
        addProductToCartFragment.show(getChildFragmentManager(), AddProductToCartFragment.TAG);
    }

    @Override
    public void onImageClick(int position) {
        productImagesViewPager.setCurrentItem(position);
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        navController.navigate(R.id.action_global_productFragment, bundle);
    }
}