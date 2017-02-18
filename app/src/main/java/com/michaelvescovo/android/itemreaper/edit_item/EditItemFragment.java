package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Michael Vescovo
 */

public class EditItemFragment extends AppCompatDialogFragment implements EditItemContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.edit_purchase_date_day)
    TextView mPurchaseDateDay;
    @BindView(R.id.edit_purchase_date_month)
    TextView mPurchaseDateMonth;
    @BindView(R.id.edit_purchase_date_year)
    TextView mPurchaseDateYear;
    @BindView(R.id.edit_price_paid)
    TextView mPricePaid;
    @BindView(R.id.edit_discount)
    TextView mDiscount;
    @BindView(R.id.edit_expiry_date_day)
    TextView mExpiryDay;
    @BindView(R.id.edit_expiry_date_month)
    TextView mExpiryMonth;
    @BindView(R.id.edit_expiry_date_year)
    TextView mExpiryYear;
    @BindView(R.id.edit_category)
    TextView mCategory;
    @BindView(R.id.edit_sub_category)
    TextView mSubCategory;
    @BindView(R.id.edit_type)
    TextView mType;
    @BindView(R.id.edit_sub_type)
    TextView mSubType;
    @BindView(R.id.edit_sub_type2)
    TextView mSubType2;
    @BindView(R.id.edit_sub_type3)
    TextView mSubType3;
    @BindView(R.id.edit_primary_colour)
    TextView mPrimaryColour;
    @BindView(R.id.edit_primary_colour_shade)
    TextView mPrimaryColourShade;
    @BindView(R.id.edit_secondary_colour)
    TextView mSecondaryColour;
    @BindView(R.id.edit_size)
    TextView mSize;
    @BindView(R.id.edit_brand)
    TextView mBrand;
    @BindView(R.id.edit_shop)
    TextView mShop;
    @BindView(R.id.edit_description)
    TextView mDescription;
    @BindView(R.id.edit_note)
    TextView mNote;
    @BindView(R.id.edit_item_image)
    ImageView mItemImage;

    private EditItemContract.Presenter mPresenter;
    private Callback mCallback;
    private boolean mIsLargeScreen;
    private Typeface mAppbarTypeface;
    private String mItemId;

    public EditItemFragment() {
    }

    public static EditItemFragment newInstance() {
        return new EditItemFragment();
    }

    @Override
    public void setPresenter(@NonNull EditItemContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_item, container, false);
        ButterKnife.bind(this, root);
        mCallback.configureSupportActionBar(mToolbar, ContextCompat.getDrawable(getContext(),
                R.drawable.ic_done_white_24dp));
        mAppbarTitle.setTypeface(mAppbarTypeface);

        mIsLargeScreen = getResources().getBoolean(R.bool.large_layout);
        if (mIsLargeScreen) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        setHasOptionsMenu(true);

        if (getArguments() != null
                && getArguments().getString(EditItemActivity.EXTRA_ITEM_ID) != null) {
            mItemId = getArguments().getString(EditItemActivity.EXTRA_ITEM_ID);
        }

        /* TEMP */
