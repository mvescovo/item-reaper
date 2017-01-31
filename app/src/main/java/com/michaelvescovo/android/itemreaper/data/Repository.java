package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

class Repository implements DataSource {

    private DataSource mRemoteDataSource;
    private Map<String, Item> mCachedItems;

    @Inject
    Repository(DataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public void getItemIds(@NonNull String userId, @NonNull GetItemIdsCallback callback) {

    }

    @Override
    public void getItems(@NonNull GetItemsCallback callback) {

    }

    @Override
    public void getItem(@NonNull String itemId, @NonNull GetItemCallback callback) {

    }

    @Override
    public void addItem(@NonNull Item item) {

    }

    @Override
    public void deleteItem(@NonNull String itemId) {

    }
}
