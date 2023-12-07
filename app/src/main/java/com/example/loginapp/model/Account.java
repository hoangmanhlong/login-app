package com.example.loginapp.model;

import lombok.Data;

@Data
public class Account {
    private String email;
    private String password;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Account{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
