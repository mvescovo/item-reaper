package com.michaelvescovo.android.itemreaper.util;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.michaelvescovo.android.itemreaper.data.Item;

/**
 * Created by Michael Vescovo.
 */

public class Analytics {

    private static final String LOGOUT = "logout";

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
    private static final String VIEW_ABOUT_PAGE = "view_about_page";
    private static final String VIEW_PRIVACY_POLICY = "view_privacy_policy";
    private static final String VIEW_REAPER_ICON_ATTRIBUTION = "view_reaper_icon_attribution";
    private static final String VIEW_REAPER_SOUND_ATTRIBUTION = "view_reaper_sound_attribution";
    private static final String EXPIRE_LIST_ITEM = "expire_list_item";
    private static final String UNDO_EXPIRE_LIST_ITEM = "undo_expire_list_item";
    private static final String ADD_ITEM = "add_item";
    private static final String EXPIRE_ITEM = "expire_item";
    private static final String UNDO_EXPIRE_ITEM = "undo_expire_item";
    private static final String EDIT_ITEM = "edit_item";
    private static final String TAKE_ITEM_PHOTO = "take_item_photo";
    private static final String SELECT_ITEM_IMAGE = "select_item_image";
    private static final String REMOVE_ITEM_IMAGE = "remove_item_image";
    private static final String DELETE_ITEM = "delete_item";
    private static final String UNDO_DELETE_ITEM = "undo_delete_item";

    public static void logEventLogout(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(LOGOUT, null);
    }

    public static void logEventViewItemList(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, null);
    }

    public static void logEventViewAboutPage(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(VIEW_ABOUT_PAGE, null);
    }

    public static void logEventViewPrivacyPolicy(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(VIEW_PRIVACY_POLICY, null);
    }

    public static void logEventViewReaperIconAttribution(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(VIEW_REAPER_ICON_ATTRIBUTION, null);
    }

    public static void logEventViewReaperSoundAttribution(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(VIEW_REAPER_SOUND_ATTRIBUTION, null);
    }

    public static void logEventViewSearchResults(Context context, String searchTerm) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm);
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, params);
    }

    public static void logEventExpireListItem(Context context, Item item) {
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
        FirebaseAnalytics.getInstance(context).logEvent(EXPIRE_LIST_ITEM, params);
    }

    public static void logEventUndoExpireListItem(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(UNDO_EXPIRE_LIST_ITEM, null);
    }

    public static void logEventAddItem(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(ADD_ITEM, null);
    }

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

    public static void logEventExpireItem(Context context, Item item) {
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
        FirebaseAnalytics.getInstance(context).logEvent(EXPIRE_ITEM, params);
    }

    public static void logEventUndoExpireItem(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(UNDO_EXPIRE_ITEM, null);
    }

    public static void logEventEditItem(Context context, Item item) {
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
        FirebaseAnalytics.getInstance(context).logEvent(EDIT_ITEM, params);
    }

    public static void logEventTakeItemPhoto(Context context, Item item) {
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
        FirebaseAnalytics.getInstance(context).logEvent(TAKE_ITEM_PHOTO, params);
    }

    public static void logEventSelectItemImage(Context context, Item item) {
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
        FirebaseAnalytics.getInstance(context).logEvent(SELECT_ITEM_IMAGE, params);
    }

    public static void logEventRemoveItemImage(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(REMOVE_ITEM_IMAGE, null);
    }

    public static void logEventDeleteItem(Context context, Item item) {
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
        FirebaseAnalytics.getInstance(context).logEvent(DELETE_ITEM, params);
    }

    public static void logEventUndoDeleteItem(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent(UNDO_DELETE_ITEM, null);
    }
}

