package com.michaelvescovo.android.itemreaper.items;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.michaelvescovo.android.itemreaper.data.Item;

import java.util.Map;

/**
 * @author Michael Vescovo
 */

public class ItemsFragment extends Fragment implements ItemsContract.View {

    ItemsContract.Presenter mPresenter;

    public ItemsFragment() {}

    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    @Override
    public void setPresenter(@NonNull ItemsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setProgressBar(boolean visible) {

    }

    @Override
    public void showItems(Map<String, Item> items) {

    }

    @Override
    public void showItemDetailsUi(String itemId) {

    }
}
