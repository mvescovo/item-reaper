package com.michaelvescovo.android.itemreaper.data;


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEMS;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEMS_CALLER;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_IDS;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_ID_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_ID_2;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.USER_ID;
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

    private Repository mRepository;
    @Mock
    private DataSource mRemoteDataSource;
    @Mock
    private DataSource.GetItemIdsCallback mGetItemIdsCallback;
    @Mock
    private DataSource.GetNewItemIdCallback mGetNewItemIdCallback;
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
        mRepository.getItem(ITEM_ID_1, ITEMS_CALLER, mGetItemCallback);
        // Trigger callback.
        verify(mRemoteDataSource).getItem(anyString(), anyString(), mItemCallbackCaptor.capture());
        // Set the callback data.
        mItemCallbackCaptor.getValue().onItemLoaded(ITEM_1);
        // Second call.
        mRepository.getItem(ITEM_ID_1, ITEMS_CALLER, mGetItemCallback);
        // Confirm the total calls to the remote data source is only 1; the cache was used.
        verify(mRemoteDataSource, times(1)).getItem(anyString(), anyString(), any(DataSource.GetItemCallback.class));
    }

    @Test
    public void getItemNotInCache_queriesRemoteDataSource() {
        // Make cache not empty.
        mRepository.mCachedItems.put(ITEM_ID_1, ITEM_1);
        // Call to get item not in cache.
        mRepository.getItem(ITEM_ID_2, ITEMS_CALLER, mGetItemCallback);
        // Confirm remote data source is called.
        verify(mRemoteDataSource).getItem(anyString(), anyString(), any(DataSource.GetItemCallback.class));
    }

    @Test
    public void getItemInCache_remoteDataSourceNotCalled() {
        // Make cache not empty.
        mRepository.mCachedItems.put(ITEM_ID_1, ITEM_1);
        // Call to get item in cache.
        mRepository.getItem(ITEM_ID_1, ITEMS_CALLER, mGetItemCallback);
        // Confirm remote data source is not called.
        verify(mRemoteDataSource, never()).getItem(anyString(), anyString(), any(DataSource.GetItemCallback.class));
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

    @Test
    public void getNewItemId() {
        mRepository.getNewItemId(USER_ID, mGetNewItemIdCallback);
        verify(mRemoteDataSource).getNewItemId(anyString(),
                any(DataSource.GetNewItemIdCallback.class));
    }

    @Test
    public void clearItemCache_ClearsCacheForItem() {
        // Add item 1 to the cache
        mRepository.mCachedItems.put(ITEM_ID_1, ITEM_1);

        // Confirm the item is there
        assertThat(mRepository.mCachedItems.containsKey(ITEM_ID_1), is(true));

        // Remove the item
        mRepository.refreshItem(ITEM_ID_1);

        // Confirm the item is gone
        assertThat(mRepository.mCachedItems.containsKey(ITEM_ID_1), is(false));
    }
}