//        mItemId = "1";

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.editItem(mItemId);
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
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_save:
//                validateItem();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validateItem() {
        boolean itemOk = true;
        if (mCategory.getText().toString().contentEquals("")) {
            mCategory.setError(getString(R.string.edit_category_error));
            mCategory.requestFocus();
            itemOk = false;
        }

        if (mType.getText().toString().contentEquals("")) {
            mType.setError(getString(R.string.edit_type_error));
            mType.requestFocus();
            itemOk = false;
        }

        int expiryDateDay = -1;
        int expiryDateMonth = -1;
        int expiryDateYear = -1;
        Calendar expiryDate = null;
        if (!mExpiryDay.getText().toString().contentEquals("")) {
            try {
                expiryDateDay = Integer.parseInt(mExpiryDay.getText().toString());
            } catch (NumberFormatException e) {
                itemOk = false;
                mExpiryDay.setError(getString(R.string.edit_date_day_error_invalid));
            }
        } else {
            mExpiryDay.setError(getString(R.string.edit_date_day_error));
        }
        if (!mExpiryMonth.getText().toString().contentEquals("")) {
            try {
                expiryDateMonth = Integer.parseInt(mExpiryMonth.getText().toString());
                expiryDateMonth--; // Java month starts at 0
            } catch (NumberFormatException e) {
                itemOk = false;
                mExpiryMonth.setError(getString(R.string.edit_date_month_error_invalid));
            }
        } else {
            mExpiryMonth.setError(getString(R.string.edit_date_month_error));
        }
        if (!mExpiryYear.getText().toString().contentEquals("")) {
            try {
                expiryDateYear = Integer.parseInt(mExpiryYear.getText().toString());
            } catch (NumberFormatException e) {
                itemOk = false;
                mExpiryYear.setError(getString(R.string.edit_date_year_error_invalid));
            }
        } else {
            mExpiryYear.setError(getString(R.string.edit_date_year_error));
        }
        if (expiryDateDay != -1 && expiryDateMonth != -1 && expiryDateYear != -1) {
            expiryDate = Calendar.getInstance();
            expiryDate.set(expiryDateYear, expiryDateMonth, expiryDateDay);
        }
        if (expiryDate == null) {
            itemOk = false;
        }

        int purchaseDateDay = -1;
        int purchaseDateMonth = -1;
        int purchaseDateYear = -1;
        Calendar purchaseDate = null;
        if (!mPurchaseDateDay.getText().toString().equals("")) {
            purchaseDateDay = Integer.parseInt(mPurchaseDateDay.getText().toString());
        }
        if (!mPurchaseDateMonth.getText().toString().equals("")) {
            purchaseDateMonth = Integer.parseInt(mPurchaseDateMonth.getText().toString());
            purchaseDateMonth--; // Java month starts at 0
        }
        if (!mPurchaseDateYear.getText().toString().equals("")) {
            purchaseDateYear = Integer.parseInt(mPurchaseDateYear.getText().toString());
        }
        if (purchaseDateDay != -1 && purchaseDateMonth != -1 && purchaseDateYear != -1) {
            purchaseDate = Calendar.getInstance();
            purchaseDate.set(purchaseDateYear, purchaseDateMonth, purchaseDateDay);
        }

        int pricePaid = -1;
        if (!mPricePaid.getText().toString().equals("")) {
            pricePaid = Integer.parseInt(mPricePaid.getText().toString());
        }
        int discount = -1;
        if (!mDiscount.getText().toString().equals("")) {
            discount = Integer.parseInt(mDiscount.getText().toString());
        }

        if (itemOk) {
            // Use "-1" as temporary ID. The real ID is set by the remote DataSource.
            Item newItem = new Item(
                    "-1",
                    purchaseDate == null ? -1 : purchaseDate.getTimeInMillis(),
                    pricePaid,
                    discount,
                    expiryDate.getTimeInMillis(),
                    mCategory.getText().toString(),
                    mSubCategory.getText().toString(),
                    mType.getText().toString(),
                    mSubType.getText().toString(),
                    mSubType2.getText().toString(),
                    mSubType3.getText().toString(),
                    mPrimaryColour.getText().toString(),
                    mPrimaryColourShade.getText().toString(),
                    mSecondaryColour.getText().toString(),
                    mSize.getText().toString(),
                    mBrand.getText().toString(),
                    mShop.getText().toString(),
                    mDescription.getText().toString(),
                    mNote.getText().toString(),
                    null,
                    false
            );
            mPresenter.saveItem(newItem);
        }
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
    public void showExistingItem(Item item) {
        if (item.getCategory() != null
                && !item.getCategory().equals(mCategory.getText().toString())) {
            mCategory.setText(item.getCategory());
        }
        if (item.getType() != null
                && !item.getType().equals(mType.getText().toString())) {
            mType.setText(item.getType());
        }

        if (item.getExpiry() != -1) {
            Calendar expiry = Calendar.getInstance();
            expiry.setTimeInMillis(item.getExpiry());
            int day = expiry.get(Calendar.DAY_OF_MONTH);
            int month = expiry.get(Calendar.MONTH);
            month++; // Months start at 0.
            int year = expiry.get(Calendar.YEAR);
            mExpiryDay.setText(String.valueOf(day));
            mExpiryMonth.setText(String.valueOf(month));
            mExpiryYear.setText(String.valueOf(year));
        }

        if (item.getPrimaryColour() != null
                && !item.getPrimaryColour().equals(mPrimaryColour.getText().toString())) {
            mPrimaryColour.setText(item.getPrimaryColour());
        }

        if (item.getPurchaseDate() != -1) {
            Calendar purchaseDate = Calendar.getInstance();
            purchaseDate.setTimeInMillis(item.getPurchaseDate());
            int day = purchaseDate.get(Calendar.DAY_OF_MONTH);
            int month = purchaseDate.get(Calendar.MONTH);
            month++; // Months start at 0.
            int year = purchaseDate.get(Calendar.YEAR);
            mPurchaseDateDay.setText(String.valueOf(day));
            mPurchaseDateMonth.setText(String.valueOf(month));
            mPurchaseDateYear.setText(String.valueOf(year));
        }

        if (item.getPricePaid() != -1
                && !String.valueOf(item.getPricePaid()).equals(mPricePaid.getText().toString())) {
            mPricePaid.setText(String.valueOf(item.getPricePaid()));
        }
        if (item.getDiscount() != -1
                && !String.valueOf(item.getDiscount()).equals(mDiscount.getText().toString())) {
            mDiscount.setText(String.valueOf(item.getDiscount()));
        }
        if (item.getSubCategory() != null
                && !item.getSubCategory().equals(mSubCategory.getText().toString())) {
            mSubCategory.setText(item.getSubCategory());
        }
        if (item.getSubtype() != null
                && !item.getSubtype().equals(mSubType.getText().toString())) {
            mSubType.setText(item.getSubtype());
        }
        if (item.getSubtype2() != null
                && !item.getSubtype2().equals(mSubType2.getText().toString())) {
            mSubType2.setText(item.getSubtype2());
        }
        if (item.getSubtype3() != null
                && !item.getSubtype3().equals(mSubType3.getText().toString())) {
            mSubType3.setText(item.getSubtype3());
        }
        if (item.getPrimaryColourShade() != null
                && !item.getPrimaryColourShade().equals(mPrimaryColourShade.getText().toString())) {
            mPrimaryColourShade.setText(item.getPrimaryColourShade());
        }
        if (item.getSecondaryColour() != null
                && !item.getSecondaryColour().equals(mSecondaryColour.getText().toString())) {
            mSecondaryColour.setText(item.getSecondaryColour());
        }
        if (item.getSize() != null
                && !item.getSize().equals(mSize.getText().toString())) {
            mSize.setText(item.getSize());
        }
        if (item.getBrand() != null
                && !item.getBrand().equals(mBrand.getText().toString())) {
            mBrand.setText(item.getBrand());
        }
        if (item.getShop() != null
                && !item.getShop().equals(mShop.getText().toString())) {
            mShop.setText(item.getShop());
        }
        if (item.getDescription() != null
                && !item.getDescription().equals(mDescription.getText().toString())) {
            mDescription.setText(item.getDescription());
        }
        if (item.getNote() != null
                && !item.getNote().equals(mNote.getText().toString())) {
            mNote.setText(item.getNote());
        }
        if (item.getImageUrl() != null) {
            mItemImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(item.getImageUrl())
                    .crossFade()
                    .into(mItemImage);
        }
    }

    @Override
    public void showItemsUi() {
        if (mIsLargeScreen) {
            dismiss();
            mCallback.refresh();
        } else {
            getActivity().finish();
        }
    }

    public interface Callback {

        void configureSupportActionBar(Toolbar toolbar, Drawable icon);

        void refresh();
    }
}
