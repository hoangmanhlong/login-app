package com.example.loginapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils {

    public static boolean isValidEmail(String email) {
        String emailRegex =
//            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\" +
                        ".[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isNumber(String text) {
        return Pattern.matches("[0-9]+", text);
    }
}
