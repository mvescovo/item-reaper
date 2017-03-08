package com.michaelvescovo.android.itemreaper.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import static java.lang.Thread.sleep;

/**
 * @author Michael Vescovo
 */

public class RotationHelper {

    private static final int ROTATION_TIME_OUT = 500;
    private Context mContext;
    private Activity mActivity;

    public RotationHelper(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
    }

    public void rotateScreen() {
        int orientation = mContext.getResources().getConfiguration().orientation;
        mActivity.setRequestedOrientation(
                orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        );
        sleepAfterRotation(ROTATION_TIME_OUT);
    }

    public void setPortrait() {
        mActivity.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sleepAfterRotation(ROTATION_TIME_OUT);
    }

    public void setLandscape() {
        mActivity.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sleepAfterRotation(ROTATION_TIME_OUT);
    }

    // Sometimes after rotating the device in Espresso it can't close the keyboard. Putting a delay
    // seems to be an effective workaround. However, a better workaround or fix would be great!
    private static void sleepAfterRotation(int ms) {
        try {
            sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
