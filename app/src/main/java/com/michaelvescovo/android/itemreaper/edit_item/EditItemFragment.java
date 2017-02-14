package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

public class EditItemFragment extends Fragment implements EditItemContract.View {

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

    private OnFragmentInteractionListener mListener;
    private EditItemContract.Presenter mPresenter;

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
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        long expiry = -1;
        Calendar expiryDate = null;
        if (!mExpiryDay.getText().toString().contentEquals("")) {
            expiryDateDay = Integer.parseInt(mExpiryDay.getText().toString());
        } else {
            mExpiryDay.setError(getString(R.string.edit_date_day_error));
        }
        if (!mExpiryMonth.getText().toString().contentEquals("")) {
            expiryDateMonth = Integer.parseInt(mExpiryMonth.getText().toString());
        } else {
            mExpiryMonth.setError(getString(R.string.edit_date_month_error));
        }
        if (!mExpiryYear.getText().toString().contentEquals("")) {
            expiryDateYear = Integer.parseInt(mExpiryYear.getText().toString());
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
                    expiry,
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

    public interface OnFragmentInteractionListener {

    }
}
