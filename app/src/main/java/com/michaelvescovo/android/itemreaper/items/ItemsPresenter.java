package com.michaelvescovo.android.itemreaper.items;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        mRepository.getItemIds(userId, new DataSource.GetItemIdsCallback() {
            @Override
            public void onItemIdsLoaded(@Nullable List<String> itemIds) {
                if (itemIds != null) {
                    if (itemIds.size() > 0) {
                        mView.showNoItemsText(false);
                        final Map<String, Item> items = new HashMap<>();
                        for (final String itemId :
                                itemIds) {
                            mRepository.getItem(itemId, new DataSource.GetItemCallback() {
                                @Override
                                public void onItemLoaded(@Nullable Item item) {
                                    items.put(itemId, item);
                                }
                            });
                        }
                        mView.setProgressBar(false);
                        mView.showItems(items);
                    } else {
                        mView.setProgressBar(false);
                        mView.showNoItemsText(true);
                    }
                }
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
