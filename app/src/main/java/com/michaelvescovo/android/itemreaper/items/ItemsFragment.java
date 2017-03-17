package com.michaelvescovo.android.itemreaper.items;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

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
import static com.michaelvescovo.android.itemreaper.edit_item.EditItemFragment.getPriceFromTotalCents;
import static com.michaelvescovo.android.itemreaper.items.ItemsActivity.EXTRA_DELETED_ITEM;

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
    private boolean mLargeScreen;
    private String mImageUrl;
    private Item mDeletedItem;

    public ItemsFragment() {
    }

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
        mLargeScreen = getResources().getBoolean(R.bool.large_layout);
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

        if (savedInstanceState != null
                && savedInstanceState.getSerializable(EXTRA_DELETED_ITEM) != null) {
            mDeletedItem = (Item) savedInstanceState.getSerializable(EXTRA_DELETED_ITEM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_items, container, false);
        ButterKnife.bind(this, root);

        if (mLargeScreen) {
            int numColumns = getResources().getInteger(R.integer.card_item_columns);
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(numColumns,
                    StaggeredGridLayoutManager.VERTICAL));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(mDividerItemDecoration);
            mRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }
        mRecyclerView.setAdapter(mItemsAdapter);
        mRecyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_DELETED_ITEM, mDeletedItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        mItemsAdapter.clearItems();
        mPresenter.getItems(true);
        final boolean[] snackbarShown = {false};
        if (mDeletedItem != null) {
            Snackbar snackbar = mCallback.onShowSnackbar(getString(R.string.delete_item_success),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.delete_item_undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.restoreItem(mDeletedItem);
                    mDeletedItem = null;
                    Snackbar snackbarRestored = mCallback.onShowSnackbar(
                            getString(R.string.delete_item_undo_success), Snackbar.LENGTH_LONG);
                    snackbarRestored.show();
                }
            });
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    // There seems to be a bug where onDismissed is called before onShown.
                    if (snackbarShown[0]) {
                        mDeletedItem = null;
                    }
                }

                @Override
                public void onShown(Snackbar transientBottomBar) {
                    super.onShown(transientBottomBar);
                    snackbarShown[0] = true;
                }
            });
            snackbar.show();
        }
    }

    public void onItemDeleted(@NonNull Item item) {
        mDeletedItem = item;
        onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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

    interface Callback {

        void onAboutSelected();

        void onSignOutSelected();

        void onEditItemSelected();

        Snackbar onShowSnackbar(String text, int duration);
    }

    private interface ItemListener {

        void onItemClick(Item item);
    }

    class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

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
                    .inflate(mLargeScreen ? R.layout.card_item : R.layout.tile_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            mItems.keySet().toArray();
            if (mLargeScreen) {
                String imageUrl = mItems.get(mItemIds.get(position)).getImageUrl();
                if (imageUrl != null) {
                    if (!imageUrl.equals(mImageUrl)) {
                        mImageUrl = imageUrl;
                        holder.mItemImage.setVisibility(View.VISIBLE);
                        EspressoIdlingResource.increment();
                        Glide.with(getContext())
                                .load(imageUrl)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new GlideDrawableImageViewTarget(holder.mItemImage) {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource,
                                                                GlideAnimation<? super GlideDrawable> animation) {
                                        super.onResourceReady(resource, animation);
                                        EspressoIdlingResource.decrement();
                                    }
                                });
                    }
                } else {
                    holder.mItemImage.setVisibility(View.GONE);
                }
            }
            holder.mCategory.setText(mItems.get(mItemIds.get(position)).getCategory());
            holder.mType.setText(mItems.get(mItemIds.get(position)).getType());
            holder.mColour.setText(mItems.get(mItemIds.get(position)).getPrimaryColour());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mItems.get(mItemIds.get(position)).getExpiry());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault());
            String expiry;
            if (mLargeScreen) {
                expiry = simpleDateFormat.format(calendar.getTime());
            } else {
                expiry = "Expires: " + simpleDateFormat.format(calendar.getTime());
            }
            holder.mExpiry.setText(expiry);
            String priceString = getPriceFromTotalCents(mItems.get(mItemIds.get(position)).getPricePaid());
            String price = "Paid: $" + priceString;
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

        void clearItems() {
            mItems.clear();
            mItemIds.clear();
            mImageUrl = null;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView mItemImage;
            TextView mCategory;
            TextView mType;
            TextView mColour;
            TextView mExpiry;
            TextView mPaid;
            ImageView mExpire;

            ViewHolder(final View itemView) {
                super(itemView);
                mItemImage = (ImageView) itemView.findViewById(R.id.item_image);
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
