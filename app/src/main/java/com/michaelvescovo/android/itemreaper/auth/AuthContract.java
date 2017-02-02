package com.michaelvescovo.android.itemreaper.auth;

import android.content.Intent;

/**
 * @author Michael Vescovo
 */

public interface AuthContract {

    interface View {

        void setPresenter(Presenter presenter);

        void setProgressIndicator(boolean active);

        void showSignInUi(Intent signInIntent, int requestCode);

        void showItemsUi();
    }

    interface Presenter {

        void signIn();
    }
}
