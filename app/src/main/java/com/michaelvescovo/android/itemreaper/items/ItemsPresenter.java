package com.michaelvescovo.android.itemreaper.items;

import android.support.annotation.Nullable;

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

    @Inject
    ItemsPresenter(ItemsContract.View view, Repository repository,
                   SharedPreferencesHelper sharedPreferencesHelper) {
        mView = view;
        mRepository = repository;
        mSharedPreferencesHelper = sharedPreferencesHelper;
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void getItems() {
        mView.setProgressBar(true);
        String userId = mSharedPreferencesHelper.getUserId();
        mRepository.getItemIds(userId, new DataSource.GetItemIdsCallback() {
            @Override
            public void onItemIdsLoaded(@Nullable List<String> itemIds) {
                if (itemIds != null) {
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
        mView.showAddItemUi();
    }

    @Override
    public void openAbout() {
        mView.showAboutUi();
    }
}
