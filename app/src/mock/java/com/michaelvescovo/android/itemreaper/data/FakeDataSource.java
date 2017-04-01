package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
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

    /*
    * Test item 1.
    * - Has all fields filled in (each must be unique to prevent ambiguous matcher exceptions).
    * */
    public final static String ITEM_ID_1 = "1";
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
    public final static String ITEM_ID_2 = "2";
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
    public final static String ITEM_ID_3 = "3";
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
    public final static String ITEM_ID_4 = "4";
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
    public final static String ITEM_ID_5 = "5";

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
    public final static String ITEM_ID_6 = "6";

    public final static Item ITEM_6 = new Item("6",
            -1, // purchase: unknown
            2000,
            -1,
            1577278800000L, // expiry: 26/12/2019
            "Bedding", null,
            "Pillow", null, null, null, "White", null, null, null, null, null, null, null, null,
            false);

    private ItemIdsListener mItemIdsListener;
    private Map<String, Map<String, ItemChangedListener>> mItemCallbacks = Maps.newHashMap();

    FakeDataSource() {
        ITEM_IDS.add(ITEM_ID_1);
        ITEM_IDS.add(ITEM_ID_2);
        ITEM_IDS.add(ITEM_ID_3);
        ITEM_IDS.add(ITEM_ID_4);
        ITEM_IDS.add(ITEM_ID_5);
        ITEM_IDS.add(ITEM_ID_6);
        ITEMS.put(ITEM_ID_1, ITEM_1);
        ITEMS.put(ITEM_ID_2, ITEM_2);
        ITEMS.put(ITEM_ID_3, ITEM_3);
        ITEMS.put(ITEM_ID_4, ITEM_4);
        ITEMS.put(ITEM_ID_5, ITEM_5);
        ITEMS.put(ITEM_ID_6, ITEM_6);
    }

    @Override
    public void getItemIds(@NonNull String userId, @NonNull final GetItemIdsCallback callback) {
        mItemIdsListener = new ItemIdsListener() {
            @Override
            public void itemIdsChanged(List<String> itemIds) {
                if (itemIds != null) {
                    callback.onItemIdsLoaded(itemIds, false);
                }
            }
        };
        mItemIdsListener.itemIdsChanged(ITEM_IDS);
    }

    @Override
    public void refreshItemIds() {
        ITEM_IDS.clear();
    }

    @Override
    public void getItem(@NonNull final String itemId, @NonNull String caller,
                        @NonNull final GetItemCallback callback) {
        if (mItemCallbacks.get(itemId) == null) {
            @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
            Map<String, ItemChangedListener> changedListeners = new HashMap<>();
            mItemCallbacks.put(itemId, changedListeners);
        }
        ItemChangedListener itemChangedListener = createItemChangedListener(callback);
        mItemCallbacks.get(itemId).put(caller, itemChangedListener);
        mItemCallbacks.get(itemId).get(caller).itemChanged(ITEMS.get(itemId));
    }

    private ItemChangedListener createItemChangedListener(final GetItemCallback callback) {
        return new ItemChangedListener() {
            @Override
            public void itemChanged(Item item) {
                if (item != null) {
                    callback.onItemLoaded(item);
                }
            }
        };
    }

    @Override
    public void getNewItemId(@NonNull String userId, @NonNull GetNewItemIdCallback callback) {
        String uniqueID = UUID.randomUUID().toString();
        callback.onNewItemIdLoaded(uniqueID);
    }

    @Override
    public void refreshItems() {
        ITEMS.clear();
    }

    @Override
    public void refreshItem(@NonNull String itemId) {
        // Nothing to do here.
    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {
        ITEMS.put(item.getId(), item);
        if (mItemCallbacks.get(item.getId()) != null) {
            for (ItemChangedListener listener :
                    mItemCallbacks.get(item.getId()).values()) {
                listener.itemChanged(ITEMS.get(item.getId()));
            }
        }
        if (!ITEM_IDS.contains(item.getId())) {
            ITEM_IDS.add(item.getId());
            if (mItemIdsListener != null) {
                mItemIdsListener.itemIdsChanged(ITEM_IDS);
            }
        }
    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull Item item) {
        ITEMS.remove(item.getId());
        ITEM_IDS.remove(item.getId());
    }

    @Override
    public void deleteAllItems(@NonNull String userId) {
        refreshItemIds();
        refreshItems();
    }

    private interface ItemIdsListener {

        void itemIdsChanged(List<String> itemIds);
    }

    private interface ItemChangedListener {

        void itemChanged(Item item);
    }
}
