package com.example.loginapp.presenter;

import com.example.loginapp.model.entity.Product;
import com.example.loginapp.model.interator.BottomSheetInterator;
import com.example.loginapp.model.listener.BottomSheetListener;
import com.example.loginapp.view.fragment.bottom_sheet.SheetView;

public class BottomSheetPresenter implements BottomSheetListener {
    private final SheetView view;
    private final BottomSheetInterator interator;

    public BottomSheetPresenter(SheetView view) {
        this.view = view;
        interator = new BottomSheetInterator(this);
    }

    public void getProduct(int id) {
        interator.getProduct(id);
    }
    @Override
    public void onLoadProduct(Product product) {
        view.onLoadProduct(product);
    }
}
