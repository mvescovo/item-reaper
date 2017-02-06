package com.michaelvescovo.android.itemreaper.auth;

import com.michaelvescovo.android.itemreaper.ApplicationComponent;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@Component(modules = AuthModule.class, dependencies = ApplicationComponent.class)
interface AuthComponent {

    AuthPresenter getAuthPresenter();
}
