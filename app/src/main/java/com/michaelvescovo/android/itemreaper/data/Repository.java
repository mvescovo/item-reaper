package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

public class Repository implements DataSource {

    private final DataSource mRemoteDataSource;
    List<Item> mCachedItems;
    private String mCurrentSort;
    List<String> mItemCallers;

    @Inject
    Repository(DataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
        mCachedItems = new ArrayList<>();
        mItemCallers = new ArrayList<>();
    }

    @Override
    public void getItems(@NonNull String userId, @NonNull String sortBy, @NonNull String caller,
                         @NonNull final GetItemsCallback callback) {
        if (mCurrentSort == null || !mCurrentSort.equals(sortBy)) {
            mCurrentSort = sortBy;
            mRemoteDataSource.getItems(userId, sortBy, caller, new GetItemsCallback() {
                @Override
                public void onItemsLoaded(@Nullable List<Item> items) {
                    if (items != null) {
                        mCachedItems = ImmutableList.copyOf(items);
                    } else {
                        mCachedItems = new ArrayList<>();
                    }
                    callback.onItemsLoaded(mCachedItems);
                }
            });
        } else {
            callback.onItemsLoaded(mCachedItems);
        }
    }

    @Override
    public void getItem(@NonNull final String itemId, @NonNull String userId,
                        @NonNull String caller, @NonNull final GetItemCallback callback) {
        // Use cache if possible
        for (Item item : mCachedItems) {
            if (item.getId().equals(itemId)) {
                callback.onItemLoaded(item);
                break;
            }
        }

        // Need to make sure a callback is established with the remote data source, even if the
        // item was cached. Item may have been cached from a call to getItems.
        if (!mItemCallers.contains(caller)) {
            mRemoteDataSource.getItem(itemId, userId, caller, new GetItemCallback() {
                @Override
                public void onItemLoaded(@Nullable Item item) {
                    if (!mCachedItems.contains(item)) {
                        mCachedItems.add(item);
                        callback.onItemLoaded(item);
                    } else {
                        int index = mCachedItems.indexOf(item);
                        // If the item has changed since it was cached, replace the cached version.
                        if (!mCachedItems.get(index).equalsAllFields(item)) {
                            mCachedItems.set(index, item);
                            callback.onItemLoaded(item);
                        }
                    }
                }
            });
            mItemCallers.add(caller);
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
    public void saveItem(@NonNull String userId, @NonNull Item item) {
        mRemoteDataSource.saveItem(userId, item);
    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull Item item) {
        mRemoteDataSource.deleteItem(userId, item);
    }

    @Override
    public void deleteAllItems(@NonNull String userId) {
        mRemoteDataSource.deleteAllItems(userId);
    }
}
