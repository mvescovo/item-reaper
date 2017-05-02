package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import static com.michaelvescovo.android.itemreaper.util.Constants.ITEM_ADDED;
import static com.michaelvescovo.android.itemreaper.util.Constants.ITEM_CHANGED;
import static com.michaelvescovo.android.itemreaper.util.Constants.ITEM_MOVED;
import static com.michaelvescovo.android.itemreaper.util.Constants.ITEM_REMOVED;
import static com.michaelvescovo.android.itemreaper.util.Constants.SORT_BY_EXPIRY_STRING;

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
    public void getItemsList(@NonNull String userId, @NonNull String sortBy,
                             @NonNull final GetItemsListCallback callback) {
        if (mCachedItems == null) {
            mRemoteDataSource.getItemsList(userId, sortBy, new GetItemsListCallback() {
                @Override
                public void onItemsLoaded(@Nullable List<Item> items) {
                    callback.onItemsLoaded(items);
                }
            });
        } else {
            callback.onItemsLoaded(mCachedItems);
        }
    }

    @Override
    public void checkItemsExist(@NonNull String userId, @NonNull String caller, @NonNull String sortBy, @NonNull final CheckItemsExistCallback callback) {
        mRemoteDataSource.checkItemsExist(userId, caller, sortBy, new CheckItemsExistCallback() {
            @Override
            public void onCheckedItemsExist(boolean itemsExist) {
                callback.onCheckedItemsExist(itemsExist);
            }
        });
    }

    @Override
    public void getItems(@NonNull String userId, @NonNull String caller, @NonNull String sortBy,
                         @NonNull final GetItemsCallback callback) {
        if (mCachedItems == null || (mCurrentSort == null || !mCurrentSort.equals(sortBy))) {
            mCurrentSort = sortBy;
            mCachedItems = new ArrayList<>();
            mRemoteDataSource.getItems(userId, caller, sortBy, new GetItemsCallback() {
                @Override
                public void onItemLoaded(@Nullable Item item, @NonNull String action) {
                    int itemIndex;
                    switch (action) {
                        case ITEM_ADDED:
                            mCachedItems.add(item);
                            // Annoying but need to sort each time. Maybe childEventListener is not
                            // such a good idea after all.
                            if (mCurrentSort.equals(SORT_BY_EXPIRY_STRING)) {
                                sortItemsByExpiry();
                            } else {
                                sortItemsByPurchaseDate();
                            }
                            break;
                        case ITEM_CHANGED:
                            itemIndex = mCachedItems.indexOf(item);
                            if (itemIndex != -1) {
                                mCachedItems.set(itemIndex, item);
                            }
                            break;
                        case ITEM_REMOVED:
                            itemIndex = mCachedItems.indexOf(item);
                            if (itemIndex != -1) {
                                mCachedItems.remove(item);
                            }
                            break;
                        case ITEM_MOVED:
                            if (mCurrentSort.equals(SORT_BY_EXPIRY_STRING)) {
                                sortItemsByExpiry();
                            } else {
                                sortItemsByPurchaseDate();
                            }
                            break;
                    }
                    callback.onItemLoaded(item, action);
                }
            });
        } else {
            for (Item item : mCachedItems) {
                callback.onItemLoaded(item, ITEM_ADDED);
            }
        }
    }

    private void sortItemsByExpiry() {
        // Sort ascending (earlier dates first)
        Collections.sort(mCachedItems, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item1.compareTo(item2);
            }
        });
    }

    private void sortItemsByPurchaseDate() {
        // Sort ascending (earlier dates first)
        Collections.sort(mCachedItems, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return compareByPurchaseDate(item1, item2);
            }
        });
    }

    private int compareByPurchaseDate(Item item1, Item item2) {
        if (item1.getPurchaseDate() > item2.getPurchaseDate()) {
            return 1;
        } else if (item1.getPurchaseDate() == item2.getPurchaseDate()) {
            return 0;
        } else {
            return -1;
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
