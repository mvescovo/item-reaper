package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.util.ImageFile;

/**
 * @author Michael Vescovo
 */

public interface EditItemContract {

    interface View {

        void setPresenter(Presenter presenter);

        void setProgressBar(boolean active);

        void setInteractionEnabled(boolean enabled);

        void setNewItemId(String itemId);

        void setDefaultDates();

        void showExistingItem(Item item);

        void showItemsUi();

        void openCamera(ImageFile imageFile);

        void openImagePicker();

        void showImage(@NonNull String imageUrl);

        void showImageError();

        void validateItem();

        void removeImage();

        void passDeletedItemToItemsUi();

        void compressImage(@NonNull String imageUrl);
    }

    interface Presenter {

        void editItem(@Nullable String itemId);

        void saveItem(@NonNull Item item);

        void doneEditing();

        void takePicture(Context context);

        void imageAvailable(@NonNull ImageFile imageFile);

        void selectImage();

        void imageSelected(Context context, Uri uri);

        void imageCaptureFailed(@NonNull Context context, @Nullable ImageFile imageFile);

        void itemChanged();

        void deleteImage();

        void clearEditItemCache(String itemId);

        void deleteItem(@NonNull Item item);

        void imageCompressed(@NonNull String imageUrl);
    }
}
