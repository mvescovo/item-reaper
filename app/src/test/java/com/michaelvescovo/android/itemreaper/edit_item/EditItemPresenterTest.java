package com.michaelvescovo.android.itemreaper.edit_item;

import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.FakeDataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_ID_1;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized.class)
public class EditItemPresenterTest {

    private EditItemPresenter mEditItemPresenter;

    @Mock
    private EditItemContract.View mView;

    @Mock
    private Repository mRepository;

    @Mock
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Captor
    private ArgumentCaptor<DataSource.GetNewItemIdCallback> mNewItemIdCallbackCaptor;

    @Captor
    private ArgumentCaptor<DataSource.GetItemCallback> mItemCallbackCaptor;

    @Parameterized.Parameters
    public static Iterable<?> data() {
        return Arrays.asList(
                ITEM_1,
                FakeDataSource.ITEM_2
        );
    }

    private Item mItem;

    public EditItemPresenterTest(Item item) {
        mItem = item;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mEditItemPresenter = new EditItemPresenter(mView, mRepository, mSharedPreferencesHelper);
    }

    @Test
    public void invalidId_createsItem() {
        mEditItemPresenter.editItem(null);

        // Gets a new itemId
        verify(mRepository).getNewItemId(anyString(), mNewItemIdCallbackCaptor.capture());

        // New itemId comes back
        mNewItemIdCallbackCaptor.getValue().onNewItemIdLoaded(ITEM_ID_1);

        // Saves the new item
        verify(mRepository).saveItem(anyString(), any(Item.class));
    }

    @Test
    public void validId_ItemIsLoadedForEditing() {
        mEditItemPresenter.editItem(mItem.getId());

        // Does not get new ID
        verify(mRepository, never()).getNewItemId(anyString(),
                any(DataSource.GetNewItemIdCallback.class));

        // Gets the existing item
        verify(mRepository).getItem(anyString(), mItemCallbackCaptor.capture());

        // New item comes back
        mItemCallbackCaptor.getValue().onItemLoaded(mItem);

        // Displays the existing item in the view
        verify(mView).showExistingItem(any(Item.class));
    }

    @Test
    public void itemChanged_SavesItem() {
        mEditItemPresenter.saveItem(mItem);
        verify(mRepository).saveItem(anyString(), any(Item.class));
    }

    @Test
    public void clickDone_ShowsItemsUi() {
        mEditItemPresenter.doneEditing();
        verify(mView).showItemsUi();
    }
}
