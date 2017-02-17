package com.michaelvescovo.android.itemreaper.edit_item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

class EditItemPresenter implements EditItemContract.Presenter {

    private EditItemContract.View mView;
    private Repository mRepository;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Inject
    EditItemPresenter(EditItemContract.View view, Repository repository,
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
    public void editItem(@Nullable String itemId) {
        if (itemId == null) {
            createNewItem();
        } else {
            loadExistingItem(itemId);
        }
    }

    @Override
    public void saveItem(@NonNull Item item) {
        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
    }

    @Override
    public void doneEditing() {
        mView.showItemsUi();
    }

    private void createNewItem() {
        EspressoIdlingResource.increment();
        mRepository.getNewItemId(mSharedPreferencesHelper.getUserId(), new DataSource.GetNewItemIdCallback() {
            @Override
            public void onNewItemIdLoaded(@Nullable String newItemId) {
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                if (newItemId != null) {
                    Item newItem = new Item(newItemId);
                    mRepository.saveItem(mSharedPreferencesHelper.getUserId(), newItem);
                }
            }
        });
    }

    private void loadExistingItem(String itemId) {
        EspressoIdlingResource.increment();
        mRepository.getItem(itemId, new DataSource.GetItemCallback() {
            @Override
            public void onItemLoaded(@Nullable Item item) {
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                mView.showExistingItem(item);
            }
        });
    }
}
