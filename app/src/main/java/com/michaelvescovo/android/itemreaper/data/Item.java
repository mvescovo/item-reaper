package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Vescovo
 */

public class Item implements Serializable, Comparable {

    @NonNull
    private String mId;
    private long mPurchaseDate;
    private int mPricePaid;
    private int mDiscount;
    private long mExpiry;
    @Nullable
    private String mCategory;
    @Nullable
    private String mSubCategory;
    @Nullable
    private String mType;
    @Nullable
    private String mSubtype;
    @Nullable
    private String mSubtype2;
    @Nullable
    private String mSubtype3;
    @Nullable
    private String mPrimaryColour;
    @Nullable
    private String mPrimaryColourShade;
    @Nullable
    private String mSecondaryColour;
    @Nullable
    private String mSize;
    @Nullable
    private String mBrand;
    @Nullable
    private String mShop;
    @Nullable
    private String mDescription;
    @Nullable
    private String mNote;
    @Nullable
    private String mImageUrl;
    private boolean mDeceased;

    public Item() {
        // Firebase required no arg constructor.
    }

    public Item(@NonNull String id) {
        mId = id;
        mDeceased = false;
    }

    public Item(@NonNull String id, long purchaseDate, int pricePaid, int discount,
                long expiry, @Nullable String category, @Nullable String subCategory,
                @NonNull String type, @Nullable String subtype, @Nullable String subtype2,
                @Nullable String subtype3, @Nullable String primaryColour,
                @Nullable String primaryColourShade, @Nullable String secondaryColour,
                @Nullable String size, @Nullable String brand, @Nullable String shop,
                @Nullable String description, @Nullable String note,
                @Nullable String imageUrl, boolean deceased) {
        mId = id;
        mPurchaseDate = purchaseDate;
        mPricePaid = pricePaid;
        mDiscount = discount;
        mExpiry = expiry;
        mCategory = category;
        mSubCategory = subCategory;
        mType = type;
        mSubtype = subtype;
        mSubtype2 = subtype2;
        mSubtype3 = subtype3;
        mPrimaryColour = primaryColour;
        mPrimaryColourShade = primaryColourShade;
        mSecondaryColour = secondaryColour;
        mSize = size;
        mBrand = brand;
        mShop = shop;
        mDescription = description;
        mNote = note;
        mImageUrl = imageUrl;
        mDeceased = deceased;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    public long getPurchaseDate() {
        return mPurchaseDate;
    }

    public void setPurchaseDate(long purchaseDate) {
        mPurchaseDate = purchaseDate;
    }

    public int getPricePaid() {
        return mPricePaid;
    }

    public void setPricePaid(int pricePaid) {
        mPricePaid = pricePaid;
    }

    public int getDiscount() {
        return mDiscount;
    }

    public void setDiscount(int discount) {
        mDiscount = discount;
    }

    public long getExpiry() {
        return mExpiry;
    }

    public void setExpiry(long expiry) {
        mExpiry = expiry;
    }

    @Nullable
    public String getCategory() {
        return mCategory;
    }

    public void setCategory(@Nullable String category) {
        mCategory = category;
    }

    @Nullable
    public String getSubCategory() {
        return mSubCategory;
    }

    public void setSubCategory(@Nullable String subCategory) {
        mSubCategory = subCategory;
    }

    @Nullable
    public String getType() {
        return mType;
    }

    public void setType(@Nullable String type) {
        mType = type;
    }

    @Nullable
    public String getSubtype() {
        return mSubtype;
    }

    public void setSubtype(@Nullable String subtype) {
        mSubtype = subtype;
    }

    @Nullable
    public String getSubtype2() {
        return mSubtype2;
    }

    public void setSubtype2(@Nullable String subtype2) {
        mSubtype2 = subtype2;
    }

    @Nullable
    public String getSubtype3() {
        return mSubtype3;
    }

    public void setSubtype3(@Nullable String subtype3) {
        mSubtype3 = subtype3;
    }

    @Nullable
    public String getPrimaryColour() {
        return mPrimaryColour;
    }

    public void setPrimaryColour(@Nullable String primaryColour) {
        mPrimaryColour = primaryColour;
    }

    @Nullable
    public String getPrimaryColourShade() {
        return mPrimaryColourShade;
    }

    public void setPrimaryColourShade(@Nullable String primaryColourShade) {
        mPrimaryColourShade = primaryColourShade;
    }

    @Nullable
    public String getSecondaryColour() {
        return mSecondaryColour;
    }

    public void setSecondaryColour(@Nullable String secondaryColour) {
        mSecondaryColour = secondaryColour;
    }

    @Nullable
    public String getSize() {
        return mSize;
    }

    public void setSize(@Nullable String size) {
        mSize = size;
    }

    @Nullable
    public String getBrand() {
        return mBrand;
    }

    public void setBrand(@Nullable String brand) {
        mBrand = brand;
    }

    @Nullable
    public String getShop() {
        return mShop;
    }

    public void setShop(@Nullable String shop) {
        mShop = shop;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(@Nullable String description) {
        mDescription = description;
    }

    @Nullable
    public String getNote() {
        return mNote;
    }

    public void setNote(@Nullable String note) {
        mNote = note;
    }

    @Nullable
    public String getImageUrl() {
        return mImageUrl;
    }

    public void setPhotoUrls(@Nullable String imageUrl) {
        mImageUrl = imageUrl;
    }

    public boolean getDeceased() {
        return mDeceased;
    }

    public void setDeceased(boolean deceased) {
        mDeceased = deceased;
    }

    // For Firebase map
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", mId);
        result.put("purchaseDate", mPurchaseDate);
        result.put("pricePaid", mPricePaid);
        result.put("discount", mDiscount);
        result.put("expiry", mExpiry);
        result.put("category", mCategory);
        result.put("subCategory", mSubCategory);
        result.put("type", mType);
        result.put("subType", mSubtype);
        result.put("subType2", mSubtype2);
        result.put("subType3", mSubtype3);
        result.put("primaryColour", mPrimaryColour);
        result.put("primaryColourShade", mPrimaryColourShade);
        result.put("secondaryColour", mSecondaryColour);
        result.put("size", mSize);
        result.put("brand", mBrand);
        result.put("shop", mShop);
        result.put("description", mDescription);
        result.put("note", mNote);
        result.put("imageUrl", mImageUrl);
        result.put("deceased", mDeceased);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Item && ((Item) obj).getId().equals(mId);
    }


    @Override
    public int compareTo(@NonNull Object item2) {
        if (mExpiry > ((Item) item2).getExpiry()) {
            return 1;
        } else if (mExpiry == ((Item) item2).getExpiry()) {
            return 0;
        } else {
            return -1;
        }
    }
}
