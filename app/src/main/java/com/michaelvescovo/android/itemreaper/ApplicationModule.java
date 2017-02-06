package com.michaelvescovo.android.itemreaper;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

/**
 * @author Michael Vescovo
 */

@Module
class ApplicationModule {

    private SharedPreferences mSharedPreferences;

    ApplicationModule(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Provides
    SharedPreferences provideSharedPreferences() {
        return mSharedPreferences;
    }

    @Provides
    SharedPreferencesHelper provideSharedPreferencesHelper(SharedPreferences sharedPreferences) {
        return new SharedPreferencesHelper(sharedPreferences);
    }
}
