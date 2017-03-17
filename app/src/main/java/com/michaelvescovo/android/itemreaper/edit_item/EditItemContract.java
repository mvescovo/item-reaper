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

        void setNewItemId(String itemId);

        void showExistingItem(Item item);

        void showItemsUi();

        void openCamera(Uri saveTo);

        void openImagePicker();

        void showImage(@NonNull String imageUrl);

        void showImageError();

        void validateItem();

        void removeImage();

        void passDeletedItemToItemsUi();
    }

    interface Presenter {

        void editItem(@Nullable String itemId);

        void saveItem(@NonNull Item item);

        void doneEditing();

        void takePicture(Context context);

        void imageAvailable();

        void selectImage(Context context);

        void imageSelected(Uri uri);

        void imageCaptureFailed();

        void itemChanged();

        void deleteImage();

        void clearEditItemCache(String itemId);

        void deleteItem(@NonNull Item item);
    }
}
