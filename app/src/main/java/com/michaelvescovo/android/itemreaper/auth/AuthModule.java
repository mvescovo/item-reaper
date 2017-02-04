package com.michaelvescovo.android.itemreaper.auth;

import dagger.Module;
import dagger.Provides;

/**
 * @author Michael Vescovo
 */

@Module
class AuthModule {

    private AuthContract.View mView;

    AuthModule(AuthContract.View view) {
        mView = view;
    }

    @Provides
    AuthContract.View provideAuthView() {
        return mView;
    }
}
