package com.michaelvescovo.android.itemreaper.util;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;


/**
 * @author Michael Vescovo
 */

public class ImageUploadService extends IntentService {

    public static final String ACTION_UPLOAD_IMAGE = "com.michaelvescovo.android.itemreaper.util.action.UPLOAD_IMAGE";
    public static final String EXTRA_ITEM = "com.michaelvescovo.android.itemreaper.util.extra.ITEM";
    private static final int COMPRESSION_AMOUNT = 50;
    private static final int SCALE_WIDTH = 1920;
    private static final int SCALE_HEIGHT = 1080;

    @Inject
    public Repository mRepository;
    @Inject
    public SharedPreferencesHelper mSharedPreferencesHelper;
    private FirebaseStorage mFirebaseStorage;
    private UploadTask mUploadTask;
    private Item mItem;

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
                mItem = (Item) intent.getSerializableExtra(EXTRA_ITEM);
                compressImage(mItem.getImageUrl());
            }
        }
    }

    private void compressImage(final String imageUrl) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageUrl, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / SCALE_WIDTH, photoH / SCALE_HEIGHT);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(imageUrl, bmOptions);
        // Compress into WEBP format
        try {
            File file = new File(imageUrl);
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.WEBP, COMPRESSION_AMOUNT, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        uploadImage(imageUrl);
    }

    private void uploadImage(final String imageUrl) {
        Uri uri = Uri.fromFile(new File(imageUrl));
        StorageReference photoRef = mFirebaseStorage.getReference("item_photos")
                .child(uri.getLastPathSegment());
        mUploadTask = photoRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        mUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl != null) {
                    deleteFile(getApplicationContext(), imageUrl);
                    mItem.setImageUrl(downloadUrl.toString());
                    mRepository.saveItem(mSharedPreferencesHelper.getUserId(), mItem);
                }
            }
        });
    }

    private void deleteFile(Context context, String imageUrl) {
        if (imageUrl != null) {
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            if (filename.startsWith("IMAGE_")) {
                context.deleteFile(filename);
            }
        }
    }
}
