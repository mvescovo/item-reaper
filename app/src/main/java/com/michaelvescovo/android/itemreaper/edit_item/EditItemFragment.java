package com.michaelvescovo.android.itemreaper.edit_item;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.michaelvescovo.android.itemreaper.auth.AuthActivity;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.util.Analytics;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;
import com.michaelvescovo.android.itemreaper.util.ImageFile;
import com.michaelvescovo.android.itemreaper.util.ImageUploadService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.michaelvescovo.android.itemreaper.util.Constants.REQUEST_CODE_SIGNIN;
import static com.michaelvescovo.android.itemreaper.util.ImageUploadService.ACTION_REMOVE_IMAGE;
import static com.michaelvescovo.android.itemreaper.util.ImageUploadService.ACTION_UPLOAD_IMAGE;
import static com.michaelvescovo.android.itemreaper.util.ImageUploadService.EXTRA_ITEM;
import static com.michaelvescovo.android.itemreaper.util.MiscHelperMethods.getPriceFromTotalCents;

/**
 * @author Michael Vescovo
 */

public class EditItemFragment extends AppCompatDialogFragment implements EditItemContract.View,
        TextWatcher {

    public static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE_PHOTO_PICKER = 2;
    private static final String STATE_IMAGE_FILE = "imageFile";
    private static final String STATE_IMAGE_COMPRESSING = "imageCompressing";
    private static final int COMPRESSION_AMOUNT = 50;
    private static final int SCALE_WIDTH = 1920;
    private static final int SCALE_HEIGHT = 1080;

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
    @BindView(R.id.edit_main_colour)
    TextView mMainColour;
    @BindView(R.id.edit_main_colour_shade)
    TextView mMainColourShade;
    @BindView(R.id.edit_accent_colour)
    TextView mAccentColour;
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
    @BindView(R.id.item_image)
    ImageView mItemImage;
    @BindView(R.id.item_remove_image_button)
    Button mRemoveImageButton;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private EditItemContract.Presenter mPresenter;
    private Callback mCallback;
    private boolean mIsLargeScreen;
    private Typeface mAppbarTypeface;
    private String mItemId;
    private String mImageUrl;
    private boolean mDeceased;
    private Calendar mPurchaseDate;
    private String mPurchaseDateString;
    private int mPreviousPurchaseDateOption;
    private ArrayAdapter<String> mPurchaseDateAdapter;
    private Calendar mExpiryDate;
    private String mExpiryDateString;
    private int mPreviousExpiryDateOption;
    private ArrayAdapter<String> mExpiryDateAdapter;
    private boolean mPurchaseDateListener;
    private boolean mExpiryDateListener;
    private boolean mImageViewListener;
    private ImageFile mImageFile;
    private boolean mCompressing;
    private Item mItem;

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
        mItem = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_item, container, false);
        ButterKnife.bind(this, root);
        mCallback.configureSupportActionBar(mToolbar, ContextCompat.getDrawable(getContext(),
                R.drawable.ic_done));
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

        if (savedInstanceState != null) {
            mImageFile = (ImageFile) savedInstanceState.getSerializable(STATE_IMAGE_FILE);
            mCompressing = savedInstanceState.getBoolean(STATE_IMAGE_COMPRESSING);
        }

        /* TEMP */
