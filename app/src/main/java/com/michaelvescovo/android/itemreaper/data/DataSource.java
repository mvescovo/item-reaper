package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * @author Michael Vescovo
 */

public interface DataSource {

    interface GetItemIdsCallback {
        void onItemIdsLoaded(@Nullable List<String> itemIds);
    }

    interface GetItemCallback {
        void onItemLoaded(@Nullable Item item);
    }

    interface GetNewItemIdCallback {
        void onNewItemIdLoaded(@Nullable String newItemId);
    }

    void getItemIds(@NonNull String userId, @NonNull GetItemIdsCallback callback);
    void stopGetItemIds();
    void refreshItemIds();
    void getItem(@NonNull String itemId, @NonNull String caller, @NonNull GetItemCallback callback);
    void getNewItemId(@NonNull String userId, @NonNull GetNewItemIdCallback callback);
    void stopGetItem(@NonNull String caller);
    void refreshItems();
    void refreshItem(@NonNull String itemId);
    void saveItem(@NonNull String userId, @NonNull Item item);
    void deleteItem(@NonNull String userId, @NonNull String itemId);
    void deleteAllItems(@NonNull String userId);
}
