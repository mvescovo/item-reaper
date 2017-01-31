package com.michaelvescovo.android.itemreaper;

import android.app.Application;

import com.michaelvescovo.android.itemreaper.data.DaggerRepositoryComponent;
import com.michaelvescovo.android.itemreaper.data.RepositoryComponent;

/**
 * @author Michael Vescovo
 */

public class ItemReaperApplication extends Application {

    private RepositoryComponent mRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mRepositoryComponent = DaggerRepositoryComponent.builder()
                .build();

    }

    public RepositoryComponent getRepositoryComponent() {
        return mRepositoryComponent;
    }
}
