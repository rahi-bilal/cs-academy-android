package com.ansaridroid.csacademy.util.authentication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.Toast;


import com.ansaridroid.csacademy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Authenticate {
    private static FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private Context context;
    private static String userToken;

    private OnFragmentInteractionListener mListener;


    public Authenticate(Context context) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        Authenticate.user = firebaseAuth.getCurrentUser();

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }else {
            Toast.makeText(context, "Listener not implemented", Toast.LENGTH_SHORT).show();
        }
        this.context = context;
    }

    public FirebaseUser getCurrentUser() {
        Authenticate.user = firebaseAuth.getCurrentUser();
        return Authenticate.user;
    }

    public void login(String email, String password) {
        mListener.showProgressBar();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Authenticate.user = firebaseAuth.getCurrentUser();
                            mListener.updateUI(Authenticate.user);
                        }else {
                            Authenticate.user = null;
                            mListener.showMessage("Invalid Username or Password");
                        }
                        mListener.hideProgressBar();
                    }
                });
    }

    public void logoutUser() {
        firebaseAuth.signOut();
        Authenticate.user = firebaseAuth.getCurrentUser();
        mListener.updateUI(Authenticate.user);
    }

    public void createUser(String email, String password, final String displayName) {
        mListener.showProgressBar();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Authenticate.user = firebaseAuth.getCurrentUser();
                    updateDisplayName(user, displayName);
                } else {
                    Authenticate.user = null;
                    mListener.showMessage("Invalid Email");
                    mListener.hideProgressBar();

                }
            }
        });
    }

    private void updateDisplayName(FirebaseUser newUser, String displayName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        newUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()) {
                           Authenticate.user = firebaseAuth.getCurrentUser();
                       }else {
                           Authenticate.user = null;
                       }
                       mListener.updateUI(Authenticate.user);
                       mListener.hideProgressBar();
                    }
                });
    }

    public void sendVerificationLink() {
        mListener.showProgressBar();
        Authenticate.user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mListener.showMessage(context.getResources().getString(R.string.verify_link_send));
                            firebaseAuth.signOut();
                            Authenticate.user = firebaseAuth.getCurrentUser();
                        }else {
                            mListener.showMessage(context.getResources().getString(R.string.something_went_wrong)+ ": " + task.getException().toString());
                        }
                        mListener.hideProgressBar();
                    }
                });
    }

    public String getUserToken() {
        Authenticate.user.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {

                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            userToken = task.getResult().getToken();
                        } else {
                            userToken = task.getException().toString();
                        }
                    }
                });
        return userToken;
    }
}
