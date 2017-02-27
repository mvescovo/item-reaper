package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;

/**
 * @author Michael Vescovo
 */

class RemoteDataSource implements DataSource {

    @Override
    public void getItemIds(@NonNull String userId, @NonNull GetItemIdsCallback callback) {

    }

    @Override
    public void stopGetItemIds() {

    }

    @Override
    public void refreshItemIds() {

    }

    @Override
    public void getItem(@NonNull String itemId, @NonNull String caller,
                        @NonNull GetItemCallback callback) {

    }

    @Override
    public void getNewItemId(@NonNull String userId, @NonNull GetNewItemIdCallback callback) {

    }

    @Override
    public void stopGetItem(@NonNull String caller) {

    }

    @Override
    public void refreshItems() {

    }

    @Override
    public void refreshItem(@NonNull String itemId) {

    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {

    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull String itemId) {

    }

    @Override
    public void deleteAllItems(@NonNull String userId) {
        refreshItemIds();
        refreshItems();
    }
}
