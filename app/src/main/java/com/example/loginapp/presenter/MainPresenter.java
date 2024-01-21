package com.example.loginapp.presenter;

import com.example.loginapp.model.interator.MainInteractor;
import com.example.loginapp.model.listener.MainListener;
import com.example.loginapp.view.activities.MainView;

public class MainPresenter implements MainListener {
    private MainInteractor interactor;

    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
        interactor = new MainInteractor(this);
        interactor.getNumber();
    }

    @Override
    public void onLoadFavoriteProducts(int number) {
        view.onLoadFavoriteProducts(number);
    }

    @Override
    public void onLoadBasket(int number) {
        view.onLoadBasket(number);
    }
}
