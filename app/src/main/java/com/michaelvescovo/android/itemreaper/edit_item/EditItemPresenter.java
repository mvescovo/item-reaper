package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;
import com.michaelvescovo.android.itemreaper.util.ImageFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

class EditItemPresenter implements EditItemContract.Presenter {

    private final static String EDIT_ITEM_CALLER = "edit_item";
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
    public void takePicture(Context context, String imageUrl) {
        deleteFile(context, imageUrl);
        createFile(context);
        mView.openCamera(mImageFile);
    }

    private void createFile(Context context) {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        mImageFile.create(context, imageFileName, ".jpg");
    }

    @Override
    public void imageAvailable(ImageFile imageFile) {
        mImageFile = imageFile;
        if (mImageFile.exists()) {
            compressImage();
            mView.showImage(mImageFile.getPath());
        } else {
            imageCaptureFailed();
        }
    }

    private void compressImage() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(mImageFile.getPath());
                    File file = new File(mImageFile.getPath());
                    OutputStream outputStream;
                    outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.WEBP, 50, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
    public void deleteImage(Context context, String imageUrl) {
        deleteFile(context, imageUrl);
        mView.removeImage();
    }

    private void deleteFile(Context context, String imageUrl) {
        if (imageUrl != null) {
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            if (filename.startsWith("JPEG_")) {
                context.deleteFile(filename);
            }
        }
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
