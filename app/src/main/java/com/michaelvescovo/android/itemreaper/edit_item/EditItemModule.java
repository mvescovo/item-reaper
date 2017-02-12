package com.michaelvescovo.android.itemreaper.edit_item;

import dagger.Module;
import dagger.Provides;

/**
 * @author Michael Vescovo
 */

@Module
class EditItemModule {

    private EditItemContract.View mView;

    EditItemModule(EditItemContract.View view) {
        mView = view;
    }

    @Provides
    EditItemContract.View provideEditItemView() {
        return mView;
    }
}
