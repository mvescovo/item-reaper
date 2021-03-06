package com.michaelvescovo.android.itemreaper.items;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;

import com.google.firebase.auth.FirebaseAuth;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import java.util.List;

import javax.inject.Inject;

import static com.michaelvescovo.android.itemreaper.items.SortItemsDialogFragment.SORT_BY_PURCHASE_DATE;
import static com.michaelvescovo.android.itemreaper.util.Constants.SORT_BY_EXPIRY_STRING;
import static com.michaelvescovo.android.itemreaper.util.Constants.SORT_BY_PURCHASE_DATE_STRING;

/**
 * @author Michael Vescovo
 */

public class ItemsPresenter implements ItemsContract.Presenter {

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    public final static String ITEMS_CALLER = "items";
    private ItemsContract.View mView;
    private Repository mRepository;
    private FirebaseAuth mFirebaseAuth;
    private String mUid;

    @Inject
    ItemsPresenter(@NonNull ItemsContract.View view, @NonNull Repository repository,
                   @NonNull FirebaseAuth firebaseAuth, @Nullable String uid) {
        mView = view;
        mRepository = repository;
        mFirebaseAuth = firebaseAuth;
        mUid = uid;
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getItems(int sortBy) {
        if (mUid == null) {
            mView.showSignIn();
        } else {
            mView.setProgressBar(true);
            String sortString;
            if (sortBy == SORT_BY_PURCHASE_DATE) {
                sortString = SORT_BY_PURCHASE_DATE_STRING;
            } else {
                sortString = SORT_BY_EXPIRY_STRING;
            }
            EspressoIdlingResource.increment();
            mRepository.getItems(mUid, sortString, ITEMS_CALLER, new DataSource.GetItemsCallback() {
                @Override
                public void onItemsLoaded(@Nullable List<Item> items) {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement();
                    }
                    if (items != null && items.size() > 0) {
                        mView.showNoItemsText(false);
                    } else {
                        mView.showNoItemsText(true);
                    }
                    mView.setProgressBar(false);
                    mView.showItems(items);
                }
            });
        }
    }

    @Override
    public void openItemDetails(String itemId) {
        mView.showItemDetailsUi(itemId);
    }

    @Override
    public void openAddItem() {
        mView.showEditItemUi();
    }

    @Override
    public void openAbout() {
        mView.showAboutUi();
    }

    @Override
    public void openSignOut() {
        setUid(null);
        mFirebaseAuth.signOut();
        mView.showAuthUi();
    }

    @Override
    public void restoreItem(@NonNull Item item) {
        mRepository.saveItem(mUid, item);
    }

    @Override
    public void expireItem(@NonNull Item item) {
        item.setDeceased(true);
        mRepository.saveItem(mUid, item);
        mView.showItemExpiredMessage(R.string.item_expired, Snackbar.LENGTH_LONG, item);
    }

    @Override
    public void unexpireItem(@NonNull Item item) {
        item.setDeceased(false);
        mRepository.saveItem(mUid, item);
        mView.showItemExpiredMessage(R.string.item_unexpired, Snackbar.LENGTH_LONG, null);
    }

    @Override
    public void itemsSizeChanged(int newSize) {
        if (newSize == 0) {
            mView.showNoItemsText(true);
        } else {
            mView.showNoItemsText(false);
        }
    }

    @Override
    public void setUid(String uid) {
        mUid = uid;
    }
}
