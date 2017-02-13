package com.michaelvescovo.android.itemreaper.edit_item;

import android.support.annotation.NonNull;

import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

class EditItemPresenter implements EditItemContract.Presenter {

    private EditItemContract.View mView;
    private Repository mRepository;

    @Inject
    EditItemPresenter(EditItemContract.View view, Repository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {
        mView.setProgressBar(true);
        mRepository.saveItem(userId, item);
        mView.setProgressBar(false);
        mView.showItemsUi();
    }
}
