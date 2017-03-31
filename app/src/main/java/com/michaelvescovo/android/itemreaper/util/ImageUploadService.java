package com.michaelvescovo.android.itemreaper.util;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import java.io.File;

import javax.inject.Inject;


/**
 * @author Michael Vescovo
 */

public class ImageUploadService extends IntentService {

    public static final String ACTION_UPLOAD_IMAGE = "com.michaelvescovo.android.itemreaper.util.action.UPLOAD_IMAGE";
    public static final String ACTION_REMOVE_IMAGE = "com.michaelvescovo.android.itemreaper.util.action.REMOVE_IMAGE";
    public static final String EXTRA_ITEM = "com.michaelvescovo.android.itemreaper.util.extra.ITEM";

    @Inject
    public Repository mRepository;
    @Inject
    public SharedPreferencesHelper mSharedPreferencesHelper;
    private FirebaseStorage mFirebaseStorage;

    public ImageUploadService() {
        super("ImageUploadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerImageUploadComponent.builder()
                .applicationComponent(((ItemReaperApplication) getApplication())
                        .getApplicationComponent())
                .repositoryComponent(((ItemReaperApplication) getApplication())
                        .getRepositoryComponent())
                .build()
                .inject(this);
        mFirebaseStorage = FirebaseStorage.getInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_IMAGE.equals(action)) {
                Item item = (Item) intent.getSerializableExtra(EXTRA_ITEM);
                boolean uploading = mSharedPreferencesHelper.imageUploading(item.getId());
                if (!uploading) {
                    mSharedPreferencesHelper.setImageUploading(item.getId());
                    uploadImage(item);
                }
            } else if (ACTION_REMOVE_IMAGE.equals(action)) {
                Item item = (Item) intent.getSerializableExtra(EXTRA_ITEM);
                removeImage(item);
            }
        }
    }

    private void uploadImage(final Item item) {
        if (item.getImageUrl() != null) {
            Uri uri = Uri.fromFile(new File(item.getImageUrl()));
            StorageReference photoRef = mFirebaseStorage.getReference("users")
                    .child(mSharedPreferencesHelper.getUserId())
                    .child("itemPhotos")
                    .child(uri.getLastPathSegment());
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/webp")
                    .build();
            UploadTask uploadTask = photoRef.putFile(uri, metadata);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    if (downloadUrl != null) {
                        deleteItemFile(item);
                        item.setImageUrl(downloadUrl.toString());
                        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
                        mSharedPreferencesHelper.removeImageUploading(item.getId());
                    }
                }
            });
        }
    }

    private void removeImage(Item item) {
        if (item.getImageUrl() != null) {
            if (item.getImageUrl().startsWith("https://firebasestorage")) {
                StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(
                        item.getImageUrl());
                photoRef.delete();
                item.setImageUrl(null);
                mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
            } else {
                deleteItemFile(item);
                item.setImageUrl(null);
                mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
            }
        }
    }

    private void deleteItemFile(Item item) {
        if (item.getImageUrl() != null) {
            String filename = item.getImageUrl().substring(item.getImageUrl()
                    .lastIndexOf("/") + 1);
            getApplicationContext().deleteFile(filename);
        }
    }
}
