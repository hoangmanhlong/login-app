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

    private Boolean isVietnamese;

    private Boolean isSelectedVietnamese;

    public SelectLanguagePresenter(SelectLanguageView view) {
        this.view = view;
        sharedPreferences = AppSharedPreferences.getInstance(App.getInstance());
    }

    public void detachView() {
        view = null;
        sharedPreferences = null;
        isSelectedVietnamese = null;
        isVietnamese = null;
    }

    public void initData() {
        isVietnamese = sharedPreferences.getBoolean(Constant.IS_VIETNAMESE_LANGUAGE);
        isSelectedVietnamese = isVietnamese;
        if (view != null) view.isVietnamese(isSelectedVietnamese);
    }

    public void setSelectedVietnamese(boolean selectedVietnamese) {
        isSelectedVietnamese = selectedVietnamese;
        view.isVietnamese(isSelectedVietnamese);
    }

    public void changeLanguage() {
        if (isSelectedVietnamese != isVietnamese) {
            sharedPreferences.putBoolean(Constant.IS_VIETNAMESE_LANGUAGE, isSelectedVietnamese);
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(
                    isSelectedVietnamese ?
                            Constant.VIETNAM_COUNTRY_CODE : Constant.ENGLISH_COUNTRY_CODE
            );
            AppCompatDelegate.setApplicationLocales(appLocale);
        }
    }
}
