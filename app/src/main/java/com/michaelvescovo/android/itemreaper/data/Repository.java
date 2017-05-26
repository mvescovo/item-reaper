package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

public class Repository implements DataSource {

    private final DataSource mRemoteDataSource;
    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    List<Item> mCachedItems;
    private String mCurrentSort;

    @Inject
    Repository(DataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public void getItems(@NonNull String userId, @NonNull String sortBy, @NonNull String caller,
                         @NonNull final GetItemsCallback callback) {
        if (mCachedItems == null || (mCurrentSort == null || !mCurrentSort.equals(sortBy))) {
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
        mRemoteDataSource.getItem(itemId, userId, caller, new GetItemCallback() {
            @Override
            public void onItemLoaded(@Nullable Item item) {
                callback.onItemLoaded(item);
            }
        });
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
