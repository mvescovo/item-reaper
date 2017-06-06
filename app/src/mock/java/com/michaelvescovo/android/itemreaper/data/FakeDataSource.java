package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Michael Vescovo
 */

public class FakeDataSource implements DataSource {

    public final static String USER_ID = "testUID";
    private final static List<Item> ITEMS = Lists.newArrayList();

    /*
    * Test item 1.
    * - Has all fields filled in (each must be unique to prevent ambiguous matcher exceptions).
    * */
    public final static Item ITEM_1 = new Item("1",
            1453554000000L, // purchase: 24/01/2016
            2000,
            0,
            1485176400000L, // expiry: 24/01/2017
            "Clothing", "Casual", "T-shirt",
            "Short sleeve", "V-neck", "Plain", "Black", "Dark", "Some secondary colour",
            "Some Size", "Some Brand", "Some Shop", "Standard plain T-shirt", "Some note",
            "file:///android_asset/black-t-shirt.jpg", false);

    /*
    * Test item 2.
    * - Has some fields filled in but not all.
    * */
    public final static Item ITEM_2 = new Item("2",
            -1, // purchase: unknown
            3000,
            -1,
            1514206800000L, // expiry: 26/12/2017
            "Bathroom", null,
            "Towel", null, null, null, "White", null, null, null, null, null, null, null, null,
            false);

    /*
    * Test item 3.
    * - For screenshots.
    * */
    public final static Item ITEM_3 = new Item("3",
            -1, // purchase: unknown
            149900,
            -1,
            1524146400000L, // expiry: 20/04/2018
            "Technology", null,
            "Laptop", null, null, null, "Silver", null, null, null, null, null, null, null, null,
            false);

    /*
    * Test item 4.
    * - For screenshots.
    * */
    public final static Item ITEM_4 = new Item("4",
            -1, // purchase: unknown
            13500,
            -1,
            1527775200000L, // expiry: 01/06/2018
            "Cookware", null,
            "Fry pan", null, null, null, "Black", null, null, null, null, null, null, null, null,
            false);

    /*
    * Test item 5.
    * - For screenshots.
    * */
    public final static Item ITEM_5 = new Item("5",
            -1, // purchase: unknown
            99900,
            -1,
            1560088800000L, // expiry: 10/06/2019
            "Furniture", null,
            "Couch", null, null, null, "Cream", null, null, null, null, null, null, null, null,
            false);
    /*
    * Test item 6.
    * - For screenshots.
    * */
    public final static Item ITEM_6 = new Item("6",
            -1, // purchase: unknown
            2000,
            -1,
            1577278800000L, // expiry: 26/12/2019
            "Bedding", null,
            "Pillow", null, null, null, "White", null, null, null, null, null, null, null, null,
            false);

    private Map<String, Map<String, ItemChangedListener>> mItemCallbacks = Maps.newHashMap();
    private Map<String, ItemsChangedListener> mItemsCallbacks = Maps.newHashMap();

    FakeDataSource() {
        ITEMS.add(ITEM_1);
        ITEMS.add(ITEM_2);
        ITEMS.add(ITEM_3);
        ITEMS.add(ITEM_4);
        ITEMS.add(ITEM_5);
        ITEMS.add(ITEM_6);
    }

    @Override
    public void getItems(@NonNull String userId, @NonNull String sortBy, @NonNull String caller,
                         @NonNull final GetItemsCallback callback) {
        if (mItemsCallbacks.get(caller) == null) {
            mItemsCallbacks.put(caller, new ItemsChangedListener() {
                @Override
                public void itemsChanged(List<Item> items) {
                    callback.onItemsLoaded(ITEMS);
                }
            });
        }
        mItemsCallbacks.get(caller).itemsChanged(ITEMS);
    }

    @Override
    public void getItem(@NonNull final String itemId, @NonNull String userId,
                        @NonNull String caller, @NonNull final GetItemCallback callback) {
        if (mItemCallbacks.get(itemId) == null) {
            @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
            Map<String, ItemChangedListener> changedListeners = new HashMap<>();
            mItemCallbacks.put(itemId, changedListeners);
        }
        ItemChangedListener itemChangedListener = createItemChangedListener(callback);
        mItemCallbacks.get(itemId).put(caller, itemChangedListener);
        mItemCallbacks.get(itemId).get(caller).itemChanged(ITEMS.get(ITEMS.indexOf(new Item(itemId))));
    }

    private ItemChangedListener createItemChangedListener(final GetItemCallback callback) {
        return new ItemChangedListener() {
            @Override
            public void itemChanged(Item item) {
                callback.onItemLoaded(item);
            }
        };
    }

    @Override
    public void getNewItemId(@NonNull String userId, @NonNull GetNewItemIdCallback callback) {
        String uniqueID = UUID.randomUUID().toString();
        callback.onNewItemIdLoaded(uniqueID);
    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {
        if (ITEMS.contains(item)) {
            ITEMS.remove(item);
        }
        ITEMS.add(item);
        if (mItemCallbacks.get(item.getId()) != null) {
            for (ItemChangedListener listener : mItemCallbacks.get(item.getId()).values()) {
                listener.itemChanged(ITEMS.get(ITEMS.indexOf(item)));
            }
        }
        if (mItemsCallbacks.size() > 0) {
            for (ItemsChangedListener itemsChangedListener : mItemsCallbacks.values()) {
                itemsChangedListener.itemsChanged(ITEMS);
            }
        }
    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull Item item) {
        ITEMS.remove(item);
        if (mItemCallbacks.get(item.getId()) != null) {
            for (ItemChangedListener listener : mItemCallbacks.get(item.getId()).values()) {
                listener.itemChanged(null);
            }
            mItemCallbacks.remove(item.getId());
        }
        if (mItemsCallbacks.size() > 0) {
            for (ItemsChangedListener itemsChangedListener : mItemsCallbacks.values()) {
                itemsChangedListener.itemsChanged(ITEMS);
            }
        }
    }

    @Override
    public void deleteAllItems(@NonNull String userId) {
        ITEMS.clear();
        if (mItemsCallbacks.size() > 0) {
            for (ItemsChangedListener itemsChangedListener : mItemsCallbacks.values()) {
                itemsChangedListener.itemsChanged(ITEMS);
            }
            mItemsCallbacks.clear();
        }
    }

    private interface ItemChangedListener {

        void itemChanged(Item item);
    }

    private interface ItemsChangedListener {

        void itemsChanged(List<Item> items);
    }
}
