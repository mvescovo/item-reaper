package com.michaelvescovo.android.itemreaper.items;


import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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
import android.util.TypedValue;
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
import com.google.common.collect.ImmutableList;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.util.Analytics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.michaelvescovo.android.itemreaper.R.id.expiry;
import static com.michaelvescovo.android.itemreaper.items.ItemsActivity.EXTRA_DELETED_ITEM;
import static com.michaelvescovo.android.itemreaper.util.MiscHelperMethods.getDateFormat;
import static com.michaelvescovo.android.itemreaper.util.MiscHelperMethods.getPriceFromTotalCents;

/**
 * @author Michael Vescovo
 */

public class ItemsFragment extends Fragment implements ItemsContract.View {

    private static String STATE_ITEM_QUERY = "item_query";

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
    private MediaPlayer mMediaPlayer;
    private FirebaseStorage mFirebaseStorage;
    private String mQuery;
    private boolean mSearching;
    private boolean mItemMoved;

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
                mPresenter.openItemDetails(item.getId());
            }
        };
        mItemsAdapter = new ItemsAdapter(new ArrayList<Item>(), itemListener);
        mDividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.divider));

        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable(EXTRA_DELETED_ITEM) != null) {
                mDeletedItem = (Item) savedInstanceState.getSerializable(EXTRA_DELETED_ITEM);
            }
            if (savedInstanceState.getString(STATE_ITEM_QUERY) != null) {
                mQuery = savedInstanceState.getString(STATE_ITEM_QUERY);
            } else {
                mQuery = null;
            }
        }
        mFirebaseStorage = FirebaseStorage.getInstance();
        mSearching = false;
        mItemMoved = false;
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
        outState.putString(STATE_ITEM_QUERY, mQuery);
    }

    @Override
    public void onResume() {
        super.onResume();
        mItemsAdapter.clearItems();
        mPresenter.getItems();
        if (mQuery != null) {
            searchItem(mQuery);
        }
        configureSnackbarForDeletedItem();
        Analytics.logEventViewItemList(getContext());
    }

    private void configureSnackbarForDeletedItem() {
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
                    Analytics.logEventUndoDeleteItem(getContext());
                }
            });
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    // There seems to be a bug where onDismissed is called before onShown.
                    if (snackbarShown[0]) {
                        if (mDeletedItem != null) {
                            removeImageFromFirebase();
                            mDeletedItem = null;
                        }
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

    private void removeImageFromFirebase() {
        if (mDeletedItem.getImageUrl() != null) {
            if (mDeletedItem.getImageUrl().contains("https://firebasestorage")) {
                StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(
                        mDeletedItem.getImageUrl());
                photoRef.delete();
            }
        }
    }

    public void onItemDeleted(@NonNull Item item) {
        mDeletedItem = item;
        onResume();
    }

    public void searchItem(@Nullable String query) {
        mQuery = query;
        mSearching = true;
        if (isResumed()) {
            mItemsAdapter.clearItems();
            mPresenter.getItems();
        }
        if (mQuery != null) {
            mItemsAdapter.searchItem();
        } else {
            mSearching = false;
        }
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
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
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
            case R.id.action_sort:
                mCallback.onSortSelected();
                break;
            case R.id.action_about:
                mPresenter.openAbout();
                break;
            case R.id.action_sign_out:
                mPresenter.openSignOut();
                Analytics.logEventLogout(getContext());
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
    public void addItem(Item item) {
        mItemsAdapter.addItem(item);
    }

    @Override
    public void changeItem(Item item) {
        mItemsAdapter.changeItem(item);
    }

    @Override
    public void removeItem(Item item) {
        mItemsAdapter.removeItem(item);
    }

    @Override
    public void moveItem() {
        mItemsAdapter.moveItem();
    }

    @Override
    public void showItemDetailsUi(String itemId) {
        mCallback.onItemDetailsSelected(itemId);
    }

    @Override
    public void showEditItemUi() {
        mCallback.onEditItemSelected(null);
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

    @Override
    public void showItemExpiredMessage(int resourceId, int duration, @Nullable final Item item) {
        Snackbar snackbar = mCallback.onShowSnackbar(getString(resourceId), duration);
        if (item != null) {
            snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.unexpireItem(item);
                    Analytics.logEventUndoExpireListItem(getContext());
                }
            });
        }
        snackbar.show();
    }

    private void playExpireItemSoundEffect() {
        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.decapitation);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mMediaPlayer.reset();
                return false;
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }

    interface Callback {

        void onSortSelected();

        void onAboutSelected();

        void onSignOutSelected();

        void onEditItemSelected(@Nullable String itemId);

        void onItemDetailsSelected(@NonNull String itemId);

        Snackbar onShowSnackbar(String text, int duration);
    }

    private interface ItemListener {

        void onItemClick(Item item);
    }

    class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

        private List<Item> mItems;
        private ItemListener mItemListener;

        ItemsAdapter(List<Item> items, ItemListener itemListener) {
            mItems = items;
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
            if (mLargeScreen) {
                String imageUrl = mItems.get(position).getImageUrl();
                if (imageUrl != null) {
                    if (!imageUrl.equals(mImageUrl)) {
                        mImageUrl = imageUrl;
                    }
                    holder.mItemImage.setVisibility(View.VISIBLE);
//                    EspressoIdlingResource.increment();
                    Glide.with(getContext())
                            .load(imageUrl)
                            .crossFade()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new GlideDrawableImageViewTarget(holder.mItemImage) {
                                @Override
                                public void onResourceReady(GlideDrawable resource,
                                                            GlideAnimation<? super GlideDrawable> animation) {
                                    super.onResourceReady(resource, animation);
//                                    EspressoIdlingResource.decrement();
                                }
                            });

                } else {
                    holder.mItemImage.setVisibility(View.GONE);
                }
            }
            String category = mItems.get(position).getCategory();
            if (category == null || category.equals("")) {
                holder.mCategory.setText(getString(R.string.edit_category_empty));
            } else {
                holder.mCategory.setText(category);
            }
            String type = mItems.get(position).getType();
            if (type == null || type.equals("")) {
                holder.mType.setText(getString(R.string.edit_type_empty));
            } else {
                holder.mType.setText(mItems.get(position).getType());
            }
            String colour = mItems.get(position).getMainColour();
            if (colour == null || colour.equals("")) {
                holder.mColour.setText(getString(R.string.edit_main_colour_empty));
            } else {
                holder.mColour.setText(mItems.get(position).getMainColour());
            }
            long expiry = mItems.get(position).getExpiry();
            if (expiry == -1) {
                holder.mExpiry.setText(getString(R.string.edit_expiry_date_empty));
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(expiry);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(
                        R.string.edit_date_format), Locale.ENGLISH);
                String expiryString = simpleDateFormat.format(calendar.getTime());
                holder.mExpiry.setText(expiryString);
                Calendar almostExpiredDate = Calendar.getInstance();
                almostExpiredDate.add(Calendar.MONTH, 1);
                if (calendar.compareTo(almostExpiredDate) < 1) {
                    holder.mExpiry.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                } else {
                    TypedValue typedValue = new TypedValue();
                    TypedArray typedArray = getContext().obtainStyledAttributes(
                            typedValue.data, new int[]{android.R.attr.textColorPrimary}
                    );
                    int textColourPrimary = typedArray.getColor(0, 0);
                    typedArray.recycle();
                    holder.mExpiry.setTextColor(textColourPrimary);
                }
            }
            int price = mItems.get(position).getPricePaid();
            String priceString;
            if (price == -1) {
                priceString = getString(R.string.edit_price_paid_empty);
            } else {
                priceString = getString(R.string.edit_price_paid_prefix)
                        + getPriceFromTotalCents(price);
            }
            holder.mPaid.setText(priceString);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        void addItem(Item item) {
            if (mItems.add(item)) {
                int itemIndex = mItems.indexOf(item);
                if (!mSearching) {
                    notifyItemInserted(itemIndex);
                }
                mPresenter.itemsSizeChanged(mItems.size());
            }
        }

        void changeItem(Item item) {
            int itemIndex = mItems.indexOf(item);
            if (itemIndex != -1) {
                mItems.set(itemIndex, item);
                if (mItemMoved) {
                    sortItemsByExpiry();
                    notifyDataSetChanged();
                } else {
                    notifyItemChanged(itemIndex);
                }
            }
        }

        void removeItem(Item item) {
            int itemIndex = mItems.indexOf(item);
            if (itemIndex != -1) {
                mItems.remove(item);
                notifyItemRemoved(itemIndex);
                mPresenter.itemsSizeChanged(mItems.size());
            }
        }

        void moveItem() {
            mItemMoved = true;
        }

        private void sortItemsByExpiry() {
            // Sort ascending (earlier dates first)
            Collections.sort(mItems, new Comparator<Item>() {
                @Override
                public int compare(Item item1, Item item2) {
                    return item1.compareTo(item2);
                }
            });
        }

        private void searchItem() {
            setProgressBar(true);
            new SearchForItemTask().execute();
        }

        private void searchComplete(List<Item> matchedItems) {
            clearItems();
            mItems.addAll(matchedItems);
            notifyDataSetChanged();
            mPresenter.itemsSizeChanged(mItems.size());
            mSearching = false;
            setProgressBar(false);
            Analytics.logEventViewSearchResults(getContext(), mQuery);
        }

        private void clearItems() {
            mItems.clear();
            if (!mSearching) {
                notifyDataSetChanged();
            }
            mImageUrl = null;
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
                        playExpireItemSoundEffect();
                        Item item = mItems.get(getAdapterPosition());
                        mPresenter.expireItem(item);
                        Analytics.logEventExpireListItem(getContext(), item);
                    }
                });
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Item item = mItems.get(getAdapterPosition());
                mItemListener.onItemClick(item);
            }
        }
    }

    private class SearchForItemTask extends AsyncTask<Void, Void, List<Item>> {
        protected List<Item> doInBackground(Void... params) {
            List<Item> matchedItems = new ArrayList<>();
            if (!mQuery.equals("")) {
                List<Item> items = ImmutableList.copyOf(mItemsAdapter.mItems);
                for (Item item : items) {
                    String format = getString(R.string.date_format);
                    if (item.getPurchaseDate() != -1) {
                        String purchaseDate = getDateFormat(format).format(item.getPurchaseDate())
                                .toLowerCase();
                        if (purchaseDate.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getShop() != null) {
                        String shop = item.getShop().toLowerCase();
                        if (shop.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getPricePaid() != -1) {
                        String pricePaid = "$" + getPriceFromTotalCents(item.getPricePaid())
                                .toLowerCase();
                        if (pricePaid.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getDiscount() != -1) {
                        String discount = "$" + getPriceFromTotalCents(item.getDiscount())
                                .toLowerCase();
                        if (discount.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getExpiry() != -1) {
                        String expiry = getDateFormat(format).format(item.getExpiry())
                                .toLowerCase();
                        if (expiry.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getCategory() != null) {
                        String category = item.getCategory().toLowerCase();
                        if (category.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getSubCategory() != null) {
                        String subCategory = item.getSubCategory().toLowerCase();
                        if (subCategory.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getType() != null) {
                        String type = item.getType().toLowerCase();
                        if (type.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getSubType() != null) {
                        String subType = item.getSubType().toLowerCase();
                        if (subType.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getSubType2() != null) {
                        String subType2 = item.getSubType2().toLowerCase();
                        if (subType2.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getSubType3() != null) {
                        String subType3 = item.getSubType3().toLowerCase();
                        if (subType3.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getMainColour() != null) {
                        String mainColour = item.getMainColour().toLowerCase();
                        if (mainColour.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getMainColourShade() != null) {
                        String mainColourShade = item.getMainColourShade().toLowerCase();
                        if (mainColourShade.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getAccentColour() != null) {
                        String accentColour = item.getAccentColour().toLowerCase();
                        if (accentColour.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getSize() != null) {
                        String size = item.getSize().toLowerCase();
                        if (size.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getBrand() != null) {
                        String brand = item.getBrand().toLowerCase();
                        if (brand.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getDescription() != null) {
                        String description = item.getDescription().toLowerCase();
                        if (description.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                            continue;
                        }
                    }
                    if (item.getNote() != null) {
                        String note = item.getNote().toLowerCase();
                        if (note.contains(mQuery.toLowerCase())) {
                            matchedItems.add(item);
                        }
                    }
                }
            }
            return matchedItems;
        }

        protected void onPostExecute(List<Item> matchedItems) {
            mItemsAdapter.searchComplete(matchedItems);
        }
    }
}
