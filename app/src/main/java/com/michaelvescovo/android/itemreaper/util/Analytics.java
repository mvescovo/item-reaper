package com.michaelvescovo.android.itemreaper.util;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.michaelvescovo.android.itemreaper.data.Item;

/**
 * Created by Michael Vescovo.
 */

public class Analytics {

    private static final String PURCHASE_DATE = "purchase_date";
    private static final String PRICE_PAID = "price_paid";
    private static final String DISCOUNT = "discount";
    private static final String EXPIRY = "expiry";
    private static final String CATEGORY = "category";
    private static final String SUB_CATEGORY = "sub_category";
    private static final String TYPE = "type";
    private static final String SUB_TYPE = "sub_type";
    private static final String SUB_TYPE2 = "sub_type2";
    private static final String SUB_TYPE3 = "sub_type3";
    private static final String MAIN_COLOUR = "main_colour";
    private static final String MAIN_COLOUR_SHADE = "main_colour_shade";
    private static final String ACCENT_COLOUR = "accent_colour";
    private static final String SIZE = "size";
    private static final String BRAND = "brand";
    private static final String SHOP = "shop";
    private static final String DESCRIPTION = "description";
    private static final String NOTE = "note";
    private static final String DECEASED = "deceased";

    public static void logEventViewItem(Context context, Item item) {
        Bundle params = new Bundle();
        if (item.getPurchaseDate() != -1) {
            params.putLong(PURCHASE_DATE, item.getPurchaseDate());
        }
        if (item.getPricePaid() != -1) {
            params.putInt(PRICE_PAID, item.getPricePaid());
        }
        if (item.getDiscount() != -1) {
            params.putInt(DISCOUNT, item.getDiscount());
        }
        if (item.getExpiry() != -1) {
            params.putLong(EXPIRY, item.getExpiry());
        }
        if (item.getCategory() != null) {
            params.putString(CATEGORY, item.getCategory());
        }
        if (item.getSubCategory() != null) {
            params.putString(SUB_CATEGORY, item.getSubCategory());
        }
        if (item.getType() != null) {
            params.putString(TYPE, item.getType());
        }
        if (item.getSubType() != null) {
            params.putString(SUB_TYPE, item.getSubType());
        }
        if (item.getSubType2() != null) {
            params.putString(SUB_TYPE2, item.getSubType2());
        }
        if (item.getSubType3() != null) {
            params.putString(SUB_TYPE3, item.getSubType3());
        }
        if (item.getMainColour() != null) {
            params.putString(MAIN_COLOUR, item.getMainColour());
        }
        if (item.getMainColourShade() != null) {
            params.putString(MAIN_COLOUR_SHADE, item.getMainColourShade());
        }
        if (item.getAccentColour() != null) {
            params.putString(ACCENT_COLOUR, item.getAccentColour());
        }
        if (item.getSize() != null) {
            params.putString(SIZE, item.getSize());
        }
        if (item.getBrand() != null) {
            params.putString(BRAND, item.getBrand());
        }
        if (item.getShop() != null) {
            params.putString(SHOP, item.getShop());
        }
        if (item.getDescription() != null) {
            params.putString(DESCRIPTION, item.getDescription());
        }
        if (item.getNote() != null) {
            params.putString(NOTE, item.getNote());
        }
        params.putBoolean(DECEASED, item.getDeceased());
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
    }
}

