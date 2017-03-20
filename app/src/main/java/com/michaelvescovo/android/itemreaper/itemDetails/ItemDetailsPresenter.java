package com.michaelvescovo.android.itemreaper.itemDetails;

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

import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_DETAILS_CALLER;

/**
 * @author Michael Vescovo
 */

class ItemDetailsPresenter implements ItemDetailsContract.Presenter {

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

        mRepository.getItem(itemId, ITEM_DETAILS_CALLER, new DataSource.GetItemCallback() {
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
    }

    @Override
    public void unexpireItem(@NonNull Item item) {
        item.setDeceased(false);
        mRepository.saveItem(mSharedPreferencesHelper.getUserId(), item);
        mView.showItemExpiredMessage(R.string.item_unexpired, Snackbar.LENGTH_LONG, null);
    }
}
