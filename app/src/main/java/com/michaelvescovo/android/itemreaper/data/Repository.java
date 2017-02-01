package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

class Repository implements DataSource {

    private DataSource mRemoteDataSource;
    @VisibleForTesting
    List<String> mCachedItemIds;
    @VisibleForTesting
    Map<String, Item> mCachedItems;

    @Inject
    Repository(DataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
        mCachedItems = new HashMap<>();
    }

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
    public void getItem(@NonNull String itemId, @NonNull GetItemCallback callback) {

    }

    @Override
    public void stopGetItem() {

    }

    @Override
    public void refreshItems() {

    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {

    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull String itemId) {

    }
}
