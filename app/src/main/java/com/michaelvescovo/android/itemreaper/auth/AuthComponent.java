package com.michaelvescovo.android.itemreaper.auth;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@Component(modules = AuthModule.class)
interface AuthComponent {

    AuthPresenter getAuthPresenter();
}
