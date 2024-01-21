package com.example.loginapp.model.interator;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.data.remote.service.Constant;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.EditUserProfileListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditUserProfileInterator {
    private static final String TAG = "EditUserProfileInterator";

    private final DatabaseReference userRef =
        FirebaseDatabase.getInstance().getReference().child(Constant.USER_REF);

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private StorageReference storageRef = storage.getReference();

    private EditUserProfileListener listener;

    public EditUserProfileInterator(EditUserProfileListener listener) {
        this.listener = listener;
    }

    public void getUserData() {
        assert currentUser != null;
        userRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.getUserData(snapshot.getValue(UserData.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void uploadImageToFirebase(
        @Nullable Uri uri,
        String username,
        String phoneNumber,
        String address
    ) {
        if (username.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            listener.onMessage("Please enter complete information");
            listener.showProcessBar(false);
        } else {
            listener.showProcessBar(true);
            if (uri == null) {
                assert currentUser != null;
                userRef.child(currentUser.getUid()).child(UserData.USERNAME).setValue(username);
                userRef.child(currentUser.getUid()).child(UserData.PHONE_NUMBER).setValue(phoneNumber);
                userRef.child(currentUser.getUid()).child(UserData.ADDRESS).setValue(address);
                listener.showProcessBar(false);
                listener.goUserProfile();
            } else {
                storageRef = storageRef.child(currentUser.getUid());
                UploadTask uploadTask = storageRef.putFile(uri);
                uploadTask.addOnFailureListener(exception -> {

                }).addOnSuccessListener(taskSnapshot -> {

                }).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        userRef.child(currentUser.getUid()).child(UserData.USERNAME).setValue(username);
                        userRef.child(currentUser.getUid()).child(UserData.PHONE_NUMBER).setValue(phoneNumber);
                        userRef.child(currentUser.getUid()).child(UserData.ADDRESS).setValue(address);
                        userRef.child(currentUser.getUid()).child(UserData.PHOTO_URL).setValue(downloadUri.toString());
                        listener.showProcessBar(false);
                        listener.goUserProfile();
                    } else {
                        listener.showProcessBar(false);
                    }
                });
            }
        }
    }
}