//        mItemId = "1";

        return root;
    }

    private void configureViews() {
        configurePurchaseDate();
        configureExpiryDate();
        mPricePaid.setFilters(createCurrencyFilter());
        mDiscount.setFilters(createCurrencyFilter());
        mRemoveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.deleteImage();
                Analytics.logEventRemoveItemImage(getContext());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_IMAGE_FILE, (Serializable) mImageFile);
        outState.putBoolean(STATE_IMAGE_COMPRESSING, mCompressing);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    private InputFilter[] createCurrencyFilter() {
        final Pattern pattern = Pattern.compile("(0|[1-9][0-9]{0,8})?(\\.[0-9]{0,2})?");
        return new InputFilter[]{
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
    }

    private void configurePurchaseDate() {
        mPurchaseDateListener = false;
        String[] dateOptions = getResources().getStringArray(R.array.date_array);
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
    }

    private void configureExpiryDate() {
        mExpiryDateListener = false;
        String[] dateOptions = getResources().getStringArray(R.array.date_array);
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
    }

    private void onPurchaseDateOptionSelected(String selectedOption) {
        if (selectedOption.equals(getString(R.string.edit_date_custom))) {
            purchaseDateCustomSelected();
        } else {
            if (selectedOption.equals(getString(R.string.edit_date_today))) {
                mPurchaseDate = Calendar.getInstance();
                clearPurchaseDateString();
            } else if (selectedOption.equals(getString(R.string.edit_date_yesterday))) {
                mPurchaseDate = Calendar.getInstance();
                mPurchaseDate.add(Calendar.DAY_OF_YEAR, -1);
                clearPurchaseDateString();
            } else if (selectedOption.equals(getString(R.string.edit_date_unknown))) {
                mPurchaseDate = null;
                clearPurchaseDateString();
            }
            mPurchaseDateSpinner.setSelection(mPurchaseDateAdapter.getPosition(selectedOption));
            if (mPurchaseDateListener) {
                mPresenter.itemChanged();
            }
            mPreviousPurchaseDateOption = mPurchaseDateAdapter.getPosition(selectedOption);
        }
    }

    private void onExpiryDateOptionSelected(String selectedOption) {
        if (selectedOption.equals(getString(R.string.edit_date_custom))) {
            expiryDateCustomSelected();
        } else {
            if (selectedOption.equals(getString(R.string.edit_date_today))) {
                mExpiryDate = Calendar.getInstance();
                clearExpiryDateString();
            } else if (selectedOption.equals(getString(R.string.edit_date_yesterday))) {
                mExpiryDate = Calendar.getInstance();
                mExpiryDate.add(Calendar.DAY_OF_YEAR, -1);
                clearExpiryDateString();
            } else if (selectedOption.equals(getString(R.string.edit_date_unknown))) {
                mExpiryDate = null;
                clearExpiryDateString();
            }
            mExpiryDateSpinner.setSelection(mExpiryDateAdapter.getPosition(selectedOption));
            if (mExpiryDateListener) {
                mPresenter.itemChanged();
            }
            mPreviousExpiryDateOption = mExpiryDateAdapter.getPosition(selectedOption);
        }
    }

    private void clearPurchaseDateString() {
        if (mPurchaseDateString != null) {
            mPurchaseDateAdapter.remove(mPurchaseDateString);
            mPurchaseDateString = null;
        }
    }

    private void clearExpiryDateString() {
        if (mExpiryDateString != null) {
            mExpiryDateAdapter.remove(mExpiryDateString);
            mExpiryDateString = null;
        }
    }

    private void purchaseDateCustomSelected() {
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
                        clearPurchaseDateString();
                        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        calendar.set(Calendar.MONTH, datePicker.getMonth());
                        calendar.set(Calendar.YEAR, datePicker.getYear());
                        mPurchaseDate = calendar;
                        if (calendar.compareTo(today) == 0) {
                            mPurchaseDateSpinner.setSelection(
                                    mPurchaseDateAdapter.getPosition(
                                            getString(R.string.edit_date_today)));
                        } else if (calendar.compareTo(yesterday) == 0) {
                            mPurchaseDateSpinner.setSelection(
                                    mPurchaseDateAdapter.getPosition(
                                            getString(R.string.edit_date_yesterday)));
                        } else {
                            mPurchaseDateString = getSimpleDateFormat().format(calendar.getTime());
                            mPurchaseDateAdapter.insert(mPurchaseDateString, 0);
                            mPurchaseDateSpinner.setSelection(0);
                        }
                        if (mPurchaseDateListener) {
                            mPresenter.itemChanged();
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
    }

    private void expiryDateCustomSelected() {
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
                        clearExpiryDateString();
                        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        calendar.set(Calendar.MONTH, datePicker.getMonth());
                        calendar.set(Calendar.YEAR, datePicker.getYear());
                        mExpiryDate = calendar;
                        if (calendar.compareTo(today) == 0) {
                            mExpiryDateSpinner.setSelection(
                                    mExpiryDateAdapter.getPosition(
                                            getString(R.string.edit_date_today)));
                        } else if (calendar.compareTo(yesterday) == 0) {
                            mExpiryDateSpinner.setSelection(
                                    mExpiryDateAdapter.getPosition(
                                            getString(R.string.edit_date_yesterday)));
                        } else {
                            mExpiryDateString = getSimpleDateFormat().format(calendar.getTime());
                            mExpiryDateAdapter.insert(mExpiryDateString, 0);
                            mExpiryDateSpinner.setSelection(0);
                        }
                        if (mExpiryDateListener) {
                            mPresenter.itemChanged();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCompressing) {
            setInteractionEnabled(false);
            setProgressBar(true);
        }
        mPresenter.editItem(mItemId);
        mCallback.onDialogResumed();
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
    public void validateItem() {
        mPresenter.saveItem(createCurrentItem());
    }

    private Item createCurrentItem() {
        return new Item(
                mItemId,
                mPurchaseDate == null ? -1 : mPurchaseDate.getTimeInMillis(),
                getPrice(mPricePaid),
                getPrice(mDiscount),
                mExpiryDate == null ? -1 : mExpiryDate.getTimeInMillis(),
                !mCategory.getText().toString().equals("")
                        ? mCategory.getText().toString()
                        : null,
                !mSubCategory.getText().toString().equals("")
                        ? mSubCategory.getText().toString()
                        : null,
                !mType.getText().toString().equals("")
                        ? mType.getText().toString()
                        : null,
                !mSubType.getText().toString().equals("")
                        ? mSubType.getText().toString()
                        : null,
                !mSubType2.getText().toString().equals("")
                        ? mSubType2.getText().toString()
                        : null,
                !mSubType3.getText().toString().equals("")
                        ? mSubType3.getText().toString()
                        : null,
                !mMainColour.getText().toString().equals("")
                        ? mMainColour.getText().toString()
                        : null,
                !mMainColourShade.getText().toString().equals("")
                        ? mMainColourShade.getText().toString()
                        : null,
                !mAccentColour.getText().toString().equals("")
                        ? mAccentColour.getText().toString()
                        : null,
                !mSize.getText().toString().equals("")
                        ? mSize.getText().toString()
                        : null,
                !mBrand.getText().toString().equals("")
                        ? mBrand.getText().toString()
                        : null,
                !mShop.getText().toString().equals("")
                        ? mShop.getText().toString()
                        : null,
                !mDescription.getText().toString().equals("")
                        ? mDescription.getText().toString()
                        : null,
                !mNote.getText().toString().equals("")
                        ? mNote.getText().toString()
                        : null,
                mImageUrl,
                mDeceased
        );
    }

    private int getPrice(TextView textView) {
        if (!textView.getText().toString().equals("")
                && !textView.getText().toString().equals(".")) {
            String priceString = textView.getText().toString();
            return getTotalCents(priceString);
        } else {
            return -1;
        }
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
    public void setDefaultDates() {
        // Purchase date
        mPurchaseDate = Calendar.getInstance();

        // Expiry date
        mExpiryDate = Calendar.getInstance();
        mExpiryDate.add(Calendar.YEAR, 1);
        mExpiryDateString = getSimpleDateFormat().format(mExpiryDate.getTime());
        mExpiryDateAdapter.insert(mExpiryDateString, 0);
        mExpiryDateSpinner.setSelection(0);
        mPresenter.itemChanged();
        enableListeners();
    }

    @Override
    public void showExistingItem(final Item item) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mItem = item;
                    // If the fragment is not active, don't allow callbacks to crash the app.
                    if (getActivity() != null) {
                        disableListeners();
                        showPurchaseDate(item);
                        if (item.getShop() != null) {
                            if (!item.getShop().equals(mShop.getText().toString())) {
                                mShop.setText(item.getShop());
                            }
                        } else {
                            mShop.setText(null);
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
                        } else {
                            mPricePaid.setText("");
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
                        } else {
                            mDiscount.setText("");
                        }
                        showExpiryDate(item);
                        if (item.getCategory() != null) {
                            if (!item.getCategory().equals(mCategory.getText().toString())) {
                                mCategory.setText(item.getCategory());
                            }
                        } else {
                            mCategory.setText(null);
                        }
                        if (item.getSubCategory() != null) {
                            if (!item.getSubCategory().equals(mSubCategory.getText().toString())) {
                                mSubCategory.setText(item.getSubCategory());
                            }
                        } else {
                            mSubCategory.setText(null);
                        }
                        if (item.getType() != null) {
                            if (!item.getType().equals(mType.getText().toString())) {
                                mType.setText(item.getType());
                            }
                        } else {
                            mType.setText(null);
                        }
                        if (item.getSubType() != null) {
                            if (!item.getSubType().equals(mSubType.getText().toString())) {
                                mSubType.setText(item.getSubType());
                            }
                        } else {
                            mSubType.setText(null);
                        }
                        if (item.getSubType2() != null) {
                            if (!item.getSubType2().equals(mSubType2.getText().toString())) {
                                mSubType2.setText(item.getSubType2());
                            }
                        } else {
                            mSubType2.setText(null);
                        }
                        if (item.getSubType3() != null) {
                            if (!item.getSubType3().equals(mSubType3.getText().toString())) {
                                mSubType3.setText(item.getSubType3());
                            }
                        } else {
                            mSubType3.setText(null);
                        }
                        if (item.getMainColour() != null) {
                            if (!item.getMainColour().equals(mMainColour.getText().toString())) {
                                mMainColour.setText(item.getMainColour());
                            }
                        } else {
                            mMainColour.setText(null);
                        }
                        if (item.getMainColourShade() != null) {
                            if (!item.getMainColourShade().equals(mMainColourShade.getText().toString())) {
                                mMainColourShade.setText(item.getMainColourShade());
                            }
                        } else {
                            mMainColourShade.setText(null);
                        }
                        if (item.getAccentColour() != null) {
                            if (!item.getAccentColour().equals(mAccentColour.getText().toString())) {
                                mAccentColour.setText(item.getAccentColour());
                            }
                        } else {
                            mAccentColour.setText(null);
                        }
                        if (item.getSize() != null) {
                            if (!item.getSize().equals(mSize.getText().toString())) {
                                mSize.setText(item.getSize());
                            }
                        } else {
                            mSize.setText(null);
                        }
                        if (item.getBrand() != null) {
                            if (!item.getBrand().equals(mBrand.getText().toString())) {
                                mBrand.setText(item.getBrand());
                            }
                        } else {
                            mBrand.setText(null);
                        }
                        if (item.getDescription() != null) {
                            if (!item.getDescription().equals(mDescription.getText().toString())) {
                                mDescription.setText(item.getDescription());
                            }
                        } else {
                            mDescription.setText(null);
                        }
                        if (item.getNote() != null) {
                            if (!item.getNote().equals(mNote.getText().toString())) {
                                mNote.setText(item.getNote());
                            }
                        } else {
                            mNote.setText(null);
                        }
                        if (item.getImageUrl() != null) {
                            if (!item.getImageUrl().equals(mImageUrl) || mItemImage.getVisibility() == View.GONE) {
                                showImage(item.getImageUrl());
                            }
                        } else {
                            mImageUrl = null;
                            mItemImage.setVisibility(View.GONE);
                            mRemoveImageButton.setVisibility(View.GONE);
                        }
                        mDeceased = item.getDeceased();
                        enableListeners();
                    }
                }
            });
        }
    }

    private void disableListeners() {
        mPurchaseDateListener = false;
        mShop.removeTextChangedListener(this);
        mPricePaid.removeTextChangedListener(this);
        mDiscount.removeTextChangedListener(this);
        mExpiryDateListener = false;
        mCategory.removeTextChangedListener(this);
        mSubCategory.removeTextChangedListener(this);
        mType.removeTextChangedListener(this);
        mSubType.removeTextChangedListener(this);
        mSubType2.removeTextChangedListener(this);
        mSubType3.removeTextChangedListener(this);
        mMainColour.removeTextChangedListener(this);
        mMainColourShade.removeTextChangedListener(this);
        mAccentColour.removeTextChangedListener(this);
        mSize.removeTextChangedListener(this);
        mBrand.removeTextChangedListener(this);
        mDescription.removeTextChangedListener(this);
        mNote.removeTextChangedListener(this);
        mImageViewListener = false;
    }

    private void enableListeners() {
        mPurchaseDateListener = true;
        mShop.addTextChangedListener(this);
        mPricePaid.addTextChangedListener(this);
        mDiscount.addTextChangedListener(this);
        mExpiryDateListener = true;
        mCategory.addTextChangedListener(this);
        mSubCategory.addTextChangedListener(this);
        mType.addTextChangedListener(this);
        mSubType.addTextChangedListener(this);
        mSubType2.addTextChangedListener(this);
        mSubType3.addTextChangedListener(this);
        mMainColour.addTextChangedListener(this);
        mMainColourShade.addTextChangedListener(this);
        mAccentColour.addTextChangedListener(this);
        mSize.addTextChangedListener(this);
        mBrand.addTextChangedListener(this);
        mDescription.addTextChangedListener(this);
        mNote.addTextChangedListener(this);
        mImageViewListener = true;
    }

    @Override
    public void setInteractionEnabled(boolean enabled) {
        if (enabled) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.doneEditing();
                }
            });
        } else {
            mToolbar.setNavigationOnClickListener(null);
        }
        mCallback.editMenuEnabled(enabled);
        mRemoveImageButton.setEnabled(enabled);
    }

    private void showPurchaseDate(Item item) {
        if (item.getPurchaseDate() != -1) {
            Calendar purchaseDate = Calendar.getInstance();
            purchaseDate.setTimeInMillis(item.getPurchaseDate());
            mPurchaseDate = purchaseDate;
            String purchaseDateString = getSimpleDateFormat().format(mPurchaseDate.getTime());
            if (purchaseDateString.equals(getTodayString())) {
                mPurchaseDateSpinner.setSelection(
                        mPurchaseDateAdapter.getPosition(getString(R.string.edit_date_today)));
            } else if (purchaseDateString.equals(getYesterdayString())) {
                mPurchaseDateSpinner.setSelection(
                        mPurchaseDateAdapter.getPosition(
                                getString(R.string.edit_date_yesterday)));
            } else {
                if (mPurchaseDateString != null) {
                    mPurchaseDateAdapter.remove(mPurchaseDateString);
                    mPurchaseDateString = null;
                }
                mPurchaseDateString = purchaseDateString;
                mPurchaseDateAdapter.insert(mPurchaseDateString, 0);
                mPurchaseDateSpinner.setSelection(0);
            }
        } else {
            mPurchaseDate = null;
            mPurchaseDateSpinner.setSelection(
                    mPurchaseDateAdapter.getPosition(
                            getString(R.string.edit_date_unknown)));
        }
    }

    private void showExpiryDate(Item item) {
        if (item.getExpiry() != -1) {
            Calendar expiryDate = Calendar.getInstance();
            expiryDate.setTimeInMillis(item.getExpiry());
            mExpiryDate = expiryDate;
            String expiryDateString = getSimpleDateFormat().format(mExpiryDate.getTime());
            if (expiryDateString.equals(getTodayString())) {
                mExpiryDateSpinner.setSelection(
                        mExpiryDateAdapter.getPosition(
                                getString(R.string.edit_date_today)));
            } else if (expiryDateString.equals(getYesterdayString())) {
                mExpiryDateSpinner.setSelection(
                        mExpiryDateAdapter.getPosition(
                                getString(R.string.edit_date_yesterday)));
            } else {
                if (mExpiryDateString != null) {
                    mExpiryDateAdapter.remove(mExpiryDateString);
                    mExpiryDateString = null;
                }
                mExpiryDateString = getSimpleDateFormat().format(expiryDate.getTime());
                mExpiryDateAdapter.insert(mExpiryDateString, 0);
                mExpiryDateSpinner.setSelection(0);
            }
        } else {
            mExpiryDate = null;
            mExpiryDateSpinner.setSelection(
                    mExpiryDateAdapter.getPosition(
                            getString(R.string.edit_date_unknown)));
        }
    }

    private SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(
                getString(R.string.edit_date_format), Locale.ENGLISH);
    }

    private String getTodayString() {
        final Calendar today = Calendar.getInstance();
        return getSimpleDateFormat().format(today.getTime());
    }

    private String getYesterdayString() {
        final Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        return getSimpleDateFormat().format(yesterday.getTime());
    }

    @Override
    public void showItemsUi() {
        if (mIsLargeScreen) {
            if (mCallback != null) {
                mCallback.onDoneEditing();
                dismiss();
            }
        } else {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_take_photo:
                mPresenter.takePicture(getContext());
                if (mItem != null) {
                    Analytics.logEventTakeItemPhoto(getContext(), mItem);
                }
                break;
            case R.id.action_select_image:
                mPresenter.selectImage();
                if (mItem != null) {
                    Analytics.logEventSelectItemImage(getContext(), mItem);
                }
                break;
            case R.id.action_delete_item:
                mPresenter.deleteItem(createCurrentItem());
                if (mItem != null) {
                    Analytics.logEventDeleteItem(getContext(), mItem);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openCamera(ImageFile imageFile) {
        mImageFile = imageFile;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageFile.getUri(getContext()));
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
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    mPresenter.imageAvailable(mImageFile);
                } else {
                    mPresenter.imageCaptureFailed(getContext(), mImageFile);
                }
                break;
            case REQUEST_CODE_PHOTO_PICKER:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    mPresenter.imageSelected(getContext(), selectedImageUri);
                } else {
                    mPresenter.imageCaptureFailed(getContext(), mImageFile);
                }
                break;
            case REQUEST_CODE_SIGNIN:
                if (resultCode == RESULT_CANCELED) {
                    getActivity().finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showImage(@NonNull final String imageUrl) {
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
                        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                            EspressoIdlingResource.decrement();
                        }
                    }
                });
        if (mImageViewListener) {
            mPresenter.itemChanged();
        }
        if (!mImageUrl.startsWith("https://firebasestorage")
                && !mImageUrl.startsWith("file:///android_asset/black-t-shirt.jpg")) {
            Intent imageUploadIntent = new Intent(getContext(), ImageUploadService.class);
            imageUploadIntent.setAction(ACTION_UPLOAD_IMAGE);
            imageUploadIntent.putExtra(EXTRA_ITEM, createCurrentItem());
            getActivity().startService(imageUploadIntent);
        }
    }

    @Override
    public void compressImage(@NonNull final String imagePath) {
        mCompressing = true;
        new Thread(new Runnable() {
            public void run() {
                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;
                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / SCALE_WIDTH, photoH / SCALE_HEIGHT);
                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
                // Compress into WEBP format
                try {
                    File file = new File(imagePath);
                    OutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.WEBP, COMPRESSION_AMOUNT, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.imageCompressed(imagePath);
                            mCompressing = false;
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void showSignIn() {
        Intent intent = new Intent(getContext(), AuthActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SIGNIN);
    }

    @Override
    public void removeImage() {
        if (mImageUrl != null) {
            Intent imageUploadIntent = new Intent(getContext(), ImageUploadService.class);
            imageUploadIntent.setAction(ACTION_REMOVE_IMAGE);
            imageUploadIntent.putExtra(EXTRA_ITEM, createCurrentItem());
            getActivity().startService(imageUploadIntent);
        }
    }

    @Override
    public void passDeletedItemToItemsUi() {
        if (mCallback != null) {
            mCallback.onItemDeleted(createCurrentItem());
        }
    }

    @Override
    public void showImageError() {
        Snackbar.make(mCoordinatorLayout,
                getString(R.string.image_capture_failed), Snackbar.LENGTH_LONG).show();
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

        void onDoneEditing();

        void editMenuEnabled(boolean enabled);
    }
}
