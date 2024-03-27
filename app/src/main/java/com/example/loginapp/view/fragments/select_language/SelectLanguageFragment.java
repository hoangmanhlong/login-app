package com.example.loginapp.view.fragments.select_language;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.loginapp.databinding.FragmentSelectLanguageBinding;
import com.example.loginapp.presenter.SelectLanguagePresenter;


public class SelectLanguageFragment extends Fragment implements SelectLanguageView {

    private static final String TAG = SelectLanguageFragment.class.getSimpleName();

    private SelectLanguagePresenter presenter;

    private FragmentSelectLanguageBinding binding;

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
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

    public void onNavigateUp() {
        navController.navigateUp();
    }

    public void onSaveButtonClick() {
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