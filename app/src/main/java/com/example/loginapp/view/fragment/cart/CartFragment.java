package com.example.loginapp.view.fragment.cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.R;
import com.example.loginapp.adapter.SwipeToDeleteCallback;
import com.example.loginapp.adapter.cart_adapter.CartAdapter;
import com.example.loginapp.databinding.FragmentCartBinding;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.presenter.CartPresenter;

import java.util.List;

public class CartFragment extends Fragment implements CartView {

    private final String TAG = this.toString();

    private final CartPresenter presenter = new CartPresenter(this);

    private FragmentCartBinding binding;

    private RecyclerView recyclerView;

    private final CartAdapter adapter = new CartAdapter(presenter);;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.setFragment(this);
        LinearLayout bottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);

        recyclerView = binding.favoriteRecyclerView;
        recyclerView.setAdapter(adapter);
        adapter.submitList(presenter.basket);
//        enableSwipeToDeleteAndUndo();

        presenter.initBasket();
    }

    @Override
    public void onMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyItemAdded(List<FirebaseProduct> products) {
        adapter.submitList(products);
        adapter.notifyItemInserted(products.size() - 1);
    }

    @Override
    public void isCheckAllVisibility(boolean visibility, boolean allChecked) {
        if (visibility) {
            binding.checkAllView.setVisibility(View.VISIBLE);
            binding.checkAll.setChecked(allChecked);
        } else {
            binding.checkAllView.setVisibility(View.GONE);
        }
    }

    @Override
    public void notifyItemRemoved(int index) {
        adapter.notifyItemRemoved(index);
    }

    @Override
    public void notifyItemChanged(int index) {
        adapter.notifyItemChanged(index);
    }


    @Override
    public void onItemClick(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("productId", id);
        Navigation.findNavController(binding.getRoot())
            .navigate(R.id.action_cartFragment_to_productFragment, bundle);
    }

    @Override
    public void setCheckAllChecked(boolean isChecked) {
        binding.checkAll.setChecked(isChecked);
    }

    @Override
    public void showCheckoutView(Boolean check) {
        if (check) binding.checkoutView.setVisibility(View.VISIBLE);
        else binding.checkoutView.setVisibility(View.GONE);
    }

    public void onCheckboxAllClick() {
        presenter.updateCheckboxAllSelected(binding.checkAll.isChecked());
    }

    @Override
    public void onDeleteProduct(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(R.string.dialog_message)
            .setPositiveButton(
                R.string.positive_button_title,
                (dialog, which) -> {
                    presenter.deleteProductInFirebase(id);
                }
            )
            .setNegativeButton(R.string.negative_button_title, (dialog, which) -> {
            })

            .setCancelable(false)
            .show();
    }

    @Override
    public void setTotal(String total, String quantity) {
        binding.priceTotal.setText(requireContext().getString(R.string.price_format, total));
        binding.productQuantity.setText(requireContext().getString(R.string.quantity_item, quantity));
    }

    public void onBuyClick() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_cartFragment_to_checkoutInfoFragment);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(requireContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                adapter.removeItem(position);
                adapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}