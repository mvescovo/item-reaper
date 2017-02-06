package com.michaelvescovo.android.itemreaper;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    SharedPreferencesHelper getSharedPreferencesHelper();
}
