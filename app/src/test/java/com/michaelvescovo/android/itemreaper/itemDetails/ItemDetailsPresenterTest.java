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

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized.class)
public class ItemDetailsPresenterTest {

    private ItemDetailsPresenter mItemDetailsPresenter;

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
        mItemDetailsPresenter = new ItemDetailsPresenter(mView, mRepository,
                mSharedPreferencesHelper, mFirebaseAuth);
    }

    @Test
    public void test() {

    }
}
