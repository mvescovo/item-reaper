package com.michaelvescovo.android.itemreaper.util;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import javax.inject.Inject;

import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.USER_ID;


/**
 * @author Michael Vescovo
 */

public class ImageUploadService extends IntentService {

    public static final String ACTION_UPLOAD_IMAGE = "com.michaelvescovo.android.itemreaper.util.action.UPLOAD_IMAGE";
    public static final String ACTION_REMOVE_IMAGE = "com.michaelvescovo.android.itemreaper.util.action.REMOVE_IMAGE";
    public static final String EXTRA_ITEM = "com.michaelvescovo.android.itemreaper.util.extra.ITEM";
    public static final int ONGOING_NOTIFICATION_ID = 1;

    @Inject
    public Repository mRepository;
    @Inject
    public SharedPreferencesHelper mSharedPreferencesHelper;
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

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_grim_reaper)
                        .setContentTitle(getText(R.string.title_uploading_or_removing_image));
        startForeground(ONGOING_NOTIFICATION_ID, mBuilder.build());
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
            deleteItemFile(item);
            item.setImageUrl("file:///android_asset/black-t-shirt.jpg");
            mRepository.saveItem(USER_ID, item);
            mSharedPreferencesHelper.removeImageUploading(item.getId());
        }
    }

    private void removeImage(Item item) {
        if (item.getImageUrl() != null) {
            deleteItemFile(item);
            item.setImageUrl(null);
            mRepository.saveItem(USER_ID, item);
        }
    }

    private void deleteItemFile(Item item) {
        if (item.getImageUrl() != null
                && !item.getImageUrl().equals("file:///android_asset/black-t-shirt.jpg")) {
            String filename = item.getImageUrl().substring(item.getImageUrl()
                    .lastIndexOf("/") + 1);
            getApplicationContext().deleteFile(filename);
        }
    }
}
