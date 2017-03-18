package com.michaelvescovo.android.itemreaper.items;

import com.google.firebase.auth.FirebaseAuth;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
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
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Mock
    private FirebaseAuth mFirebaseAuth;

    @Captor
    private ArgumentCaptor<DataSource.GetItemIdsCallback> mGetItemIdsCallbackCaptor;

    @Captor
    private ArgumentCaptor<DataSource.GetItemCallback> mGetItemCallbackCaptor;

    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[] {
                new ArrayList<String>() {{
                    add("1");
                }},
                new ArrayList<String>() {{

                }},
        };
    }

    private List mItemIds;

    public ItemsPresenterTest(List itemIds) {
        mItemIds = itemIds;
    }

    @Before
    public void setItemsPresenter() {
        MockitoAnnotations.initMocks(this);
        mItemsPresenter = new ItemsPresenter(mView, mRepository, mSharedPreferencesHelper,
                mFirebaseAuth);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getItemsFromRepositoryAndDisplayInView() {
        mItemsPresenter.getItems(true);

        verify(mView).setProgressBar(true);

        verify(mRepository).refreshItemIds();

        // Make sure to get the correct userId to pass in.
        verify(mSharedPreferencesHelper).getUserId();

        // Get all itemIds for that userId and stub the result.
        verify(mRepository).getItemIds(anyString(), mGetItemIdsCallbackCaptor.capture());
        mGetItemIdsCallbackCaptor.getValue().onItemIdsLoaded(mItemIds);

        if (mItemIds.size() > 0) {
            verify(mView).showNoItemsText(false);

            // Get an item for each itemId.
            verify(mRepository, times(mItemIds.size())).getItem(anyString(), anyString(),
                    mGetItemCallbackCaptor.capture());

            // Stub a result for each getItem call
            for (int i = 0; i < mItemIds.size(); i++) {
                mGetItemCallbackCaptor.getValue().onItemLoaded(any(Item.class));
            }

            // Show an item for each itemId.
            verify(mView, times(mItemIds.size())).showItem(any(Item.class));

            verify(mView).setProgressBar(false);
        } else {
            verify(mView).setProgressBar(false);

            verify(mView).showNoItemsText(true);
        }
    }

    @Test
    public void clickItem_ShowsItemDetailsUi() {
        mItemsPresenter.openItemDetails(ITEM_1);
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
        mItemsPresenter.expireItem(ITEM_1, 1);
        assertThat(ITEM_1.getDeceased(), is(equalTo(true)));
        verify(mRepository).saveItem(anyString(), any(Item.class));
        verify(mView).showItemExpiredMessage(anyInt(), anyInt(), any(Item.class));
        verify(mView).showNoItemsText(true);
    }

    @Test
    public void unexpireItem_UnexpiresAndSavesItem() {
        mItemsPresenter.unexpireItem(ITEM_1);
        assertThat(ITEM_1.getDeceased(), is(equalTo(false)));
        verify(mRepository).saveItem(anyString(), any(Item.class));
        verify(mView).showItemExpiredMessage(anyInt(), anyInt(), any(Item.class));
        verify(mView).showNoItemsText(false);
    }
}
