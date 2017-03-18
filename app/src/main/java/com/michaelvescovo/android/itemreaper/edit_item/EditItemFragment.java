package com.michaelvescovo.android.itemreaper.edit_item;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * @author Michael Vescovo
 */

public class EditItemFragment extends AppCompatDialogFragment implements EditItemContract.View,
        TextWatcher {

    public static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE_PHOTO_PICKER = 2;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.purchase_date_spinner)
    Spinner mPurchaseDateSpinner;
    @BindView(R.id.edit_price_paid)
    TextView mPricePaid;
    @BindView(R.id.edit_discount)
    TextView mDiscount;
    @BindView(R.id.expiry_date_spinner)
    Spinner mExpiryDateSpinner;
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
    @BindView(R.id.edit_item_remove_image_button)
    Button mRemoveImageButton;

    private EditItemContract.Presenter mPresenter;
    private Callback mCallback;
    private boolean mIsLargeScreen;
    private Typeface mAppbarTypeface;
    private String mItemId;
    private String mImageUrl;
    private String mSelectedPurchaseDate;
    private int mPreviousPurchaseDateOption;
    private Calendar mPurchaseDate;
    private ArrayAdapter<String> mPurchaseDateAdapter;
    private String mSelectedExpiryDate;
    private int mPreviousExpiryDateOption;
    private Calendar mExpiryDate;
    private ArrayAdapter<String> mExpiryDateAdapter;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.doneEditing();
            }
        });

        setHasOptionsMenu(true);

        if (getArguments() != null
                && getArguments().getString(EditItemActivity.EXTRA_ITEM_ID) != null) {
            mItemId = getArguments().getString(EditItemActivity.EXTRA_ITEM_ID);
        }

        configureViews();

        /* TEMP */
