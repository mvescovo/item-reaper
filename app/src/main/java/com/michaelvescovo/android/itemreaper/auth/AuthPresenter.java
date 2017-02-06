package com.michaelvescovo.android.itemreaper.auth;

import android.support.annotation.Nullable;

import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

class AuthPresenter implements AuthContract.Presenter {

    private AuthContract.View mView;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Inject
    AuthPresenter(AuthContract.View view, SharedPreferencesHelper sharedPreferencesHelper) {
        mView = view;
        mSharedPreferencesHelper = sharedPreferencesHelper;
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void googleSignIn() {
        mView.showSignInButton(false);
        mView.setProgressIndicator(true);
        mView.showGoogleSignInUi();
    }

    @Override
    public void handleGoogleSignInResult(boolean signedIn, @Nullable String userId) {
        if (signedIn) {
            if (userId != null) {
                mSharedPreferencesHelper.saveUserId(userId);
            }
            mView.showFireBaseAuthUi();
        } else {
            mView.setProgressIndicator(false);
            mView.showSignInButton(true);
            mView.showFailMessage();
        }
    }

    @Override
    public void handleFirebaseSignInResult(boolean signedIn) {
        mView.setProgressIndicator(false);
        mView.showSignInButton(true);
        if (signedIn) {
            mView.showItemsUi();
        } else {
            mView.showFailMessage();
        }
    }
}
