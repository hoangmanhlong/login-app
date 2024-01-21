package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.data.remote.service.Constant;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.UserProfileListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileInterator {
    private final UserProfileListener listener;

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final DatabaseReference useRef =
        FirebaseDatabase.getInstance().getReference().child(Constant.USER_REF);

    public UserProfileInterator(UserProfileListener listener) {
        this.listener = listener;
    }

    public void getUserData() {
        assert currentUser != null;
        useRef.child(currentUser.getUid()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
                UserData user = task.getResult().getValue(UserData.class);
                listener.getUserData(user);
            }
        });
    }
}
