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
            callback.onItemIdsLoaded(mCachedItemIds, false);
        } else {
            mRemoteDataSource.getItemIds(userId, new GetItemIdsCallback() {
                @Override
                public void onItemIdsLoaded(@Nullable List<String> itemIds, boolean itemRemoved) {
                    if (itemIds != null) {
                        if (mCachedItemIds != null && (mCachedItemIds.size() > itemIds.size())) {
                            itemRemoved = true;
                        }
                        mCachedItemIds = ImmutableList.copyOf(itemIds);
                        callback.onItemIdsLoaded(mCachedItemIds, itemRemoved);
                    } else {
                        callback.onItemIdsLoaded(null, false);
                    }
                }
            });
        }
    }

    @Override
    public void refreshItemIds() {
        if (mCachedItemIds != null) {
            mCachedItemIds =  null;
        }
    }

    @Override
    public void getItem(@NonNull final String itemId, @NonNull String userId,
                        @NonNull String caller, @NonNull final GetItemCallback callback) {
        if (mCachedItems.containsKey(itemId)) {
            callback.onItemLoaded(mCachedItems.get(itemId));
        } else {
            mRemoteDataSource.getItem(itemId, userId, caller, new GetItemCallback() {
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
    public void getNewItemId(@NonNull String userId, @NonNull final GetNewItemIdCallback callback) {
        mRemoteDataSource.getNewItemId(userId, new GetNewItemIdCallback() {
            @Override
            public void onNewItemIdLoaded(@Nullable String newItemId) {
                if (newItemId != null) {
                    callback.onNewItemIdLoaded(newItemId);
                }
            }
        });
    }

    @Override
    public void refreshItems() {
        mCachedItems.clear();
    }

    @Override
    public void refreshItem(@NonNull String itemId) {
        if (mCachedItems.containsKey(itemId)) {
            mCachedItems.remove(itemId);
        }
    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {
        mRemoteDataSource.saveItem(userId, item);
    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull Item item) {
        mRemoteDataSource.deleteItem(userId, item);
    }

    @Override
    public void deleteAllItems(@NonNull String userId) {
        refreshItemIds();
        refreshItems();
        mRemoteDataSource.deleteAllItems(userId);
    }
}
