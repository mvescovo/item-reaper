package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private EditItemContract.Presenter mPresenter;
    private Callback mCallback;
    private Typeface mAppbarTypeface;

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
        View root;
        boolean isLargeScreen = getResources().getBoolean(R.bool.large_layout);
        root = isLargeScreen
                ? inflater.inflate(R.layout.dialog_fragment_edit_item, container, false)
                : inflater.inflate(R.layout.fragment_edit_item, container, false);
        ButterKnife.bind(this, root);
        mCallback.configureSupportActionBar(mToolbar);
        if (isLargeScreen) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        mAppbarTitle.setTypeface(mAppbarTypeface);
        setHasOptionsMenu(true);
        return root;
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
            case R.id.action_save:
                validateItem();
                break;
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
    public void showItemsUi() {
        getActivity().finish();
    }

    public interface Callback {
        void configureSupportActionBar(Toolbar toolbar);
    }
}
