package com.michaelvescovo.android.itemreaper.edit_item;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.michaelvescovo.android.itemreaper.util.ImageFile;
import com.michaelvescovo.android.itemreaper.util.ImageFileImpl;

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
        return new ImageFileImpl();
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
