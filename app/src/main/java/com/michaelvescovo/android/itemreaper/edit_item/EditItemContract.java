package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelvescovo.android.itemreaper.data.Item;

/**
 * @author Michael Vescovo
 */

public interface EditItemContract {

    interface View {

        void setPresenter(Presenter presenter);

        void setProgressBar(boolean active);

        void showExistingItem(Item item);

        void showItemsUi();

        void openCamera(Uri saveTo);

        void showImage(@NonNull String imageUrl);

        void showImageError();
    }

    interface Presenter {

        void editItem(@Nullable String itemId);

        void saveItem(@NonNull Item item);

        void doneEditing();

        void takePicture(Context context);

        void imageAvailable();

        void imageCaptureFailed();
    }
}
