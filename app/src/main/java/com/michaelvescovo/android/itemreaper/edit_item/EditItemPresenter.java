package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;
import com.michaelvescovo.android.itemreaper.util.ImageFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.EDIT_ITEM_CALLER;

/**
 * @author Michael Vescovo
 */

class EditItemPresenter implements EditItemContract.Presenter {

    private EditItemContract.View mView;
    private Repository mRepository;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private ImageFile mImageFile;
    private boolean itemLoaded;

    @Inject
    EditItemPresenter(@NonNull EditItemContract.View view, @NonNull Repository repository,
                      @NonNull SharedPreferencesHelper sharedPreferencesHelper,
                      @NonNull ImageFile imageFile) {
        mView = view;
        mRepository = repository;
        mSharedPreferencesHelper = sharedPreferencesHelper;
        mImageFile = imageFile;
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void editItem(@Nullable String itemId) {
        if (itemId == null) {
            createNewItem();
        } else {
            loadExistingItem(itemId);
        }
    }

    @Override
    public void saveItem(@NonNull Item item) {
        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
    }

    @Override
    public void doneEditing() {
        mView.showItemsUi();
    }

    @Override
    public void takePicture(Context context) {
        createFile(context);
        mView.openCamera(mImageFile.getUri());
    }

    private void createFile(Context context) {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        mImageFile.create(context, imageFileName, ".jpg");
    }

    @Override
    public void imageAvailable() {
        if (mImageFile.exists()) {
            mView.showImage(mImageFile.getPath());
        } else {
            imageCaptureFailed();
        }
    }

    @Override
    public void selectImage(Context context) {
        mView.openImagePicker();
    }

    @Override
    public void imageSelected(Uri uri) {
        mView.showImage(uri.toString());
    }

    @Override
    public void imageCaptureFailed() {
        mView.showImageError();
        mImageFile.delete();
    }

    @Override
    public void itemChanged() {
        mView.validateItem();
    }

    @Override
    public void deleteImage() {
        mView.removeImage();
    }

    @Override
    public void clearEditItemCache(@NonNull String itemId) {
        mRepository.refreshItem(itemId);
    }

    @Override
    public void deleteItem(@NonNull Item item) {
        mRepository.deleteItem(mSharedPreferencesHelper.getUserId(), item);
        mView.passDeletedItemToItemsUi();
        mView.showItemsUi();
    }

    private void createNewItem() {
        EspressoIdlingResource.increment();
        mRepository.getNewItemId(mSharedPreferencesHelper.getUserId(), new DataSource.GetNewItemIdCallback() {
            @Override
            public void onNewItemIdLoaded(@Nullable String newItemId) {
                EspressoIdlingResource.decrement();
                if (newItemId != null) {
                    mView.setNewItemId(newItemId);
                    mView.setDefaultDates();
                }
            }
        });
    }

    private void loadExistingItem(String itemId) {
        itemLoaded = false;
        EspressoIdlingResource.increment();
        mRepository.getItem(itemId, EDIT_ITEM_CALLER, new DataSource.GetItemCallback() {
            @Override
            public void onItemLoaded(@Nullable Item item) {
                if (!itemLoaded) {
                    itemLoaded = true;
                    EspressoIdlingResource.decrement();
                }
                mView.showExistingItem(item);
            }
        });
    }
}
