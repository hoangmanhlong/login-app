package com.example.loginapp.model.interator;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.loginapp.data.Constant;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.EditUserProfileListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
        listener.showProcessBar(true);
        String uid = Constant.currentUser.getUid();
        if (uri == null) {
            setInfo(uid, username, phoneNumber, address);
            listener.showProcessBar(false);
            listener.goUserProfile();
        } else {
            storageRef = storageRef.child(currentUser.getUid());
            UploadTask uploadTask = storageRef.putFile(uri);
            uploadTask
                    .continueWithTask(task -> storageRef.getDownloadUrl())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            setInfo(uid, username, phoneNumber, address);
                            userRef.child(currentUser.getUid()).child(UserData.PHOTO_URL).setValue(downloadUri.toString());
                            listener.showProcessBar(false);
                            listener.goUserProfile();
                        } else {
                            listener.showProcessBar(false);
                        }
                    });
        }
    }


    public void setInfo(String uid, String username, String phoneNumber, String address) {
        userRef.child(uid).child(UserData.USERNAME).setValue(username);
        userRef.child(uid).child(UserData.PHONE_NUMBER).setValue(phoneNumber);
        userRef.child(uid).child(UserData.ADDRESS).setValue(address);
    }
}
