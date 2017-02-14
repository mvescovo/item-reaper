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
    public void saveItem(@NonNull final Item item) {
        mView.setProgressBar(true);
        if (item.getId().equals("-1")) {
            EspressoIdlingResource.increment();
            mRepository.getNewItemId(mSharedPreferencesHelper.getUserId(),
                    new DataSource.GetNewItemIdCallback() {
                @Override
                public void onNewItemIdLoaded(@Nullable String newItemId) {
                    EspressoIdlingResource.decrement();
                    if (newItemId != null) {
                        item.setId(newItemId);
                        finishSavingItem(item);
                    }
                }
            });
        } else {
            finishSavingItem(item);
        }
    }

    private void finishSavingItem(@NonNull final Item item) {
        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
        mView.setProgressBar(false);
        mView.showItemsUi();
    }
}
