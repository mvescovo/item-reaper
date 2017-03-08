package com.michaelvescovo.android.itemreaper.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

/**
 * @author Michael Vescovo
 */

public class RotationHelper {

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
    }

    public void setPortrait() {
        mActivity.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void setLandscape() {
        mActivity.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
