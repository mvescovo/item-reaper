package com.michaelvescovo.android.itemreaper.edit_item;

import dagger.Module;
import dagger.Provides;

/**
 * @author Michael Vescovo
 */

@Module
public class EditItemModule {

    private EditItemContract.View mView;

    public EditItemModule(EditItemContract.View view) {
        mView = view;
    }

    @Provides
    EditItemContract.View provideEditItemView() {
        return mView;
    }
}
