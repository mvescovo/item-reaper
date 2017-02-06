package com.michaelvescovo.android.itemreaper.items;

import com.google.common.collect.Lists;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Michael Vescovo
 */

public class ItemsPresenterTest {

    private ItemsPresenter mItemsPresenter;

    @Mock
    private ItemsContract.View mView;

    @Mock
    private Repository mRepository;

    @Mock
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Captor
    private ArgumentCaptor<DataSource.GetItemIdsCallback> mGetItemIdsCallbackCaptor;

    @Captor
    private ArgumentCaptor<DataSource.GetItemCallback> mGetItemCallbackCaptor;

    private static List<String> ITEM_IDS;
    private static Item ITEM;

    @Before
    public void setItemsPresenter() {
        MockitoAnnotations.initMocks(this);
        mItemsPresenter = new ItemsPresenter(mView, mRepository, mSharedPreferencesHelper);
        ITEM_IDS = Lists.newArrayList("1");
        ITEM = new Item("1", "1/1/1", "Clothing", "T-shirt");
    }

    @Test
    public void getItemsFromRepositoryAndDisplayInView() {
        mItemsPresenter.getItems();

        verify(mView).setProgressBar(true);

        // Make sure to get the correct userId to pass in.
        verify(mSharedPreferencesHelper).getUserId();

        // Get all itemIds for that userId and stub the result.
        verify(mRepository).getItemIds(anyString(), mGetItemIdsCallbackCaptor.capture());
        mGetItemIdsCallbackCaptor.getValue().onItemIdsLoaded(ITEM_IDS);

        // Get an item for each itemId.
        verify(mRepository, times(ITEM_IDS.size())).getItem(anyString(),
                any(DataSource.GetItemCallback.class));

        verify(mView).setProgressBar(false);

        verify(mView).showItems(anyMapOf(String.class, Item.class));
    }

    @Test
    public void clickItem_ShowsItemDetailsUi() {
        mItemsPresenter.openItemDetails(ITEM);
        verify(mView).showItemDetailsUi(anyString());
    }
}
