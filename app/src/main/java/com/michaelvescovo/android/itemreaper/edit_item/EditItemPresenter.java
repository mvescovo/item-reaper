package com.michaelvescovo.android.itemreaper.edit_item;

import android.support.annotation.NonNull;

import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

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

    @Override
    public void saveItem(@NonNull Item item) {
        mView.setProgressBar(true);
        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
        mView.setProgressBar(false);
        mView.showItemsUi();
    }
}
