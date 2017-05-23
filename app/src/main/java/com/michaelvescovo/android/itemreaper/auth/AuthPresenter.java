package com.michaelvescovo.android.itemreaper.auth;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

class AuthPresenter implements AuthContract.Presenter {

    private AuthContract.View mView;

    @Inject
    AuthPresenter(AuthContract.View view) {
        mView = view;
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
    public void handleGoogleSignInResult(boolean signedIn) {
        if (signedIn) {
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
        if (signedIn) {
            mView.closeAuthUi();
            mView.updateWidget();
        } else {
            mView.showSignInButton(true);
            mView.showFailMessage();
        }
    }
}
