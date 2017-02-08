package com.michaelvescovo.android.itemreaper.items;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Module;
import dagger.Provides;

/**
 * @author Michael Vescovo
 */

@Module
public class ItemsModule {

    private ItemsContract.View mView;

    ItemsModule(ItemsContract.View view) {
        mView = view;
    }

    @Provides
    ItemsContract.View provideItemsView() {
        return mView;
    }

    @Provides
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
