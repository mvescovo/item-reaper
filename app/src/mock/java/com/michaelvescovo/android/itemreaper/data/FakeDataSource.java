package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Michael Vescovo
 */

@VisibleForTesting
@SuppressWarnings("WeakerAccess")
public class FakeDataSource implements DataSource {

    public final static String USER_ID = "testUser";
    public final static List<String> ITEM_IDS = Lists.newArrayList();
    public final static Map<String, Item> ITEMS = Maps.newHashMap();
    public final static String ITEM_ID_1 = "1";
    public final static Item ITEM_1 = new Item("-1", -1, 20, -1, 1, "Clothing", null,
            "T-shirt", null, null, null, "Black", null, null, null, null, null, null, null,
            "https://image.freepik.com/free-vector/black-vector-t-shirt_6646.jpg", false);
    public final static String ITEM_ID_2 = "2";
    public final static Item ITEM_2 = new Item("2", -1, 30, -1, 2, "Bathroom", null,
            "Towel", null, null, null, "White", null, null, null, null, null, null, null, null,
            false);

    FakeDataSource() {
        ITEM_IDS.add(ITEM_ID_1);
        ITEM_IDS.add(ITEM_ID_2);
        ITEMS.put(ITEM_ID_1, ITEM_1);
        ITEMS.put(ITEM_ID_2, ITEM_2);
    }

    @Override
    public void getItemIds(@NonNull String userId, @NonNull GetItemIdsCallback callback) {
        if (ITEM_IDS != null) {
            callback.onItemIdsLoaded(ITEM_IDS);
        }
    }

    @Override
    public void stopGetItemIds() {
        // Nothing to do here since this data source doesn't have listeners.
    }

    @Override
    public void refreshItemIds() {
        ITEM_IDS.clear();
    }

    @Override
    public void getItem(@NonNull String itemId, @NonNull GetItemCallback callback) {
        if (ITEMS.containsKey(itemId)) {
            callback.onItemLoaded(ITEMS.get(itemId));
        }
    }

    @Override
    public void getNewItemId(@NonNull String userId, @NonNull GetNewItemIdCallback callback) {
        String uniqueID = UUID.randomUUID().toString();
        callback.onNewItemIdLoaded(uniqueID);
    }

    @Override
    public void stopGetItem() {
        // Nothing to do here since this data source doesn't have listeners.
    }

    @Override
    public void refreshItems() {
        ITEMS.clear();
    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {
        ITEM_IDS.add(item.getId());
        ITEMS.put(item.getId(), item);
    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull String itemId) {
        ITEMS.remove(userId);
    }

    @Override
    public void deleteAllItems(@NonNull String userId) {
        refreshItemIds();
        refreshItems();
    }
}
