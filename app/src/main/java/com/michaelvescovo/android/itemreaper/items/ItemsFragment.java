package com.michaelvescovo.android.itemreaper.items;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.add_item.AddItemActivity;
import com.michaelvescovo.android.itemreaper.data.Item;

import java.util.Map;

/**
 * @author Michael Vescovo
 */

public class ItemsFragment extends Fragment implements ItemsContract.View {

    ItemsContract.Presenter mPresenter;
    FloatingActionButton mAddItem;

    public ItemsFragment() {}

    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    @Override
    public void setPresenter(@NonNull ItemsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_items, container, false);

        mAddItem = (FloatingActionButton) getActivity().findViewById(R.id.add_item);
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openAddItem();
            }
        });

        return root;
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

    @Override
    public void showAddItemUi() {
        Intent intent = new Intent(getContext(), AddItemActivity.class);
        startActivity(intent);
    }
}
