package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

public class Repository implements DataSource {

    private final DataSource mRemoteDataSource;
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
    public void getItemIds(@NonNull String userId, @NonNull final GetItemIdsCallback callback) {
        if (mCachedItemIds != null) {
            callback.onItemIdsLoaded(mCachedItemIds);
        } else {
            mRemoteDataSource.getItemIds(userId, new GetItemIdsCallback() {
                @Override
                public void onItemIdsLoaded(@Nullable List<String> itemIds) {
                    if (itemIds != null) {
                        mCachedItemIds = ImmutableList.copyOf(itemIds);
                        callback.onItemIdsLoaded(mCachedItemIds);
                    }
                }
            });
        }
    }

    @Override
    public void stopGetItemIds() {
        mRemoteDataSource.stopGetItemIds();
    }

    @Override
    public void refreshItemIds() {
        if (mCachedItemIds != null) {
            mCachedItemIds.clear();
        }
    }

    @Override
    public void getItem(@NonNull final String itemId, @NonNull final GetItemCallback callback) {
        if (mCachedItems.containsKey(itemId)) {
            callback.onItemLoaded(mCachedItems.get(itemId));
        } else {
            mRemoteDataSource.getItem(itemId, new GetItemCallback() {
                @Override
                public void onItemLoaded(@Nullable Item item) {
                    if (item != null) {
                        mCachedItems.put(itemId, item);
                        callback.onItemLoaded(mCachedItems.get(itemId));
                    }
                }
            });
        }
    }

    @Override
    public void stopGetItem() {
        mRemoteDataSource.stopGetItem();
    }

    @Override
    public void refreshItems() {
        mCachedItems.clear();
    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {
        mRemoteDataSource.saveItem(userId, item);
    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull String itemId) {
        mRemoteDataSource.deleteItem(userId, itemId);
    }

    @Override
    public void deleteAllItems(@NonNull String userId) {
        refreshItemIds();
        refreshItems();
        mRemoteDataSource.deleteAllItems(userId);
    }
}
