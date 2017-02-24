package com.michaelvescovo.android.itemreaper.items;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.michaelvescovo.android.itemreaper.R.id.expiry;

/**
 * @author Michael Vescovo
 */

public class ItemsFragment extends Fragment implements ItemsContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.no_items)
    TextView mNoItems;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private Callback mCallback;
    private ItemsContract.Presenter mPresenter;
    private ItemsAdapter mItemsAdapter;
    private DividerItemDecoration mDividerItemDecoration;

    public ItemsFragment() {}

    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    @Override
    public void setPresenter(@NonNull ItemsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemListener itemListener = new ItemListener() {
            @Override
            public void onItemClick(Item item) {
                mPresenter.openItemDetails(item);
            }
        };
        mItemsAdapter = new ItemsAdapter(new HashMap<String, Item>(), itemListener);
        mDividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.divider));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_items, container, false);
        ButterKnife.bind(this, root);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        mRecyclerView.setAdapter(mItemsAdapter);
        mRecyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.clearListeners();
        mPresenter.getItems(true);
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
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                mPresenter.openAbout();
                break;
            case R.id.action_sign_out:
                mPresenter.openSignOut();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setProgressBar(boolean active) {
        if (active) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showItem(Item item) {
        mItemsAdapter.replaceItem(item);
    }

    @Override
    public void showItemDetailsUi(String itemId) {

    }

    @Override
    public void showEditItemUi() {
        mCallback.onEditItemSelected();
    }

    @Override
    public void showAboutUi() {
        mCallback.onAboutSelected();
    }

    @Override
    public void showAuthUi() {
        mCallback.onSignOutSelected();
    }

    @Override
    public void showNoItemsText(boolean active) {
        if (active) {
            mNoItems.setVisibility(View.VISIBLE);
        } else {
            mNoItems.setVisibility(View.GONE);
        }
    }

    public interface Callback {

        void onAboutSelected();

        void onSignOutSelected();

        void onEditItemSelected();
    }

    private interface ItemListener {

        void onItemClick(Item item);
    }

    public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

        private Map<String, Item> mItems;
        private List<String> mItemIds;
        private ItemListener mItemListener;

        ItemsAdapter(Map<String, Item> items, ItemListener itemListener) {
            mItems = items;
            mItemIds = new ArrayList<>();
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            mItems.keySet().toArray();
            holder.mCategory.setText(mItems.get(mItemIds.get(position)).getCategory());
            holder.mType.setText(mItems.get(mItemIds.get(position)).getType());
            holder.mColour.setText(mItems.get(mItemIds.get(position)).getPrimaryColour());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mItems.get(mItemIds.get(position)).getExpiry());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault());
            String expiry = "Expires: " + simpleDateFormat.format(calendar.getTime());
            holder.mExpiry.setText(expiry);
            String price = "Paid: $" + String.valueOf(mItems.get(mItemIds.get(position)).getPricePaid());
            holder.mPaid.setText(price);
        }

        @Override
        public int getItemCount() {
            return mItemIds.size();
        }

        void replaceItem(@NonNull Item item) {
            mItems.put(item.getId(), item);
            if (!mItemIds.contains(item.getId())) {
                mItemIds.add(item.getId());
            }
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView mCategory;
            TextView mType;
            TextView mColour;
            TextView mExpiry;
            TextView mPaid;
            ImageView mExpire;

            ViewHolder(final View itemView) {
                super(itemView);
                mCategory = (TextView) itemView.findViewById(R.id.category);
                mType = (TextView) itemView.findViewById(R.id.type);
                mColour = (TextView) itemView.findViewById(R.id.colour);
                mExpiry = (TextView) itemView.findViewById(expiry);
                mPaid = (TextView) itemView.findViewById(R.id.paid);
                mExpire = (ImageView) itemView.findViewById(R.id.expire);
                mExpire.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                Item item = mItems.get(mItemIds.get(position));
                mItemListener.onItemClick(item);
            }
        }
    }
}
