package com.michaelvescovo.android.itemreaper.items;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

    private Callback mCallback;
    private ItemsContract.Presenter mPresenter;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_items, container, false);

        FloatingActionButton addItem = (FloatingActionButton) getActivity().findViewById(
                R.id.add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openAddItem();
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement callback");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                mPresenter.openAbout();
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void showAboutUi() {
        mCallback.onAboutSelected();
    }

    public interface Callback {

        void onAboutSelected();
    }
}
