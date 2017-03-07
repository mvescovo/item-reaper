package com.michaelvescovo.android.itemreaper.items;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
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

class ItemsPresenter implements ItemsContract.Presenter {

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
    public void getItems(final boolean forceUpdate) {
        mView.setProgressBar(true);
        if (forceUpdate) {
            mRepository.refreshItemIds();
        }
        String userId = mSharedPreferencesHelper.getUserId();
        EspressoIdlingResource.increment();
        mRepository.getItemIds(userId, new DataSource.GetItemIdsCallback() {
            @Override
            public void onItemIdsLoaded(@Nullable List<String> itemIds) {
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                if (itemIds != null) {
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
                }
            }
        });
    }

    private void getItem(String itemId) {
        EspressoIdlingResource.increment();
        mRepository.getItem(itemId, "items", new DataSource.GetItemCallback() {
            @Override
            public void onItemLoaded(@Nullable Item item) {
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                mView.showItem(item);
            }
        });
    }


    @Override
    public void openItemDetails(Item item) {
        mView.showItemDetailsUi(item.getId());
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
}
