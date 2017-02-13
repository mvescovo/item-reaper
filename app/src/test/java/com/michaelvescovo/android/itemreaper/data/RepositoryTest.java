package com.michaelvescovo.android.itemreaper.data;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Michael Vescovo
 */

public class RepositoryTest {

    private final static String USER_ID = "testUser";
    private final static String ITEM_ID_1 = "1";
    private final static String ITEM_ID_2 = "2";
    private final static List<String> ITEM_IDS = Lists.newArrayList(ITEM_ID_1, ITEM_ID_2);
    private static Item ITEM_1 = new Item("1", null, 20, 0, "1/1/1", "Clothing", null,
            "T-shirt", null, null, null, "Black", null, null, null, null, null, null, null, null,
            false);
    private static Item ITEM_2 = new Item("2", null, 30, 0, "2/2/2", "Bathroom", null,
            "Towel", null, null, null, "White", null, null, null, null, null, null, null, null,
            false);
    private final static Map<String, Item> ITEMS = Maps.newHashMap();
    static {
        ITEMS.put(ITEM_ID_1, ITEM_1);
        ITEMS.put(ITEM_ID_2, ITEM_2);
    }
    private Repository mRepository;
    @Mock
    private DataSource mRemoteDataSource;
    @Mock
    private DataSource.GetItemIdsCallback mGetItemIdsCallback;
    @Captor
    private ArgumentCaptor<DataSource.GetItemIdsCallback> mItemIdsCallbackCaptor;
    @Captor
    private ArgumentCaptor<DataSource.GetItemCallback> mItemCallbackCaptor;
    @Mock
    private DataSource.GetItemCallback mGetItemCallback;

    @Before
    public void setupRepository() {
        MockitoAnnotations.initMocks(this);
        mRepository = new Repository(mRemoteDataSource);
    }

    @Test
    public void getItemIds_CacheAfterFirstCall() {
        // First call.
        mRepository.getItemIds(USER_ID, mGetItemIdsCallback);
        // Trigger callback.
        verify(mRemoteDataSource).getItemIds(anyString(), mItemIdsCallbackCaptor.capture());
        // Set the callback data.
        mItemIdsCallbackCaptor.getValue().onItemIdsLoaded(ITEM_IDS);
        // Second call.
        mRepository.getItemIds(USER_ID, mGetItemIdsCallback);
        // Confirm the total calls to the remote data source is only 1; the cache was used.
        verify(mRemoteDataSource, times(1)).getItemIds(anyString(), any(DataSource.GetItemIdsCallback.class));
    }

    @Test
    public void stopGetItemIds() {
        // Call repository.
        mRepository.stopGetItemIds();
        // Confirm remote data source called.
        verify(mRemoteDataSource).stopGetItemIds();
    }

    @Test
    public void refreshItemIds_CacheIsEmptied() {
        // Add itemIds to cache.
        mRepository.mCachedItemIds = ITEM_IDS;
        // Confirm cache is not empty.
        assertThat(mRepository.mCachedItemIds.size(), is(ITEM_IDS.size()));
        // Refresh itemIds.
        mRepository.refreshItemIds();
        // Confirm cache is now empty.
        assertThat(mRepository.mCachedItemIds, equalTo(null));
    }

    @Test
    public void getItem_CacheAfterFirstCall() {
        // First call.
        mRepository.getItem(ITEM_ID_1, mGetItemCallback);
        // Trigger callback.
        verify(mRemoteDataSource).getItem(anyString(), mItemCallbackCaptor.capture());
        // Set the callback data.
        mItemCallbackCaptor.getValue().onItemLoaded(ITEM_1);
        // Second call.
        mRepository.getItem(ITEM_ID_1, mGetItemCallback);
        // Confirm the total calls to the remote data source is only 1; the cache was used.
        verify(mRemoteDataSource, times(1)).getItem(anyString(), any(DataSource.GetItemCallback.class));
    }

    @Test
    public void stopGetItem() {
        // Cal repository.
        mRepository.stopGetItem();
        // Confirm remote data source called.
        verify(mRemoteDataSource).stopGetItem();
    }

    @Test
    public void getItemNotInCache_queriesRemoteDataSource() {
        // Make cache not empty.
        mRepository.mCachedItems.put(ITEM_ID_1, ITEM_1);
        // Call to get item not in cache.
        mRepository.getItem(ITEM_ID_2, mGetItemCallback);
        // Confirm remote data source is called.
        verify(mRemoteDataSource).getItem(anyString(), any(DataSource.GetItemCallback.class));
    }

    @Test
    public void getItemInCache_remoteDataSourceNotCalled() {
        // Make cache not empty.
        mRepository.mCachedItems.put(ITEM_ID_1, ITEM_1);
        // Call to get item in cache.
        mRepository.getItem(ITEM_ID_1, mGetItemCallback);
        // Confirm remote data source is not called.
        verify(mRemoteDataSource, never()).getItem(anyString(), any(DataSource.GetItemCallback.class));
    }

    @Test
    public void refreshItems_CacheIsEmptied() {
        // Add items to cache.
        mRepository.mCachedItems = ITEMS;
        // Confirm cache is not empty.
        assertThat(mRepository.mCachedItems.size(), is(ITEMS.size()));
        // Refresh items.
        mRepository.refreshItems();
        // Confirm cache is now empty.
        assertThat(mRepository.mCachedItems.size(), is(0));
    }

    @Test
    public void saveItem() {
        // Call to add item.
        mRepository.saveItem(USER_ID, ITEM_1);
        // Confirm remote data source called.
        verify(mRemoteDataSource).saveItem(anyString(), any(Item.class));
    }

    @Test
    public void deleteItem() {
        // Call to delete item.
        mRepository.deleteItem(USER_ID, ITEM_ID_1);
        // Confirm remote data source called.
        verify(mRemoteDataSource).deleteItem(anyString(), anyString());
    }
}
