package com.example.loginapp.view.fragments.select_language;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.databinding.FragmentSelectLanguageBinding;
import com.example.loginapp.presenter.SelectLanguagePresenter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class SelectLanguageFragment extends BottomSheetDialogFragment implements SelectLanguageView {

    public static final String TAG = SelectLanguageFragment.class.getSimpleName();

    private SelectLanguagePresenter presenter;

    private FragmentSelectLanguageBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SelectLanguagePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectLanguageBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initData();
    }

    public void onSaveButtonClick() {
        this.dismiss();
        presenter.changeLanguage();
    }

    public void onVietnameseViewClick() {
        presenter.setSelectedVietnamese(true);
    }

    public void onEnglishClick() {
        presenter.setSelectedVietnamese(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void isVietnamese(boolean isVietnamese) {
        binding.setIsVietnamese(isVietnamese);
    }
}