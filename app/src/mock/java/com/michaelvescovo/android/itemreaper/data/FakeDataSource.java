package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author Michael Vescovo
 */

class FakeDataSource implements DataSource {

    private final static String USER_ID = "testUser";
    private final static List<String> ITEM_IDS = Lists.newArrayList();
    private final static Map<String, Item> ITEMS = Maps.newHashMap();
    private final static String ITEM_ID_1 = "1";
    private final static Item ITEM_1 = new Item("1", "1/1/1", "Clothing", "T-shirt");
    private final static String ITEM_ID_2 = "2";
    private final static Item ITEM_2 = new Item("2", "2/2/2", "Bathroom", "Towel");

    FakeDataSource() {
        ITEM_IDS.add(ITEM_ID_1);
        ITEMS.put(ITEM_ID_1, ITEM_1);
        ITEM_IDS.add(ITEM_ID_2);
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
    public void stopGetItem() {
        // Nothing to do here since this data source doesn't have listeners.
    }

    @Override
    public void refreshItems() {
        ITEMS.clear();
    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {
        ITEMS.put(userId, item);
    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull String itemId) {
        ITEMS.remove(userId);
    }
}
