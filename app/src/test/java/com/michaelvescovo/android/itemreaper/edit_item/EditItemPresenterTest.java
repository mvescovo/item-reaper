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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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

    @Parameterized.Parameters
    public static Iterable<?> data() {
        return Arrays.asList(
                FakeDataSource.ITEM_1,
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
    public void clickSave_SavesItem() {
        mEditItemPresenter.saveItem(mItem);
        verify(mView).setProgressBar(true);
        if (mItem.getId().equals("-1")) {
            verify(mRepository).getNewItemId(anyString(), mNewItemIdCallbackCaptor.capture());
            mNewItemIdCallbackCaptor.getValue().onNewItemIdLoaded("newId");
        }
        assertThat(mItem.getId(), not(equalTo("-1")));
        verify(mRepository).saveItem(anyString(), any(Item.class));
        verify(mView).setProgressBar(false);
        verify(mView).showItemsUi();
    }
}
