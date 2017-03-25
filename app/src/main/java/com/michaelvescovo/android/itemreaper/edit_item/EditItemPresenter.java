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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public void takePicture(Context context) {
        createFile(context);
        mView.openCamera(mImageFile);
    }

    private void createFile(Context context) {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        mImageFile.create(context, imageFileName, ".webp");
    }

    @Override
    public void imageAvailable(@NonNull Context context, @NonNull ImageFile imageFile,
                               @Nullable String imageUrl) {
        deleteImage(context, imageUrl); // delete previous image
        mImageFile = imageFile;
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
    public void imageSelected(Context context, String imageUrl, Uri uri) {
        deleteImage(context, imageUrl); // delete previous image
        createFile(context);
        copyUriImageToFile(context, uri);
        mView.showImage(mImageFile.getPath());
    }

    private void copyUriImageToFile(Context context, Uri uri) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (context.getContentResolver() != null) {
                inputStream = context.getContentResolver().openInputStream(uri);
                outputStream = new FileOutputStream(mImageFile.getPath());
                copyFile(inputStream, outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignore) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
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
        if (imageUrl != null && !imageUrl.startsWith("https://firebasestorage")) {
            deleteFile(context, imageUrl);
        }
        mView.removeImage();
    }

    @Override
    public void deleteFile(Context context, String imageUrl) {
        if (imageUrl != null) {
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            if (filename.startsWith("IMAGE_")) {
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
