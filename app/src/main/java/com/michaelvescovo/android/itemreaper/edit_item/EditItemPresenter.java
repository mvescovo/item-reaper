package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseUser mFirebaseUser;
    private ImageFile mImageFile;
    private boolean mItemLoaded;

    @Inject
    EditItemPresenter(@NonNull EditItemContract.View view, @NonNull Repository repository,
                      @NonNull ImageFile imageFile) {
        mView = view;
        mRepository = repository;
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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

    private void createNewItem() {
        EspressoIdlingResource.increment();
        mRepository.getNewItemId(mFirebaseUser.getUid(), new DataSource.GetNewItemIdCallback() {
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
        mItemLoaded = false;
        EspressoIdlingResource.increment();
        mRepository.getItem(itemId, mFirebaseUser.getUid(), EDIT_ITEM_CALLER,
                new DataSource.GetItemCallback() {
                    @Override
                    public void onItemLoaded(@Nullable Item item) {
                        if (!mItemLoaded) {
                            mItemLoaded = true;
                            if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                EspressoIdlingResource.decrement();
                            }
                        }
                        if (item != null) {
                            mView.showExistingItem(item);
                        } else {
                            mView.passDeletedItemToItemsUi();
                            mView.showItemsUi();
                        }
                    }
                });
    }

    @Override
    public void saveItem(@NonNull Item item) {
        mRepository.saveItem(mFirebaseUser.getUid(), item);
    }

    @Override
    public void deleteItem(@NonNull Item item) {
        mRepository.deleteItem(mFirebaseUser.getUid(), item);
        if (!mItemLoaded) {
            mView.passDeletedItemToItemsUi();
            mView.showItemsUi();
        }
    }

    @Override
    public void itemChanged() {
        mView.validateItem();
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
    public void selectImage() {
        mView.openImagePicker();
    }

    @Override
    public void imageAvailable(@NonNull ImageFile imageFile) {
        mView.setProgressBar(true);
        mView.setInteractionEnabled(false);
        mView.removeImage();
        if (imageFile.exists()) {
            mView.compressImage(imageFile.getPath());

        } else {
            mView.setProgressBar(false);
            mView.setInteractionEnabled(true);
            mView.showImageError();
        }
    }

    @Override
    public void imageSelected(Context context, Uri uri) {
        mView.setProgressBar(true);
        mView.setInteractionEnabled(false);
        mView.removeImage();
        createFile(context);
        copyUriImageToFile(context, uri);
        mView.compressImage(mImageFile.getPath());
    }

    @Override
    public void imageCompressed(@NonNull String imageUrl) {
        mView.setProgressBar(false);
        mView.setInteractionEnabled(true);
        mView.showImage(imageUrl);
    }

    @Override
    public void imageCaptureFailed(@NonNull Context context, @Nullable ImageFile imageFile) {
        mView.showImageError();
        if (imageFile != null && imageFile.exists()) {
            imageFile.delete(context);
        }
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
    public void deleteImage() {
        mView.removeImage();
    }
}
