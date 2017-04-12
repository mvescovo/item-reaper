package com.michaelvescovo.android.itemreaper.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Michael Vescovo
 *
 * To and from byte array from http://www.java2s.com/Code/Java/File-Input-Output/Convertobjecttobytearrayandconvertbytearraytoobject.htm
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
        return new SimpleDateFormat(format, Locale.ENGLISH);
    }

    public static byte[] toByteArray(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return bytes;
    }

    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return obj;
    }
}
