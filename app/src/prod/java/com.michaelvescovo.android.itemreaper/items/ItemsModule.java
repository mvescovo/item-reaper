package com.michaelvescovo.android.itemreaper.items;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dagger.Module;
import dagger.Provides;

/**
 * @author Michael Vescovo
 */

@Module
class ItemsModule {

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

    @Provides
    String provideUid() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.getUid();
        } else {
            return null;
        }
    }
}
