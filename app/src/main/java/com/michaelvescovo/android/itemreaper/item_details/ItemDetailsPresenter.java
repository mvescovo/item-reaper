package com.michaelvescovo.android.itemreaper.item_details;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.google.firebase.auth.FirebaseAuth;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import javax.inject.Inject;

/**
 * @author Michael Vescovo
 */

class ItemDetailsPresenter implements ItemDetailsContract.Presenter {

    private final static String ITEM_DETAILS_CALLER = "item_details";
    private ItemDetailsContract.View mView;
    private Repository mRepository;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private FirebaseAuth mFirebaseAuth;


    @Inject
    ItemDetailsPresenter(ItemDetailsContract.View view, Repository repository,
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
    public void displayItem(@NonNull String itemId) {

        mRepository.getItem(itemId, mSharedPreferencesHelper.getUserId(), ITEM_DETAILS_CALLER,
                new DataSource.GetItemCallback() {
            @Override
            public void onItemLoaded(@Nullable Item item) {
                if (item != null) {
                    mView.showItem(item);
                }
            }
        });
    }

    @Override
    public void openEditItem() {
        mView.showEditItemUi();
    }

    @Override
    public void expireItem(@NonNull Item item) {
        item.setDeceased(true);
        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
        mView.showItemExpiredMessage(R.string.item_expired, Snackbar.LENGTH_LONG, item);
        mView.showExpireMenuButton(false);
    }

    @Override
    public void unexpireItem(@NonNull Item item) {
        item.setDeceased(false);
        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
        mView.showItemExpiredMessage(R.string.item_unexpired, Snackbar.LENGTH_LONG, null);
        mView.showExpireMenuButton(true);
    }
}
