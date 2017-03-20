package com.michaelvescovo.android.itemreaper.item_details;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Module;
import dagger.Provides;

/**
 * @author Michael Vescovo
 */

@Module
public class ItemDetailsModule {

    private ItemDetailsContract.View mView;

    public ItemDetailsModule(ItemDetailsContract.View view) {
        mView = view;
    }

    @Provides
    ItemDetailsContract.View provideItemDetailsView() {
        return mView;
    }

    @Provides
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
