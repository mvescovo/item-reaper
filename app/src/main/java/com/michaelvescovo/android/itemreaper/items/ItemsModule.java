package com.michaelvescovo.android.itemreaper.items;

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
}
