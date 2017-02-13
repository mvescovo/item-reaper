package com.michaelvescovo.android.itemreaper.edit_item;

import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.FakeDataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * @author Michael Vescovo
 */

public class EditItemPresenterTest {

    private EditItemPresenter mEditItemPresenter;

    @Mock
    private EditItemContract.View mView;

    @Mock
    private Repository mRepository;

    @Mock
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mEditItemPresenter = new EditItemPresenter(mView, mRepository, mSharedPreferencesHelper);
    }

    @Test
    public void clickSave_SavesItem() {
        mEditItemPresenter.saveItem(FakeDataSource.ITEM_1);
        verify(mView).setProgressBar(true);
        verify(mRepository).saveItem(anyString(), any(Item.class));
        verify(mView).setProgressBar(false);
        verify(mView).showItemsUi();
    }
}
