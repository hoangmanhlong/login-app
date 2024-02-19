package com.example.loginapp.view.state;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class TextEditTextObserver implements TextWatcher {

    private final TextInputLayout layout;

    public TextEditTextObserver(TextInputLayout layout) {
        this.layout = layout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        layout.setErrorEnabled(s.toString().length() == 0);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
