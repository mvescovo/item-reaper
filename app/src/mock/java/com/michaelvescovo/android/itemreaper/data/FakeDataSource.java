package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;

/**
 * @author Michael Vescovo
 */

class FakeDataSource implements DataSource {

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
