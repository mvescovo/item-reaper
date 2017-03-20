package com.michaelvescovo.android.itemreaper.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Michael Vescovo
 */

public class MiscHelperMethods {

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

    public static SimpleDateFormat getDateFormat(String format) {
        return new SimpleDateFormat(format, Locale.getDefault());
    }
}
