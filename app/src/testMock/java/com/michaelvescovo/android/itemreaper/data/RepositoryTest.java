package com.michaelvescovo.android.itemreaper.data;


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEMS;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_2;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.USER_ID;
import static com.michaelvescovo.android.itemreaper.item_details.ItemDetailsPresenter.ITEM_DETAILS_CALLER;
import static com.michaelvescovo.android.itemreaper.items.ItemsPresenter.ITEMS_CALLER;
import static com.michaelvescovo.android.itemreaper.util.Constants.SORT_BY_EXPIRY_STRING;
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
    private DataSource.GetItemsCallback mGetItemsCallback;
    @Captor
    private ArgumentCaptor<DataSource.GetItemsCallback> mItemsCallbackCaptor;
    @Mock
    private DataSource.GetNewItemIdCallback mGetNewItemIdCallback;
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
    public void getItems_CacheAfterFirstCall() {
        // First call.
        mRepository.getItems(USER_ID, SORT_BY_EXPIRY_STRING, ITEMS_CALLER, mGetItemsCallback);
        // Trigger callback.
        verify(mRemoteDataSource).getItems(anyString(), anyString(), anyString(),
                mItemsCallbackCaptor.capture());
        // Set the callback data.
        mItemsCallbackCaptor.getValue().onItemsLoaded(ITEMS);
        // Second call.
        mRepository.getItems(USER_ID, SORT_BY_EXPIRY_STRING, ITEMS_CALLER, mGetItemsCallback);
        // Confirm the total calls to the remote data source is only 1; the cache was used.
        verify(mRemoteDataSource, times(1)).getItems(anyString(), anyString(), anyString(),
                any(DataSource.GetItemsCallback.class));
    }

    @Test
    public void noItems_getItemsReturnsEmptyList() {
        List<Item> emptyList = new ArrayList<>();
        mRepository.getItems(USER_ID, SORT_BY_EXPIRY_STRING, ITEMS_CALLER, mGetItemsCallback);
        // Trigger callback.
        verify(mRemoteDataSource).getItems(anyString(), anyString(), anyString(),
                mItemsCallbackCaptor.capture());
        // Set the callback data.
        mItemsCallbackCaptor.getValue().onItemsLoaded(emptyList);
        // Returns null
        verify(mGetItemsCallback).onItemsLoaded(emptyList);
    }

    @Test
    public void getItem_CacheAfterFirstCall() {
        // First call.
        mRepository.getItem(ITEM_1.getId(), USER_ID, ITEM_DETAILS_CALLER, mGetItemCallback);
        // Trigger callback.
        verify(mRemoteDataSource).getItem(anyString(), anyString(), anyString(),
                mItemCallbackCaptor.capture());
        // Set the callback data.
        mItemCallbackCaptor.getValue().onItemLoaded(ITEM_1);
        // Second call.
        mRepository.getItem(ITEM_1.getId(), USER_ID, ITEM_DETAILS_CALLER, mGetItemCallback);
        // Confirm the total calls to the remote data source is only 1; the cache was used.
        verify(mRemoteDataSource, times(1)).getItem(anyString(), anyString(), anyString(),
                any(DataSource.GetItemCallback.class));
    }

    @Test
    public void getItemNotInCache_queriesRemoteDataSource() {
        // Call to get item not in cache.
        mRepository.getItem(ITEM_2.getId(), USER_ID, ITEM_DETAILS_CALLER, mGetItemCallback);
        // Confirm remote data source is called.
        verify(mRemoteDataSource).getItem(anyString(), anyString(), anyString(),
                any(DataSource.GetItemCallback.class));
    }

    @Test
    public void getItemInCache_remoteDataSourceCalledIfCallbackNotSet() {
        // Add item to cache.
        mRepository.mCachedItems.add(ITEM_1);
        // Call to get item in cache.
        mRepository.getItem(ITEM_1.getId(), USER_ID, ITEM_DETAILS_CALLER, mGetItemCallback);
        // Confirm remote data source is not called.
        verify(mRemoteDataSource, times(1)).getItem(anyString(), anyString(), anyString(),
                any(DataSource.GetItemCallback.class));
    }

    @Test
    public void getItemInCache_remoteDataSourceNotCalledIfCallbackSet() {
        // Add item to cache.
        mRepository.mCachedItems.add(ITEM_1);
        // Set callback
        mRepository.mItemCallers.add(ITEM_DETAILS_CALLER);
        // Call to get item in cache.
        mRepository.getItem(ITEM_1.getId(), USER_ID, ITEM_DETAILS_CALLER, mGetItemCallback);
        // Confirm remote data source is not called.
        verify(mRemoteDataSource, never()).getItem(anyString(), anyString(), anyString(),
                any(DataSource.GetItemCallback.class));
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
        mRepository.deleteItem(USER_ID, ITEM_1);
        // Confirm remote data source called.
        verify(mRemoteDataSource).deleteItem(anyString(), any(Item.class));
    }

    @Test
    public void getNewItemId() {
        mRepository.getNewItemId(USER_ID, mGetNewItemIdCallback);
        verify(mRemoteDataSource).getNewItemId(anyString(),
                any(DataSource.GetNewItemIdCallback.class));
    }
}
