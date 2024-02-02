package com.example.loginapp.view.state;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPhoneNumberButtonObserver implements TextWatcher {


    private Button button;

//    private EditText editText;


    public LoginPhoneNumberButtonObserver(Button button) {
        this.button = button;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = s.toString().trim();
        button.setEnabled(text.length() == 10 && text.startsWith("0"));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }



}
