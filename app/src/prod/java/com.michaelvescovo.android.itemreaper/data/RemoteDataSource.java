package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;

/**
 * @author Michael Vescovo
 */

class RemoteDataSource implements DataSource {

    public final static String ITEMS_CALLER = "items";
    public final static String EDIT_ITEM_CALLER = "edit_item";
    public final static String ITEM_DETAILS_CALLER = "item_details";

    @Override
    public void getItemIds(@NonNull String userId, @NonNull GetItemIdsCallback callback) {

    }

    @Override
    public void refreshItemIds() {

    }

    @Override
    public void getItem(@NonNull String itemId, @NonNull String caller,
                        @NonNull GetItemCallback callback) {

    }

    @Override
    public void getNewItemId(@NonNull String userId, @NonNull GetNewItemIdCallback callback) {

    }

    @Override
    public void refreshItems() {

    }

    @Override
    public void refreshItem(@NonNull String itemId) {

    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {

    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull Item item) {

    }

    @Override
    public void deleteAllItems(@NonNull String userId) {
        refreshItemIds();
        refreshItems();
    }
}
