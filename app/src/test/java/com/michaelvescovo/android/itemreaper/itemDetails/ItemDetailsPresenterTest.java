package com.michaelvescovo.android.itemreaper.itemDetails;

import com.google.firebase.auth.FirebaseAuth;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_2;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized.class)
public class ItemDetailsPresenterTest {

    private ItemDetailsPresenter mPresenter;

    @Mock
    private ItemDetailsContract.View mView;

    @Mock
    private Repository mRepository;

    @Mock
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Mock
    private FirebaseAuth mFirebaseAuth;

    @Parameterized.Parameters
    public static Iterable<?> data() {
        return Arrays.asList(
                ITEM_1,
                ITEM_2
        );
    }

    private Item mItem;

    public ItemDetailsPresenterTest(Item item) {
        mItem = item;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new ItemDetailsPresenter(mView, mRepository,
                mSharedPreferencesHelper, mFirebaseAuth);
    }

    @Test
    public void openEditItem() {
        mPresenter.openEditItem();
        verify(mView).showEditItemUi();
    }

    @Test
    public void expireItem_ExpiresAndSavesItem() {
        mPresenter.expireItem(ITEM_1);
        assertThat(ITEM_1.getDeceased(), is(equalTo(true)));
        verify(mRepository).saveItem(anyString(), any(Item.class));
        verify(mView).showItemExpiredMessage(anyInt(), anyInt(), any(Item.class));
    }

    @Test
    public void unexpireItem_UnexpiresAndSavesItem() {
        mPresenter.unexpireItem(ITEM_1);
        assertThat(ITEM_1.getDeceased(), is(equalTo(false)));
        verify(mRepository).saveItem(anyString(), any(Item.class));
        verify(mView).showItemExpiredMessage(anyInt(), anyInt(), any(Item.class));
    }
}