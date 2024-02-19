package com.example.loginapp.model.interator;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.EditUserProfileListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EditUserProfileInterator {

    private final DatabaseReference userRef = Constant.userRef;

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private StorageReference storageRef = storage.getReference();

    private final EditUserProfileListener listener;

    public EditUserProfileInterator(EditUserProfileListener listener) {
        this.listener = listener;
    }

    public void uploadImageToFirebase(@Nullable Uri uri, String username, String phoneNumber, String address) {
        AtomicInteger successCount = new AtomicInteger(0);
        String uid = Constant.currentUser.getUid();
        Map<String, Object> updates = new HashMap<>();
        updates.put(UserData.USERNAME, username);
        updates.put(UserData.PHONE_NUMBER, phoneNumber);
        updates.put(UserData.ADDRESS, address);
        if (uri == null) {
            userRef.child(uid).updateChildren(updates)
                    .addOnFailureListener(e -> listener.isUpdateSuccess(false))
                    .addOnCompleteListener(s -> listener.isUpdateSuccess(s.isSuccessful()));
        } else {
            storageRef = storageRef.child(currentUser.getUid());
            UploadTask uploadTask = storageRef.putFile(uri);
            uploadTask
                    .continueWithTask(task -> storageRef.getDownloadUrl())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            userRef.child(uid).updateChildren(updates)
                                    .addOnCompleteListener(s -> {
                                        if (s.isSuccessful()) successCount.getAndIncrement();
                                        if (successCount.get() == 2) listener.isUpdateSuccess(true);
                                    });
                            userRef.child(uid).child(UserData.PHOTO_URL).setValue(downloadUri.toString())
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) successCount.getAndIncrement();
                                        if (successCount.get() == 2) listener.isUpdateSuccess(true);
                                    })
                                    .addOnFailureListener(e -> listener.isUpdateSuccess(false));
                        }
                    });
        }
    }
}
