package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Context;
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

/**
 * @author Michael Vescovo
 */

class EditItemPresenter implements EditItemContract.Presenter {

    private EditItemContract.View mView;
    private Repository mRepository;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private ImageFile mImageFile;

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
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        mImageFile.create(context, imageFileName, ".jpg");
        mView.openCamera(mImageFile.getUri());
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
    public void imageCaptureFailed() {
        mView.showImageError();
        mImageFile.delete();
    }

    private void createNewItem() {
        EspressoIdlingResource.increment();
        mRepository.getNewItemId(mSharedPreferencesHelper.getUserId(), new DataSource.GetNewItemIdCallback() {
            @Override
            public void onNewItemIdLoaded(@Nullable String newItemId) {
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                if (newItemId != null) {
                    Item newItem = new Item(newItemId);
                    mRepository.saveItem(mSharedPreferencesHelper.getUserId(), newItem);
                }
            }
        });
    }

    private void loadExistingItem(String itemId) {
        // When running tests the activity pauses and resumes. Make sure it only increments once.
        if (EspressoIdlingResource.getIdlingResource().isIdleNow()) {
            EspressoIdlingResource.increment();
        }
        mRepository.getItem(itemId, new DataSource.GetItemCallback() {
            @Override
            public void onItemLoaded(@Nullable Item item) {
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                mView.showExistingItem(item);
            }
        });
    }
}
