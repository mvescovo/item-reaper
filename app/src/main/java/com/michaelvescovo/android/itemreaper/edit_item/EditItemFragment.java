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
        if (mIsLargeScreen) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.doneEditing();
                }
            });
        }

        setHasOptionsMenu(true);

        if (getArguments() != null
                && getArguments().getString(EditItemActivity.EXTRA_ITEM_ID) != null) {
            mItemId = getArguments().getString(EditItemActivity.EXTRA_ITEM_ID);
        }

        configureViews();

        /* TEMP */
        mItemId = "1";

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

        mShop.addTextChangedListener(this);
        mPricePaid.addTextChangedListener(this);
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
                                        "dd MMMM YYYY", Locale.getDefault());
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
                                        "dd MMMM YYYY", Locale.getDefault());
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
            mPreviousExpiryDateOption = mExpiryDateAdapter.getPosition(selectedOption);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_take_photo:
                mPresenter.takePicture(getContext());
                break;
            case R.id.action_select_image:
                mPresenter.selectImage(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void validateItem() {
        int pricePaid = -1;
        if (!mPricePaid.getText().toString().equals("")) {
            pricePaid = Integer.parseInt(mPricePaid.getText().toString());
        }
        int discount = -1;
        if (!mDiscount.getText().toString().equals("")) {
            discount = Integer.parseInt(mDiscount.getText().toString());
        }

        Item newItem = new Item(
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
        mPresenter.saveItem(newItem);
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
        final Calendar today = Calendar.getInstance();
        final Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        final Calendar calendar = Calendar.getInstance();

        if (item.getPurchaseDate() != -1) {
            Calendar purchaseDate = Calendar.getInstance();
            purchaseDate.setTimeInMillis(item.getPurchaseDate());
            if (mPurchaseDate == null || mPurchaseDate.compareTo(purchaseDate) != 0) {
                mPurchaseDate = purchaseDate;
                if (purchaseDate.compareTo(today) == 0) {
                    mPurchaseDateSpinner.setSelection(
                            mPurchaseDateAdapter.getPosition(
                                    getString(R.string.edit_date_today)));
                } else if (purchaseDate.compareTo(yesterday) == 0) {
                    mPurchaseDateSpinner.setSelection(
                            mPurchaseDateAdapter.getPosition(
                                    getString(R.string.edit_date_yesterday)));
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                            "dd MMMM YYYY", Locale.getDefault());
                    mSelectedPurchaseDate = simpleDateFormat.format(purchaseDate.getTime());
                    mPurchaseDateAdapter.insert(mSelectedPurchaseDate, 0);
                    mPurchaseDateSpinner.setSelection(0);
                }
            }
        }
        if (item.getShop() != null
                && !item.getShop().equals(mShop.getText().toString())) {
            mShop.setText(item.getShop());
        }
        if (item.getPricePaid() != -1
                && !String.valueOf(item.getPricePaid()).equals(mPricePaid.getText().toString())) {
            mPricePaid.setText(String.valueOf(item.getPricePaid()));
        }
        if (item.getDiscount() != -1
                && !String.valueOf(item.getDiscount()).equals(mDiscount.getText().toString())) {
            mDiscount.setText(String.valueOf(item.getDiscount()));
        }
        if (item.getExpiry() != -1) {
            Calendar expiryDate = Calendar.getInstance();
            expiryDate.setTimeInMillis(item.getExpiry());
            if (mExpiryDate == null || mExpiryDate.compareTo(expiryDate) != 0) {
                mExpiryDate = expiryDate;
                if (expiryDate.compareTo(today) == 0) {
                    mExpiryDateSpinner.setSelection(
                            mExpiryDateAdapter.getPosition(
                                    getString(R.string.edit_date_today)));
                } else if (expiryDate.compareTo(yesterday) == 0) {
                    mExpiryDateSpinner.setSelection(
                            mExpiryDateAdapter.getPosition(
                                    getString(R.string.edit_date_yesterday)));
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                            "dd MMMM YYYY", Locale.getDefault());
                    mSelectedExpiryDate = simpleDateFormat.format(expiryDate.getTime());
                    mExpiryDateAdapter.insert(mSelectedExpiryDate, 0);
                    mExpiryDateSpinner.setSelection(0);
                }
            }
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
    }
}
