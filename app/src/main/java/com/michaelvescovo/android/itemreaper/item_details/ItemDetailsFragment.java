package com.michaelvescovo.android.itemreaper.item_details;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID;
import static com.michaelvescovo.android.itemreaper.util.MiscHelperMethods.getDateFormat;
import static com.michaelvescovo.android.itemreaper.util.MiscHelperMethods.getPriceFromTotalCents;

/**
 * @author Michael Vescovo
 */

public class ItemDetailsFragment extends AppCompatDialogFragment implements ItemDetailsContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.purchase_date_value)
    TextView mPurchaseDate;
    @BindView(R.id.shop_value)
    TextView mShop;
    @BindView(R.id.price_paid_value)
    TextView mPricePaid;
    @BindView(R.id.discount_value)
    TextView mDiscount;
    @BindView(R.id.expiry_date_value)
    TextView mExpiryDate;
    @BindView(R.id.category_value)
    TextView mCategory;
    @BindView(R.id.sub_category_value)
    TextView mSubCategory;
    @BindView(R.id.type_value)
    TextView mType;
    @BindView(R.id.sub_type_value)
    TextView mSubType;
    @BindView(R.id.sub_type2_value)
    TextView mSubType2;
    @BindView(R.id.sub_type3_value)
    TextView mSubType3;
    @BindView(R.id.primary_colour_value)
    TextView mPrimaryColour;
    @BindView(R.id.primary_colour_shade_value)
    TextView mPrimaryColourShade;
    @BindView(R.id.secondary_colour_value)
    TextView mSecondaryColour;
    @BindView(R.id.size_value)
    TextView mSize;
    @BindView(R.id.brand_value)
    TextView mBrand;
    @BindView(R.id.description_value)
    TextView mDescription;
    @BindView(R.id.note_value)
    TextView mNote;
    @BindView(R.id.item_image)
    ImageView mItemImage;

    private ItemDetailsContract.Presenter mPresenter;
    private Callback mCallback;
    private boolean mLargeScreen;
    private Typeface mAppbarTypeface;
    private String mItemId;
    private Item mItem;
    private MediaPlayer mMediaPlayer;
    private MenuItem mExpireMenuItem;

    public ItemDetailsFragment() {
    }

    public static ItemDetailsFragment newInstance() {
        return new ItemDetailsFragment();
    }

    @Override
    public void setPresenter(@NonNull ItemDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_details, container, false);
        ButterKnife.bind(this, root);
        mCallback.configureSupportActionBar(mToolbar, ContextCompat.getDrawable(getContext(),
                R.drawable.ic_close_white_24dp));
        mAppbarTitle.setTypeface(mAppbarTypeface);

        mLargeScreen = getResources().getBoolean(R.bool.large_layout);
        if (mLargeScreen) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            if (getArguments().getString(EXTRA_ITEM_ID) != null) {
                mItemId = getArguments().getString(EXTRA_ITEM_ID);
            }
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.displayItem(mItemId);
    }

    @Override
    public void showItem(@NonNull Item item) {
        // If the fragment is not active, don't allow callbacks to crash the app.
        if (getActivity() != null) {
            mItem = item;
            String format = getString(R.string.date_format);
            if (item.getPurchaseDate() != -1) {
                mPurchaseDate.setText(getDateFormat(format).format(item.getPurchaseDate()));
            } else {
                mPurchaseDate.setText(getString(R.string.empty_value));
            }
            if (item.getShop() != null) {
                mShop.setText(item.getShop());
            } else {
                mShop.setText(getString(R.string.empty_value));
            }
            if (item.getPricePaid() != -1) {
                String pricePaid = "$" + getPriceFromTotalCents(item.getPricePaid());
                mPricePaid.setText(pricePaid);
            } else {
                mPricePaid.setText(getString(R.string.empty_value));
            }
            if (item.getDiscount() != -1) {
                String discount = "$" + getPriceFromTotalCents(item.getDiscount());
                mDiscount.setText(discount);
            } else {
                mDiscount.setText(getString(R.string.empty_value));
            }
            if (item.getExpiry() != -1) {
                mExpiryDate.setText(getDateFormat(format).format(item.getExpiry()));
            } else {
                mExpiryDate.setText(getString(R.string.empty_value));
            }
            if (item.getCategory() != null) {
                mCategory.setText(item.getCategory());
            } else {
                mCategory.setText(getString(R.string.empty_value));
            }
            if (item.getSubCategory() != null) {
                mSubCategory.setText(item.getSubCategory());
            } else {
                mSubCategory.setText(getString(R.string.empty_value));
            }
            if (item.getType() != null) {
                mType.setText(item.getType());
            } else {
                mType.setText(getString(R.string.empty_value));
            }
            if (item.getSubtype() != null) {
                mSubType.setText(item.getSubtype());
            } else {
                mSubType.setText(getString(R.string.empty_value));
            }
            if (item.getSubtype2() != null) {
                mSubType2.setText(item.getSubtype2());
            } else {
                mSubType2.setText(getString(R.string.empty_value));
            }
            if (item.getSubtype3() != null) {
                mSubType3.setText(item.getSubtype3());
            } else {
                mSubType3.setText(getString(R.string.empty_value));
            }
            if (item.getPrimaryColour() != null) {
                mPrimaryColour.setText(item.getPrimaryColour());
            } else {
                mPrimaryColour.setText(getString(R.string.empty_value));
            }
            if (item.getPrimaryColourShade() != null) {
                mPrimaryColourShade.setText(item.getPrimaryColourShade());
            } else {
                mPrimaryColourShade.setText(getString(R.string.empty_value));
            }
            if (item.getSecondaryColour() != null) {
                mSecondaryColour.setText(item.getSecondaryColour());
            } else {
                mSecondaryColour.setText(getString(R.string.empty_value));
            }
            if (item.getSize() != null) {
                mSize.setText(item.getSize());
            } else {
                mSize.setText(getString(R.string.empty_value));
            }
            if (item.getBrand() != null) {
                mBrand.setText(item.getBrand());
            } else {
                mBrand.setText(getString(R.string.empty_value));
            }
            if (item.getDescription() != null) {
                mDescription.setText(item.getDescription());
            } else {
                mDescription.setText(getString(R.string.empty_value));
            }
            if (item.getNote() != null) {
                mNote.setText(item.getNote());
            } else {
                mNote.setText(getString(R.string.empty_value));
            }
            if (item.getImageUrl() != null) {
                mItemImage.setVisibility(View.VISIBLE);
                EspressoIdlingResource.increment();
                Glide.with(getContext())
                        .load(item.getImageUrl())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new GlideDrawableImageViewTarget(mItemImage) {
                            @Override
                            public void onResourceReady(GlideDrawable resource,
                                                        GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                EspressoIdlingResource.decrement();
                            }
                        });
            } else {
                mItemImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showExpireMenuButton(boolean visible) {
        if (mExpireMenuItem != null) {
            mExpireMenuItem.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_expire_item:
                mExpireMenuItem = item;
                playExpireItemSoundEffect();
                mPresenter.expireItem(mItem);
                break;
            case R.id.action_edit_item:
                mPresenter.openEditItem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showEditItemUi() {
        mCallback.onEditItemSelected(mItemId);
    }

    @Override
    public void showItemExpiredMessage(int resourceId, int duration, @Nullable final Item item) {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, resourceId, duration);
        if (item != null) {
            snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.unexpireItem(item);
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

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
        mAppbarTypeface = Typeface.createFromAsset(getActivity().getAssets(),
                "Nosifer-Regular.ttf");
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

    public interface Callback {

        void configureSupportActionBar(Toolbar toolbar, Drawable icon);

        void onEditItemSelected(@Nullable String itemId);
    }
}