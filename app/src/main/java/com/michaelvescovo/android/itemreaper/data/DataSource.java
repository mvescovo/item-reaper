package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * @author Michael Vescovo
 */

interface DataSource {

    interface GetItemIdsCallback {
        void onItemIdsLoaded(@Nullable Map<String, Boolean> itemIds);
    }

    interface GetItemsCallback {
        void onItemsLoaded(@Nullable Map<String, Item> items);
    }

    interface GetItemCallback {
        void onItemLoaded(@Nullable Item item);
    }

    void getItemIds(@NonNull String userId, @NonNull GetItemIdsCallback callback);
    void getItems(@NonNull GetItemsCallback callback);
    void getItem(@NonNull String itemId, @NonNull GetItemCallback callback);
    void addItem(@NonNull Item item);
    void deleteItem(@NonNull String itemId);
}
