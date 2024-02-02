package com.example.loginapp.presenter;

import com.example.loginapp.adapter.cart_adapter.CartItemClickListener;
import com.example.loginapp.model.entity.FirebaseProduct;
import com.example.loginapp.model.entity.OrderProduct;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.entity.Voucher;
import com.example.loginapp.model.interator.CartInterator;
import com.example.loginapp.model.listener.CartListener;
import com.example.loginapp.view.fragment.cart.CartView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartPresenter implements CartListener, CartItemClickListener {

    private final CartInterator interator = new CartInterator(this);

    private final CartView view;

    public final List<FirebaseProduct> basket = new ArrayList<>();

    public List<FirebaseProduct> selectedProduct = new ArrayList<>();

    public Voucher seletedVoucher;

    public CartPresenter(CartView view) {
        this.view = view;
    }

    public void initBasket() {
        updateUI();
        if (basket.size() == 0) interator.getFavoriteProductFromFirebase();
        else view.notifyItemAdded(basket);
    }

    @Override
    public void notifyItemAdded(FirebaseProduct product, String previousChildName) {
        basket.add(product);
        updateUI();
        view.notifyItemAdded(basket);
    }

    @Override
    public void notifyItemRemoved(FirebaseProduct product) {
        int index = basket.indexOf(basket.stream().filter(p -> p.getId() == product.getId()).collect(Collectors.toList()).get(0));
        basket.remove(index);
        updateUI();
        view.notifyItemRemoved(index);
    }

    @Override
    public void notifyItemChanged(FirebaseProduct product, String previousChildName) {
        int index = basket.indexOf(basket.stream().filter(p -> p.getId() == product.getId()).collect(Collectors.toList()).get(0));
        basket.set(index, product);
        updateUI();
        view.notifyItemChanged(index);
    }

    @Override
    public void onMessage(String message) {
        view.onMessage(message);
    }

    public void deleteProductInFirebase(int id) {
        interator.deleteProductInFirebase(id);
    }

    @Override
    public void onItemClick(Product product) {
        view.onItemClick(product);
    }

    @Override
    public void updateQuantity(int id, int quantity) {
        interator.updateQuantity(id, quantity);
    }

    @Override
    public void onItemChecked(FirebaseProduct product, boolean checked) {
        if (checked) {
            interator.updateChecked(product.getId(), true);
        } else {
            view.setCheckAllChecked(false);
            interator.updateChecked(product.getId(), false);
        }
    }

    private void updateUI() {
        int totalTemp;
        List<FirebaseProduct> selectedProduct = basket.stream().filter(FirebaseProduct::isChecked).collect(Collectors.toList());
        totalTemp = selectedProduct.stream().mapToInt(product -> product.getPrice() * Integer.parseInt(product.getQuantity())).sum();
        view.isCheckAllVisibility(basket.size() > 0, selectedProduct.size() == basket.size());
        view.showCheckoutView(totalTemp != 0);
        view.setTotal(String.valueOf(totalTemp), String.valueOf(selectedProduct.size()));
        this.selectedProduct = selectedProduct;
    }

    @Override
    public void onDeleteProduct(int id) {
        view.onDeleteProduct(id);
    }

    public void updateCheckboxAllSelected(Boolean checked) {
        for (FirebaseProduct product : basket) {
            interator.updateChecked(product.getId(), checked);
        }
    }

    public List<OrderProduct> listProduct() {
        List<OrderProduct> list = new ArrayList<>();
        for (FirebaseProduct product : selectedProduct) list.add(product.toOrderProduct());
        return list;
    }
}
