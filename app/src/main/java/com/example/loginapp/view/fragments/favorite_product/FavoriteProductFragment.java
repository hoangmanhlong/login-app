package com.example.loginapp.view.fragments.favorite_product;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.loginapp.R;
import com.example.loginapp.adapter.SwipeHelper;
import com.example.loginapp.adapter.favorite_adapter.FavoriteAdapter;
import com.example.loginapp.adapter.favorite_adapter.FavoriteItemClickListener;
import com.example.loginapp.databinding.FragmentFavoriteProductBinding;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.presenter.FavoritePresenter;
import com.example.loginapp.utils.Constant;

import java.util.List;


public class FavoriteProductFragment extends Fragment implements FavoriteView, FavoriteItemClickListener {

    private final String TAG = FavoriteProductFragment.class.getSimpleName();

    private FavoritePresenter presenter;

    private FragmentFavoriteProductBinding binding;

    private FavoriteAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FavoritePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteProductBinding.inflate(inflater, container, false);
        adapter = new FavoriteAdapter(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    private void initView() {
        RecyclerView recyclerView = binding.favoriteRecyclerView;
        recyclerView.setAdapter(adapter);

        // Remove flashes
        SimpleItemAnimator simpleItemAnimator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        if (simpleItemAnimator != null) simpleItemAnimator.setSupportsChangeAnimations(false);
        presenter.initData();
        new SwipeHelper(requireContext(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        adapter::getProductByPosition
                ));
            }
        };
    }

    @Override
    public void isWishlistEmpty(Boolean isEmpty) {
        binding.setIsWishlistEmpty(isEmpty);
    }

    @Override
    public void bindFavoriteListProduct(List<Product> products) {
        adapter.submitList(products);
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PRODUCT_KEY, product);
        NavHostFragment.findNavController(this).navigate(R.id.action_global_productFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.addFavoriteListValueEventListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeFavoriteListValueEventListener();
    }

    @Override
    public void getProductByPosition(int productId) {
        presenter.deleteFavoriteProduct(productId);
    }
}