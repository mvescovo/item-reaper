package com.michaelvescovo.android.itemreaper.item_details;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

class ItemDetailsPresenter implements ItemDetailsContract.Presenter {

    private final static String ITEM_DETAILS_CALLER = "item_details";

    private ItemDetailsContract.View mView;
    private Repository mRepository;
    private String mUid;


    @Inject
    ItemDetailsPresenter(ItemDetailsContract.View view, Repository repository, String uid) {
        mView = view;
        mRepository = repository;
        mUid = uid;
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void displayItem(@NonNull String itemId) {
        if (mUid == null) {
            mView.showSignIn();
        } else {
            EspressoIdlingResource.increment();
            mRepository.getItem(itemId, mUid, ITEM_DETAILS_CALLER,
                    new DataSource.GetItemCallback() {
                        @Override
                        public void onItemLoaded(@Nullable Item item) {
                            if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                                EspressoIdlingResource.decrement();
                            }
                            if (item != null) {
                                mView.showItem(item);
                            }
                        }
                    });
        }
    }

    @Override
    public void openEditItem() {
        mView.showEditItemUi();
    }

    @Override
    public void expireItem(@NonNull Item item) {
        item.setDeceased(true);
        mRepository.saveItem(mUid, item);
        mView.showItemExpiredMessage(R.string.item_expired, Snackbar.LENGTH_LONG, item);
        mView.showExpireMenuButton(false);
    }

    @Override
    public void unexpireItem(@NonNull Item item) {
        item.setDeceased(false);
        mRepository.saveItem(mUid, item);
        mView.showItemExpiredMessage(R.string.item_unexpired, Snackbar.LENGTH_LONG, null);
        mView.showExpireMenuButton(true);
    }
}
