package com.michaelvescovo.android.itemreaper.item_details;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    String provideUserId() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            return firebaseUser.getUid();
        } else {
            return null;
        }
    }
}
