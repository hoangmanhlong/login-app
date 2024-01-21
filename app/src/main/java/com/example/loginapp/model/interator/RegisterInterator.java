package com.example.loginapp.model.interator;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.RegisterListener;
import com.example.loginapp.data.remote.service.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterInterator {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final DatabaseReference userRef =
        FirebaseDatabase.getInstance().getReference().child(Constant.USER_REF);

    private RegisterListener listener;

    public RegisterInterator(RegisterListener listener) {
        this.listener = listener;

    }

    public void register(
        String email,
        String password,
        Activity activity,
        String confirmPassword
    ) {
        listener.onShowProcessBar(true);
        if (!isValidEmail(email)) {
            listener.onRegisterMessage("Email format is wrong, Please re-enter");
            listener.onShowProcessBar(false);
        } else if (!isPasswordValid(password)) {
            listener.onRegisterMessage("Password must be more than 6 characters");
            listener.onShowProcessBar(false);
        } else if (!password.equals(confirmPassword)) {
            listener.onRegisterMessage("Passwords are not duplicates");
            listener.onShowProcessBar(false);
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getUid();
                            UserData userData = new UserData(uid, email, password);
                            userRef.child(uid).setValue(userData);
                            // Sign in success, update UI with the signed-in user's information
                            listener.goLoginScreen();
                            listener.onRegisterMessage("Sign Up Success");
                            listener.onShowProcessBar(false);
                        } else {
                            task.getException().getMessage();
                            listener.onRegisterMessage("Account already exists");
                            listener.onShowProcessBar(false);
                        }
                    }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        }

    }

    private boolean isValidEmail(String email) {
        // Biểu thức chính quy để kiểm tra địa chỉ email
        String emailRegex =
//            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        // Tạo một đối tượng Pattern từ biểu thức chính quy
        Pattern pattern = Pattern.compile(emailRegex);

        // Tạo một đối tượng Matcher từ đối tượng Pattern và đầu vào email
        Matcher matcher = pattern.matcher(email);

        // Kiểm tra xem địa chỉ email có khớp với biểu thức chính quy không
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

}
