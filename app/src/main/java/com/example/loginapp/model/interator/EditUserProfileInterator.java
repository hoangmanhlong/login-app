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

public class EditUserProfileInterator {

    private static final String TAG = EditUserProfileInterator.class.getSimpleName();

    private final EditUserProfileListener listener;

    public EditUserProfileInterator(EditUserProfileListener listener) {
        this.listener = listener;
    }

    public void saveUserData(@Nullable Uri uri, UserData userData) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference currentUserAvatarRef = Constant.avatarUserRef.child(currentUser.getUid());
        String uid = currentUser.getUid();
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(UserData.USERNAME, userData.getUsername());
        updates.put(UserData.PHONE_NUMBER, userData.getPhoneNumber());
        updates.put(UserData.ADDRESS, userData.getAddress());
        if (uri == null) {
            Constant.userRef.child(uid).updateChildren(updates)
                    .addOnFailureListener(e -> listener.isUpdateSuccess(false))
                    .addOnCompleteListener(s -> listener.isUpdateSuccess(s.isSuccessful()));
        } else {
            UploadTask uploadTask = currentUserAvatarRef.putFile(uri);
            uploadTask
                    .continueWithTask(task -> currentUserAvatarRef.getDownloadUrl())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.d(TAG, "saveUserData: " + downloadUri);
                            updates.put(UserData.AVATAR, downloadUri.toString());
                            Log.d(TAG, "saveUserData: " + updates);
                            Constant.userRef.child(uid).updateChildren(updates)
                                    .addOnCompleteListener(task1 -> listener.isUpdateSuccess(task1.isSuccessful()))
                                    .addOnFailureListener(e -> listener.isUpdateSuccess(false));
                        }
                    });
        }
    }
}
