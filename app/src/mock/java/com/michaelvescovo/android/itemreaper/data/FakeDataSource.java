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
            1452776400000L, // 15/1/2016
            20,
            0,
            1496325600000L, // 2/6/2017
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
            -1,
            30,
            -1,
            1496325600000L,
            "Bathroom", null,
            "Towel", null, null, null, "White", null, null, null, null, null, null, null, null,
            false);
    public final static String ITEMS_CALLER = "items";
    public final static String EDIT_ITEM_CALLER = "edit_item";
    private ItemIdsListener mItemIdsListener;
    private Map<String, Map<String, ItemChangedListener>> mItemCallbacks = Maps.newHashMap();
    static {

    }

    FakeDataSource() {
        ITEM_IDS.add(ITEM_ID_1);
        ITEM_IDS.add(ITEM_ID_2);
        ITEMS.put(ITEM_ID_1, ITEM_1);
        ITEMS.put(ITEM_ID_2, ITEM_2);
    }

    @Override
    public void getItemIds(@NonNull String userId, @NonNull final GetItemIdsCallback callback) {
        mItemIdsListener = new ItemIdsListener() {
            @Override
            public void itemIdsChanged(List<String> itemIds) {
                if (itemIds != null) {
                    callback.onItemIdsLoaded(itemIds);
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
    public void deleteItem(@NonNull String userId, @NonNull String itemId) {
        ITEMS.remove(userId);
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
