package com.example.loginapp.view.fragments.search_test;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListPopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loginapp.R;
import com.example.loginapp.databinding.FragmentSearchBinding;
import com.example.loginapp.view.commonUI.HideKeyboard;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.Arrays;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private SearchView searchView;

    private SearchBar searchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HideKeyboard.setupHideKeyboard(view, requireActivity());

        searchView = binding.searchView;
        searchBar = binding.searchBar;

        searchView
                .getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            searchBar.setText(searchView.getText());
                            searchView.hide();
                            return false;
                        });


        ListPopupWindow  listPopupWindow =
                new ListPopupWindow(requireContext(), null, androidx.appcompat.R.attr.listPopupWindowStyle);

        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("Long", "Hoang", "Manh", "Hai", "Hoang", "Manh", "Hai"));

        ArrayAdapter adapter = new ArrayAdapter(requireContext(), R.layout.layout_textview, arrayList);
        listPopupWindow.setAdapter(adapter);

        listPopupWindow.setAnchorView(searchBar);

        listPopupWindow.setOnItemClickListener((parent, view1, position, id) -> {
            searchView.getEditText().setText(arrayList.get(position));

            // Dismiss popup.
            listPopupWindow.dismiss();
        });

        searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listPopupWindow.show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}