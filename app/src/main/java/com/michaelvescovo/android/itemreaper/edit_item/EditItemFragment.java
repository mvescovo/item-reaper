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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Michael Vescovo
 */

public class EditItemFragment extends Fragment implements EditItemContract.View {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.edit_purchase_date)
    TextView mPurchaseDate;
    @BindView(R.id.edit_price_paid)
    TextView mPricePaid;
    @BindView(R.id.edit_discount)
    TextView mDiscount;
    @BindView(R.id.edit_expiry)
    TextView mExpiry;
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

        long expiry = -1;
        if (mExpiry.getText().toString().contentEquals("")) {
            mExpiry.setError(getString(R.string.edit_expiry_error));
            mExpiry.requestFocus();
            itemOk = false;
        } else {
            expiry = Long.getLong(mExpiry.getText().toString());
        }

        long purchaseDate = -1;
        if (!mPurchaseDate.getText().toString().equals("")) {
            purchaseDate = Long.getLong(mPurchaseDate.getText().toString());
        }
        int pricePaid = -1;
        if (!mPricePaid.getText().toString().equals("")) {
            pricePaid = Integer.getInteger(mPricePaid.getText().toString());
        }
        int discount = -1;
        if (!mDiscount.getText().toString().equals("")) {
            discount = Integer.getInteger(mDiscount.getText().toString());
        }

        if (itemOk) {
            // Use "-1" as temporary ID. The real ID is set by the remote DataSource.
            Item newItem = new Item(
                    "-1",
                    purchaseDate,
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
