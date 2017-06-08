package com.michaelvescovo.android.itemreaper.items;

import com.google.firebase.auth.FirebaseAuth;
import com.michaelvescovo.android.itemreaper.data.DataSource;
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

import java.util.ArrayList;
import java.util.List;

import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.USER_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized.class)
public class ItemsPresenterTest {

    private ItemsPresenter mItemsPresenter;

    @Mock
    private ItemsContract.View mView;

    @Mock
    private Repository mRepository;

    @Mock
    private FirebaseAuth mFirebaseAuth;

    @Captor
    private ArgumentCaptor<DataSource.GetItemsCallback> mGetItemsCallbackCaptor;
    private List mItems;

    public ItemsPresenterTest(List items) {
        mItems = items;
    }

    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[]{
                new ArrayList<Item>() {{
                    add(ITEM_1);
                }},
                new ArrayList<String>() {{
                    // Empty list
                }},
                null
        };
    }

    @Before
    public void setItemsPresenter() {
        MockitoAnnotations.initMocks(this);
        mItemsPresenter = new ItemsPresenter(mView, mRepository, mFirebaseAuth, USER_ID);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getItemsFromRepositoryAndDisplayInView() {
        mItemsPresenter.getItems(anyInt());
        verify(mView).setProgressBar(true);

        // Get all items for the userId and stub the result.
        verify(mRepository).getItems(anyString(), anyString(), anyString(),
                mGetItemsCallbackCaptor.capture());
        mGetItemsCallbackCaptor.getValue().onItemsLoaded(mItems);

        if (mItems != null && mItems.size() > 0) {
            verify(mView).showNoItemsText(false);
            verify(mView).showItems(any(List.class));
            verify(mView).setProgressBar(false);
        } else {
            verify(mView).showNoItemsText(true);
            verify(mView).setProgressBar(false);
        }
    }

    @Test
    public void clickItem_ShowsItemDetailsUi() {
        mItemsPresenter.openItemDetails(ITEM_1.getId());
        verify(mView).showItemDetailsUi(anyString());
    }

    @Test
    public void clickAddItem_ShowsAddItemUi() {
        mItemsPresenter.openAddItem();
        verify(mView).showEditItemUi();
    }

    @Test
    public void clickAboutMenuItem_ShowsAboutUi() {
        mItemsPresenter.openAbout();
        verify(mView).showAboutUi();
    }

    @Test
    public void clickSignOutMenuItem_SignsOut() {
        mItemsPresenter.openSignOut();
        verify(mFirebaseAuth).signOut();
        verify(mView).showAuthUi();
    }

    @Test
    public void expireItem_ExpiresAndSavesItem() {
        mItemsPresenter.expireItem(ITEM_1);
        assertThat(ITEM_1.getDeceased(), is(equalTo(true)));
        verify(mRepository).saveItem(anyString(), any(Item.class));
        verify(mView).showItemExpiredMessage(anyInt(), anyInt(), any(Item.class));
    }

    @Test
    public void unexpireItem_UnexpiresAndSavesItem() {
        mItemsPresenter.unexpireItem(ITEM_1);
        assertThat(ITEM_1.getDeceased(), is(equalTo(false)));
        verify(mRepository).saveItem(anyString(), any(Item.class));
        verify(mView).showItemExpiredMessage(anyInt(), anyInt(), any(Item.class));
    }

    @Test
    public void itemsSizeChangedToEmpty_ShowsNoItemsText() {
        mItemsPresenter.itemsSizeChanged(0);
        verify(mView).showNoItemsText(true);
    }

    @Test
    public void itemsSizeChangedToNonEmpty_DoesNotShowNoItemsText() {
        mItemsPresenter.itemsSizeChanged(1);
        verify(mView).showNoItemsText(false);
    }
}
