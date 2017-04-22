package com.michaelvescovo.android.itemreaper.items;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;

import com.google.firebase.auth.FirebaseAuth;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import javax.inject.Inject;

import static com.michaelvescovo.android.itemreaper.util.Constants.ITEM_ADDED;
import static com.michaelvescovo.android.itemreaper.util.Constants.ITEM_CHANGED;
import static com.michaelvescovo.android.itemreaper.util.Constants.ITEM_MOVED;
import static com.michaelvescovo.android.itemreaper.util.Constants.ITEM_REMOVED;

/**
 * @author Michael Vescovo
 */

public class ItemsPresenter implements ItemsContract.Presenter {

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    public final static String ITEMS_CALLER = "items";
    private ItemsContract.View mView;
    private Repository mRepository;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private FirebaseAuth mFirebaseAuth;

    @Inject
    ItemsPresenter(ItemsContract.View view, Repository repository,
                   SharedPreferencesHelper sharedPreferencesHelper, FirebaseAuth firebaseAuth) {
        mView = view;
        mRepository = repository;
        mSharedPreferencesHelper = sharedPreferencesHelper;
        mFirebaseAuth = firebaseAuth;
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getItems() {
        mView.setProgressBar(true);
        String userId = mSharedPreferencesHelper.getUserId();
        EspressoIdlingResource.increment();
        mRepository.getItems(userId, ITEMS_CALLER, new DataSource.GetItemsCallback() {
            @Override
            public void onItemLoaded(@Nullable Item item, @NonNull String action) {
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                switch (action) {
                    case ITEM_ADDED:
                        mView.addItem(item);
                        break;
                    case ITEM_CHANGED:
                        mView.changeItem(item);
                        break;
                    case ITEM_REMOVED:
                        mView.removeItem(item);
                        break;
                    case ITEM_MOVED:
                        mView.moveItem();
                }
                mView.setProgressBar(false);
            }
        });
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
        mFirebaseAuth.signOut();
        mView.showAuthUi();
    }

    @Override
    public void restoreItem(@NonNull Item item) {
        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
    }

    @Override
    public void expireItem(@NonNull Item item) {
        item.setDeceased(true);
        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
        mView.showItemExpiredMessage(R.string.item_expired, Snackbar.LENGTH_LONG, item);
    }

    @Override
    public void unexpireItem(@NonNull Item item) {
        item.setDeceased(false);
        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
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
}
