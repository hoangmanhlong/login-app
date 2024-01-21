package com.example.loginapp.view.state;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

public class SaveUserDataButtonObserver implements TextWatcher {

    private final Button button;

    public SaveUserDataButtonObserver(Button button) {
        this.button = button;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        button.setEnabled(s.length() != 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
