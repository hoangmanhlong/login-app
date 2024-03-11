package com.example.loginapp.presenter;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.example.loginapp.App;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.view.fragments.select_language.SelectLanguageView;

public class SelectLanguagePresenter {

    private final AppSharedPreferences sharedPreferences;

    private final SelectLanguageView view;

    private boolean isVietnamese;

    private boolean isSelectedVietnamese;

    public SelectLanguagePresenter(SelectLanguageView view) {
        this.view= view;
        sharedPreferences = AppSharedPreferences.getInstance(App.getInstance());
    }

    public void initData() {
        isVietnamese = sharedPreferences.getLanguage();
        isSelectedVietnamese = isVietnamese;
        view.isVietnamese(isSelectedVietnamese);
    }

    public void setSelectedVietnamese(boolean selectedVietnamese) {
        isSelectedVietnamese = selectedVietnamese;
        view.isVietnamese(isSelectedVietnamese);
    }

    public void changeLanguage() {
        if (isSelectedVietnamese != isVietnamese) {
            sharedPreferences.setLanguage(isSelectedVietnamese);
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(isSelectedVietnamese ? "vi" : "en-US");
            AppCompatDelegate.setApplicationLocales(appLocale);
        }
    }
}
