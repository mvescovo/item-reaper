package com.michaelvescovo.android.itemreaper.auth;

import android.support.annotation.Nullable;

/**
 * @author Michael Vescovo
 */

public interface AuthContract {

    interface View {

        void setPresenter(Presenter presenter);

        void setProgressIndicator(boolean active);

        void showSignInButton(boolean visible);

        void showGoogleSignInUi();

        void showFailMessage();

        void showFireBaseAuthUi();

        void showItemsUi();
    }

    interface Presenter {

        void googleSignIn();

        void handleGoogleSignInResult(boolean signedIn, @Nullable String userId);

        void handleFirebaseSignInResult(boolean signedIn);
    }
}
