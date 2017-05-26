package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * @author Michael Vescovo
 */

public interface DataSource {

    void getItems(@NonNull String userId, @NonNull String sortBy, @NonNull String caller,
                  @NonNull GetItemsCallback callback);

    void getItem(@NonNull String itemId, @NonNull String userId, @NonNull String caller,
                 @NonNull GetItemCallback callback);

    void getNewItemId(@NonNull String userId, @NonNull GetNewItemIdCallback callback);

    void saveItem(@NonNull String userId, @NonNull Item item);

    void deleteItem(@NonNull String userId, @NonNull Item item);

    void deleteAllItems(@NonNull String userId);

    interface GetItemsCallback {
        void onItemsLoaded(@Nullable List<Item> items);
    }

    interface GetItemCallback {
        void onItemLoaded(@Nullable Item item);
    }

    interface GetNewItemIdCallback {
        void onNewItemIdLoaded(@Nullable String newItemId);
    }
}
