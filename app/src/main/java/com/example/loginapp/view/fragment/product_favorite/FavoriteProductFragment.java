package com.example.loginapp.view.fragment.product_favorite;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.loginapp.R;
import com.example.loginapp.adapter.favorite_adapter.FavoriteAdapter;
import com.example.loginapp.adapter.favorite_adapter.FavoriteItemClickListener;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.databinding.FragmentFavoriteProductBinding;
import com.example.loginapp.presenter.FavoritePresenter;

import java.util.ArrayList;
import java.util.List;


public class FavoriteProductFragment extends Fragment implements FavoriteView, FavoriteItemClickListener {

    private FavoritePresenter presenter  = new FavoritePresenter(this);;

    private FragmentFavoriteProductBinding binding;
    private RecyclerView recyclerView;
    private final FavoriteAdapter adapter = new FavoriteAdapter(this);

    private final List<Product> wishlist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentFavoriteProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        presenter.getFavoriteProducts();

        binding.favoriteSwipe.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            binding.favoriteSwipe.setRefreshing(false);
        });
    }

    private void initView() {
        recyclerView = binding.favoriteRecyclerView;
        recyclerView.setAdapter(adapter);

        LinearLayout bottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemAdded(Product product) {
        wishlist.add(product);
        adapter.submitList(wishlist);
        adapter.notifyItemInserted(wishlist.size() - 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        wishlist.clear();
    }

    @Override
    public void onMessage(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putInt("productId", product.getId());
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_favoriteProductFragment_to_productFragment, bundle);
    }
}