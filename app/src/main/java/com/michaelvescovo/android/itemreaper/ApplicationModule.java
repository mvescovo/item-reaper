package com.michaelvescovo.android.itemreaper;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import dagger.Module;
import dagger.Provides;

/**
 * @author Michael Vescovo
 */

@Module
class ApplicationModule {

    private SharedPreferences mSharedPreferences;

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

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
