package com.example.loginapp.view.state;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ClearButtonObserver implements TextWatcher {

    private Button button;

    private EditText editText;

    public ClearButtonObserver(Button button, EditText editText) {
        this.button = button;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().trim().isEmpty()) {
            button.setVisibility(View.GONE);
        } else button.setVisibility(View.VISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(TextUtils.isEmpty(editText.getText().toString())) {
            button.setVisibility(View.GONE);
        }
    }
}
