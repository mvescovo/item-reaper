package com.michaelvescovo.android.itemreaper.edit_item;

import com.michaelvescovo.android.itemreaper.util.FakeImageFileImpl;
import com.michaelvescovo.android.itemreaper.util.ImageFile;

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

    @Provides
    ImageFile provideImageFile() {
        return new FakeImageFileImpl();
    }

    @Provides
    String provideUid() {
        return "testUid";
    }
}
