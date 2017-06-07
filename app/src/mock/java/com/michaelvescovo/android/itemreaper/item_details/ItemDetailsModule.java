package com.michaelvescovo.android.itemreaper.item_details;

import dagger.Module;
import dagger.Provides;

import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.USER_ID;

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
        return USER_ID;
    }
}
