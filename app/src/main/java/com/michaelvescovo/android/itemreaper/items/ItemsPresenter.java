package com.michaelvescovo.android.itemreaper.items;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.google.firebase.auth.FirebaseAuth;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

public class ItemsPresenter implements ItemsContract.Presenter {

    public final static String ITEMS_CALLER = "items";
    private ItemsContract.View mView;
    private Repository mRepository;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private FirebaseAuth mFirebaseAuth;
    private int mItemsToLoad;
    private boolean mItemsLoaded;

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
    public void getItems(final boolean forceUpdate) {
        mView.setProgressBar(true);
        if (forceUpdate) {
            mRepository.refreshItemIds();
        }
        String userId = mSharedPreferencesHelper.getUserId();
        mItemsLoaded = false;
        EspressoIdlingResource.increment();
        mRepository.getItemIds(userId, new DataSource.GetItemIdsCallback() {
            @Override
            public void onItemIdsLoaded(@Nullable List<String> itemIds, boolean itemRemoved) {
                if (!mItemsLoaded) {
                    mItemsLoaded = true;
                    EspressoIdlingResource.decrement();
                }
                if (itemIds != null) {
                    if (itemRemoved) {
                        mView.clearItems();
                    }
                    if (itemIds.size() > 0) {
                        mView.showNoItemsText(false);
                        for (final String itemId : itemIds) {
                            getItem(itemId);
                        }
                        mView.setProgressBar(false);
                    } else {
                        mView.setProgressBar(false);
                        mView.showNoItemsText(true);
                    }
                } else {
                    mView.clearItems();
                    mView.setProgressBar(false);
                    mView.showNoItemsText(true);
                }
            }
        });
    }

    private void getItem(String itemId) {
        mItemsToLoad++;
        EspressoIdlingResource.increment();
        mRepository.getItem(itemId, ITEMS_CALLER, new DataSource.GetItemCallback() {
            @Override
            public void onItemLoaded(@Nullable Item item) {
                if (mItemsToLoad > 0) {
                    mItemsToLoad--;
                    EspressoIdlingResource.decrement();
                }
                mView.showItem(item);
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
