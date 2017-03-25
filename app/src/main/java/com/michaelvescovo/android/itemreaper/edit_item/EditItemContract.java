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
    }

    interface Presenter {

        void editItem(@Nullable String itemId);

        void saveItem(@NonNull Item item);

        void doneEditing();

        void takePicture(Context context);

        void imageAvailable(@NonNull Context context, @NonNull ImageFile imageFile,
                            @Nullable String imageUrl);

        void selectImage(Context context);

        void imageSelected(Context context, String imageUrl, Uri uri);

        void imageCaptureFailed();

        void itemChanged();

        void deleteImage(Context context, String imageUrl);

        void deleteFile(Context context, String imageUrl);

        void clearEditItemCache(String itemId);

        void deleteItem(@NonNull Item item);
    }
}
