package com.example.loginapp.view.state.delivery_address_input_state;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.textfield.TextInputLayout;

public class PhoneNumberEditTextWatcher  implements TextWatcher {

    private String TAG = PhoneNumberEditTextWatcher.class.getName();

    private final TextInputLayout layout;

    public PhoneNumberEditTextWatcher(TextInputLayout layout) {
        this.layout = layout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        int length = s.toString().trim().length();
//        if (length < 10 || length > 12) {
//            layout.setErrorEnabled(true);
//            layout.setError("Invalid phone number");
//        } else {
//            layout.setErrorEnabled(false);
//        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
