package com.example.loginapp.model.interator;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.data.Constant;
import com.example.loginapp.model.entity.UserData;
import com.example.loginapp.model.listener.VerificationListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerificationInterator {
    private String TAG = this.toString();

    private final VerificationListener listener;

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final DatabaseReference userRef = Constant.userRef;

    private String mVerificationId;

    private String phoneNumberTemp;

    private Activity activity;

    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    Log.d(TAG, "onVerificationCompleted:" + credential);
                    signInWithPhoneAuthCredential(credential, activity);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
//                    Log.w(TAG, "onVerificationFailed", e);

//                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                        // Invalid request
//                    } else if (e instanceof FirebaseTooManyRequestsException) {
//                        // The SMS quota for the project has been exceeded
//                    } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
//                        // reCAPTCHA verification attempted with null Activity
//                    }

                    // Show a message and update the UI
                }

                @Override
                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d(TAG, "onCodeSent:" + verificationId);

                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;

                    listener.enableVerifyButton();

                }
            };

    public VerificationInterator(VerificationListener listener) {
        this.listener = listener;
    }

//    public void startPhoneNumberVerification(String phoneNumber, Activity activity) {
//        phoneNumberTemp = phoneNumber;
//        // [START start_phone_auth]
//        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
//                .setPhoneNumber(phoneNumber)       // Phone number to verify
//                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                .setActivity(activity)                 // (optional) Activity for callback binding
//                // If no activity is passed, reCAPTCHA verification can not be used.
//                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//        // [END start_phone_auth]
//    }

        public void startPhoneNumberVerification(String phoneNumber, Activity activity) {
        this.activity = activity;
        String phoneNumberTest = "+16668889999";
        String smsCode = "123456";
        phoneNumberTemp = phoneNumberTest;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

// Configure faking the auto-retrieval with the whitelisted numbers.
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumberTest, smsCode);

        // [START start_phone_auth]
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumberTest)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity)                 // (optional) Activity for callback binding
                // If no activity is passed, reCAPTCHA verification can not be used.
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    public void verifyPhoneNumberWithCode(String code, Activity activity) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential, activity);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, Activity activity) {
        // Force reCAPTCHA flow
        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                String uid = mAuth.getUid();
                UserData userData = new UserData(uid, "", "", "", phoneNumberTemp, "", "");
                userRef.child(uid).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.goHomeScreen();
                    }
                });
            } else {
                // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                }
            }
        });
    }
}
