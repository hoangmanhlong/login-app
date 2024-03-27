package com.example.loginapp.presenter;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.example.loginapp.App;
import com.example.loginapp.data.local.AppSharedPreferences;
import com.example.loginapp.utils.Constant;
import com.example.loginapp.view.fragments.select_language.SelectLanguageView;

public class SelectLanguagePresenter {

    private AppSharedPreferences sharedPreferences;

    private SelectLanguageView view;

    private boolean isVietnamese;

    private boolean isSelectedVietnamese;

    public SelectLanguagePresenter(SelectLanguageView view) {
        this.view = view;
        sharedPreferences = AppSharedPreferences.getInstance(App.getInstance());
    }

    public void detachView() {
        view = null;
        sharedPreferences = null;
    }

    public void initData() {
        isVietnamese = sharedPreferences.getLanguage();
        isSelectedVietnamese = isVietnamese;
        if (view != null) view.isVietnamese(isSelectedVietnamese);
    }

    public void setSelectedVietnamese(boolean selectedVietnamese) {
        isSelectedVietnamese = selectedVietnamese;
        view.isVietnamese(isSelectedVietnamese);
    }

    public void changeLanguage() {
        if (isSelectedVietnamese != isVietnamese) {
            sharedPreferences.setLanguage(isSelectedVietnamese);
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(
                    isSelectedVietnamese ?
                            Constant.VIETNAM_COUNTRY_CODE : Constant.ENGLISH_COUNTRY_CODE
            );
            AppCompatDelegate.setApplicationLocales(appLocale);
        }
    }
}
