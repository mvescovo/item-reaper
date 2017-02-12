package com.michaelvescovo.android.itemreaper;

import android.app.Application;
import android.preference.PreferenceManager;

import com.michaelvescovo.android.itemreaper.data.DaggerRepositoryComponent;
import com.michaelvescovo.android.itemreaper.data.RepositoryComponent;

/**
 * @author Michael Vescovo
 */

public class ItemReaperApplication extends Application {

    private ApplicationComponent mApplicationComponent;
    private RepositoryComponent mRepositoryComponent;

    static {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(
                new ApplicationModule(PreferenceManager.getDefaultSharedPreferences(this))).build();
        mRepositoryComponent = DaggerRepositoryComponent.builder().build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public RepositoryComponent getRepositoryComponent() {
        return mRepositoryComponent;
    }
}
