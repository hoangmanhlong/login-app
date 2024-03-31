package com.example.loginapp.model.interator;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.EditUserProfileListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditUserProfileInteractor {

    private static final String TAG = EditUserProfileInteractor.class.getSimpleName();

    private EditUserProfileListener listener;

    public EditUserProfileInteractor(EditUserProfileListener listener) {
        this.listener = listener;
    }

    public void clear() {
        listener = null;
    }

    public void saveUserData(@Nullable Uri uri, UserData userData) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            StorageReference currentUserAvatarRef = Constant.avatarUserRef.child(uid);
            HashMap<String, Object> updates = new HashMap<>();
            updates.put(UserData.USERNAME, userData.getUsername());
            updates.put(UserData.PHONE_NUMBER, userData.getPhoneNumber());
            updates.put(UserData.ADDRESS, userData.getAddress());
            if (uri == null) {
                Constant.userRef.child(uid).updateChildren(updates)
                        .addOnFailureListener(e -> {
                            listener.isUpdateSuccess(false);
                            Log.d(TAG, "saveUserData: " + e.getMessage());
                        })
                        .addOnCompleteListener(s -> listener.isUpdateSuccess(s.isSuccessful()));
            } else {
                UploadTask uploadTask = currentUserAvatarRef.putFile(uri);
                uploadTask
                        .continueWithTask(task -> currentUserAvatarRef.getDownloadUrl())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                updates.put(UserData.AVATAR, downloadUri.toString());

                                Constant.userRef.child(uid).updateChildren(updates)
                                        .addOnCompleteListener(task1 -> listener.isUpdateSuccess(task1.isSuccessful()))
                                        .addOnFailureListener(e -> {
                                            listener.isUpdateSuccess(false);
                                            Log.d(TAG, "saveUserData: " + e.getMessage());
                                        });
                            }
                        });
            }
        }
    }
}
