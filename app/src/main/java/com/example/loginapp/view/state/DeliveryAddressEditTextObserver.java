package com.example.loginapp.view.state;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DeliveryAddressEditTextObserver implements TextWatcher {

    private final TextInputLayout layout;

    private final Button button;

    private final TextInputEditText[] editTexts;

    public DeliveryAddressEditTextObserver(Button button, TextInputLayout layout, TextInputEditText... editTexts) {
        this.button = button;
        this.layout = layout;
        this.editTexts = editTexts;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().trim().length() == 0) {
            layout.setErrorEnabled(true);
            button.setEnabled(false);
            layout.setError("Please enter complete information!");
        } else {
            layout.setErrorEnabled(false);
            button.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        checkFields();
    }

    private void checkFields() {
        // Enable the button only if all fields are not empty
        boolean allFieldsNotEmpty = true;
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText().toString())) {

                allFieldsNotEmpty = false;
                break;
            }
        }
        button.setEnabled(allFieldsNotEmpty);
    }
}