//        mItemId = "1";

        return root;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    private void configureViews() {
        String[] dateOptions = getResources().getStringArray(R.array.date_array);

        // Purchase date
        mPurchaseDateAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(dateOptions)));
        mPurchaseDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPurchaseDateSpinner.setAdapter(mPurchaseDateAdapter);
        mPurchaseDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String selectedOption = (String) adapterView.getItemAtPosition(pos);
                onPurchaseDateOptionSelected(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mPurchaseDate = Calendar.getInstance();

        mShop.addTextChangedListener(this);

        final Pattern pattern = Pattern.compile("(0|[1-9]+[0-9]*)?(\\.[0-9]{0,2})?");
        InputFilter[] currencyFilters = new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String result = dest.subSequence(0, dstart) + source.toString()
                                + dest.subSequence(dend, dest.length());
                        Matcher matcher = pattern.matcher(result);
                        return matcher.matches() ? null : dest.subSequence(dstart, dend);
                    }
                }
        };
        mPricePaid.setFilters(currencyFilters);
        mPricePaid.addTextChangedListener(this);
        mDiscount.setFilters(currencyFilters);
        mDiscount.addTextChangedListener(this);

        // Expiry date
        mExpiryDateAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(dateOptions)));
        mExpiryDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExpiryDateSpinner.setAdapter(mExpiryDateAdapter);
        mExpiryDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String selectedOption = (String) adapterView.getItemAtPosition(pos);
                onExpiryDateOptionSelected(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mExpiryDate = Calendar.getInstance();
        mExpiryDate.add(Calendar.YEAR, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                getString(R.string.edit_date_format), Locale.getDefault());
        mSelectedExpiryDate = simpleDateFormat.format(mExpiryDate.getTime());
        mExpiryDateAdapter.insert(mSelectedExpiryDate, 0);
        mExpiryDateSpinner.setSelection(0);

        mCategory.addTextChangedListener(this);
        mSubCategory.addTextChangedListener(this);
        mType.addTextChangedListener(this);
        mSubType.addTextChangedListener(this);
        mSubType2.addTextChangedListener(this);
        mSubType3.addTextChangedListener(this);
        mPrimaryColour.addTextChangedListener(this);
        mPrimaryColourShade.addTextChangedListener(this);
        mSecondaryColour.addTextChangedListener(this);
        mSize.addTextChangedListener(this);
        mBrand.addTextChangedListener(this);
        mDescription.addTextChangedListener(this);
        mNote.addTextChangedListener(this);
        mRemoveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.deleteImage();
            }
        });
    }

    private void onPurchaseDateOptionSelected(String selectedOption) {
        if (selectedOption.equals(getString(R.string.edit_date_custom))) {
            final Calendar today = Calendar.getInstance();
            final Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DAY_OF_YEAR, -1);
            final Calendar calendar = Calendar.getInstance();
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentYear = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            if (mSelectedPurchaseDate != null) {
                                mPurchaseDateAdapter.remove(mSelectedPurchaseDate);
                                mSelectedPurchaseDate = null;
                            }
                            calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                            calendar.set(Calendar.MONTH, datePicker.getMonth());
                            calendar.set(Calendar.YEAR, datePicker.getYear());
                            mPurchaseDate = calendar;
                            mPresenter.itemChanged();
                            if (calendar.compareTo(today) == 0) {
                                mPurchaseDateSpinner.setSelection(
                                        mPurchaseDateAdapter.getPosition(
                                                getString(R.string.edit_date_today)));
                            } else if (calendar.compareTo(yesterday) == 0) {
                                mPurchaseDateSpinner.setSelection(
                                        mPurchaseDateAdapter.getPosition(
                                                getString(R.string.edit_date_yesterday)));
                            } else {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                                        getString(R.string.edit_date_format), Locale.getDefault());
                                mSelectedPurchaseDate = simpleDateFormat.format(calendar.getTime());
                                mPurchaseDateAdapter.insert(mSelectedPurchaseDate, 0);
                                mPurchaseDateSpinner.setSelection(0);
                            }
                        }
                    }, currentYear, currentMonth, currentDay);
            datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mPurchaseDateSpinner.setSelection(mPreviousPurchaseDateOption);
                }
            });
            datePickerDialog.show();
        } else {
            if (selectedOption.equals(getString(R.string.edit_date_today))
                    || selectedOption.equals(getString(R.string.edit_date_yesterday))
                    || selectedOption.equals(getString(R.string.edit_date_unknown))) {
                if (mSelectedPurchaseDate != null) {
                    mPurchaseDateAdapter.remove(mSelectedPurchaseDate);
                    mSelectedPurchaseDate = null;
                }
                mPurchaseDateSpinner.setSelection(mPurchaseDateAdapter.getPosition(selectedOption));
            }
            if (selectedOption.equals(getString(R.string.edit_date_today))) {
                mPurchaseDate = Calendar.getInstance();
            } else if (selectedOption.equals(getString(R.string.edit_date_yesterday))) {
                mPurchaseDate = Calendar.getInstance();
                mPurchaseDate.add(Calendar.DAY_OF_YEAR, -1);
            } else if (selectedOption.equals(getString(R.string.edit_date_unknown))) {
                mPurchaseDate = null;
            }
            mPresenter.itemChanged();
            mPreviousPurchaseDateOption = mPurchaseDateAdapter.getPosition(selectedOption);
        }
    }

    private void onExpiryDateOptionSelected(String selectedOption) {
        if (selectedOption.equals(getString(R.string.edit_date_custom))) {
            final Calendar today = Calendar.getInstance();
            final Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DAY_OF_YEAR, -1);
            final Calendar calendar = Calendar.getInstance();
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentYear = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            if (mSelectedExpiryDate != null) {
                                mExpiryDateAdapter.remove(mSelectedExpiryDate);
                                mSelectedExpiryDate = null;
                            }
                            calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                            calendar.set(Calendar.MONTH, datePicker.getMonth());
                            calendar.set(Calendar.YEAR, datePicker.getYear());
                            mExpiryDate = calendar;
                            mPresenter.itemChanged();
                            if (calendar.compareTo(today) == 0) {
                                mExpiryDateSpinner.setSelection(
                                        mExpiryDateAdapter.getPosition(
                                                getString(R.string.edit_date_today)));
                            } else if (calendar.compareTo(yesterday) == 0) {
                                mExpiryDateSpinner.setSelection(
                                        mExpiryDateAdapter.getPosition(
                                                getString(R.string.edit_date_yesterday)));
                            } else {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                                        getString(R.string.edit_date_format), Locale.getDefault());
                                mSelectedExpiryDate = simpleDateFormat.format(calendar.getTime());
                                mExpiryDateAdapter.insert(mSelectedExpiryDate, 0);
                                mExpiryDateSpinner.setSelection(0);
                            }
                        }
                    }, currentYear, currentMonth, currentDay);
            datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mExpiryDateSpinner.setSelection(mPreviousExpiryDateOption);
                }
            });
            datePickerDialog.show();
        } else {
            if (selectedOption.equals(getString(R.string.edit_date_today))
                    || selectedOption.equals(getString(R.string.edit_date_yesterday))
                    || selectedOption.equals(getString(R.string.edit_date_unknown))) {
                if (mSelectedExpiryDate != null) {
                    mExpiryDateAdapter.remove(mSelectedExpiryDate);
                    mSelectedExpiryDate = null;
                }
                mExpiryDateSpinner.setSelection(mExpiryDateAdapter.getPosition(selectedOption));
            }
            if (selectedOption.equals(getString(R.string.edit_date_today))) {
                mExpiryDate = Calendar.getInstance();
            } else if (selectedOption.equals(getString(R.string.edit_date_yesterday))) {
                mExpiryDate = Calendar.getInstance();
                mExpiryDate.add(Calendar.DAY_OF_YEAR, -1);
            } else if (selectedOption.equals(getString(R.string.edit_date_unknown))) {
                mExpiryDate = null;
            }
            mPresenter.itemChanged();
            mPreviousExpiryDateOption = mExpiryDateAdapter.getPosition(selectedOption);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.editItem(mItemId);
        mCallback.onDialogResumed();
        mPresenter.itemChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
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
            case R.id.action_take_photo:
                mPresenter.takePicture(getContext());
                break;
            case R.id.action_select_image:
                mPresenter.selectImage(getContext());
                break;
            case R.id.action_delete_item:
                mPresenter.deleteItem(createCurrentItem());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void validateItem() {
        mPresenter.saveItem(createCurrentItem());
    }

    private Item createCurrentItem() {
        int pricePaid = -1;
        if (!mPricePaid.getText().toString().equals("")
                && !mPricePaid.getText().toString().equals(".")) {
            String priceString = mPricePaid.getText().toString();
            pricePaid = getTotalCents(priceString);
        }
        int discount = -1;
        if (!mDiscount.getText().toString().equals("")
                && !mDiscount.getText().toString().equals(".")) {
            String discountString = mDiscount.getText().toString();
            discount = getTotalCents(discountString);
        }

        return new Item(
                mItemId,
                mPurchaseDate == null ? -1 : mPurchaseDate.getTimeInMillis(),
                pricePaid,
                discount,
                mExpiryDate == null ? -1 : mExpiryDate.getTimeInMillis(),
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
                mImageUrl,
                false
        );
    }

    private int getTotalCents(String priceString) {
        String dollarsString = priceString;
        String centsString = "0";
        if (priceString.contains(".")) {
            int decimalIndex = priceString.indexOf(".");
            if (decimalIndex == 0) {
                dollarsString = "0";
                centsString = priceString.substring(decimalIndex + 1);
            } else {
                dollarsString = priceString.substring(0, decimalIndex);
                if (priceString.length() > decimalIndex + 1) {
                    centsString = priceString.substring(decimalIndex + 1);
                }
            }
        }
        int dollars = Integer.valueOf(dollarsString);
        int cents = Integer.valueOf(centsString);
        if (centsString.length() < 2 && Integer.valueOf(centsString) != 0) {
            cents *= 10;
        }
        return (dollars * 100) + cents;
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
    public void setNewItemId(String itemId) {
        mItemId = itemId;
    }

    @Override
    public void showExistingItem(Item item) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                getString(R.string.edit_date_format), Locale.getDefault());
        final Calendar today = Calendar.getInstance();
        String todayString = simpleDateFormat.format(today.getTime());
        final Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        String yesterdayString = simpleDateFormat.format(yesterday.getTime());

        if (item.getPurchaseDate() != -1) {
            Calendar purchaseDate = Calendar.getInstance();
            purchaseDate.setTimeInMillis(item.getPurchaseDate());
            String purchaseDateString = simpleDateFormat.format(purchaseDate.getTime());
            String currentPurchaseDateString = simpleDateFormat.format(mPurchaseDate.getTime());
            if (!currentPurchaseDateString.equals(purchaseDateString)) {
                mPurchaseDate = purchaseDate;
                if (purchaseDateString.equals(todayString)) {
                    mPurchaseDateSpinner.setSelection(
                            mPurchaseDateAdapter.getPosition(
                                    getString(R.string.edit_date_today)));
                } else if (purchaseDateString.equals(yesterdayString)) {
                    mPurchaseDateSpinner.setSelection(
                            mPurchaseDateAdapter.getPosition(
                                    getString(R.string.edit_date_yesterday)));
                } else {
                    if (mSelectedPurchaseDate != null) {
                        mPurchaseDateAdapter.remove(mSelectedPurchaseDate);
                        mSelectedPurchaseDate = null;
                    }
                    mSelectedPurchaseDate = simpleDateFormat.format(purchaseDate.getTime());
                    mPurchaseDateAdapter.insert(mSelectedPurchaseDate, 0);
                    mPurchaseDateSpinner.setSelection(0);
                }
            }
        } else {
            mPurchaseDateSpinner.setSelection(
                    mPurchaseDateAdapter.getPosition(
                            getString(R.string.edit_date_unknown)));
        }
        if (item.getShop() != null
                && !item.getShop().equals(mShop.getText().toString())) {
            mShop.setText(item.getShop());
        }
        if (item.getPricePaid() != -1) {
            int totalCentsInView = -1;
            if (!mPricePaid.getText().toString().equals("")
                    && !mPricePaid.getText().toString().equals(".")) {
                totalCentsInView = getTotalCents(mPricePaid.getText().toString());
            }
            if (totalCentsInView != item.getPricePaid()) {
                String priceString = getPriceFromTotalCents(item.getPricePaid());
                mPricePaid.setText(priceString);
            }
        }
        if (item.getDiscount() != -1) {
            int totalCentsInView = -1;
            if (!mDiscount.getText().toString().equals("")
                    && !mDiscount.getText().toString().equals(".")) {
                totalCentsInView = getTotalCents(mDiscount.getText().toString());
            }
            if (totalCentsInView != item.getDiscount()) {
                String discountString = getPriceFromTotalCents(item.getDiscount());
                mDiscount.setText(discountString);
            }
        }
        if (item.getExpiry() != -1) {
            Calendar expiryDate = Calendar.getInstance();
            expiryDate.setTimeInMillis(item.getExpiry());
            String expiryDateString = simpleDateFormat.format(expiryDate.getTime());
            String currentExpiryDateString = simpleDateFormat.format(mExpiryDate.getTime());
            if (!currentExpiryDateString.equals(expiryDateString)) {
                mExpiryDate = expiryDate;
                if (expiryDateString.equals(todayString)) {
                    mExpiryDateSpinner.setSelection(
                            mExpiryDateAdapter.getPosition(
                                    getString(R.string.edit_date_today)));
                } else if (expiryDateString.equals(yesterdayString)) {
                    mExpiryDateSpinner.setSelection(
                            mExpiryDateAdapter.getPosition(
                                    getString(R.string.edit_date_yesterday)));
                } else {
                    if (mSelectedExpiryDate != null) {
                        mExpiryDateAdapter.remove(mSelectedExpiryDate);
                        mSelectedExpiryDate = null;
                    }
                    mSelectedExpiryDate = simpleDateFormat.format(expiryDate.getTime());
                    mExpiryDateAdapter.insert(mSelectedExpiryDate, 0);
                    mExpiryDateSpinner.setSelection(0);
                }
            }
        } else {
            mExpiryDateSpinner.setSelection(
                    mExpiryDateAdapter.getPosition(
                            getString(R.string.edit_date_unknown)));
        }
        if (item.getCategory() != null
                && !item.getCategory().equals(mCategory.getText().toString())) {
            mCategory.setText(item.getCategory());
        }
        if (item.getSubCategory() != null
                && !item.getSubCategory().equals(mSubCategory.getText().toString())) {
            mSubCategory.setText(item.getSubCategory());
        }
        if (item.getType() != null
                && !item.getType().equals(mType.getText().toString())) {
            mType.setText(item.getType());
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
        if (item.getPrimaryColour() != null
                && !item.getPrimaryColour().equals(mPrimaryColour.getText().toString())) {
            mPrimaryColour.setText(item.getPrimaryColour());
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

        if (item.getDescription() != null
                && !item.getDescription().equals(mDescription.getText().toString())) {
            mDescription.setText(item.getDescription());
        }
        if (item.getNote() != null
                && !item.getNote().equals(mNote.getText().toString())) {
            mNote.setText(item.getNote());
        }
        if (item.getImageUrl() != null) {
            if (!item.getImageUrl().equals(mImageUrl) || mItemImage.getVisibility() == View.GONE) {
                mImageUrl = item.getImageUrl();
                showImage(mImageUrl);
            }
        } else {
            mImageUrl = null;
            mItemImage.setVisibility(View.GONE);
            mRemoveImageButton.setVisibility(View.GONE);
        }
    }

    public static String getPriceFromTotalCents(int totalCents) {
        int dollars = totalCents / 100;
        int cents = totalCents % 100;
        String centsString = cents > 9
                ? String.valueOf(cents)
                : "0" + cents;
        return cents == 0
                ? String.valueOf(dollars)
                : dollars + "." + centsString;
    }

    @Override
    public void showItemsUi() {
        if (mIsLargeScreen) {
            dismiss();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void openCamera(Uri saveTo) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, saveTo);
            startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
        }
    }

    @Override
    public void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        startActivityForResult(intent, REQUEST_CODE_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                mPresenter.imageAvailable();
            } else {
                mPresenter.imageCaptureFailed();
            }
        } else if (requestCode == REQUEST_CODE_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                mPresenter.imageSelected(selectedImageUri);
            } else {
                mPresenter.imageCaptureFailed();
            }
        }
    }

    @Override
    public void showImage(@NonNull String imageUrl) {
        mImageUrl = imageUrl;
        mItemImage.setVisibility(View.VISIBLE);
        mRemoveImageButton.setVisibility(View.VISIBLE);
        EspressoIdlingResource.increment();
        Glide.with(this)
                .load(mImageUrl)
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
        mPresenter.itemChanged();
    }

    @Override
    public void removeImage() {
        mImageUrl = null;
        mPresenter.itemChanged();
    }

    @Override
    public void passDeletedItemToItemsUi() {
        mCallback.onItemDeleted(createCurrentItem());
    }

    @Override
    public void showImageError() {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Nothing to do here.
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mPresenter.itemChanged();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Nothing to do here.
    }

    public interface Callback {

        void configureSupportActionBar(Toolbar toolbar, Drawable icon);

        void onDialogResumed();

        void onItemDeleted(@NonNull Item item);
    }
}
