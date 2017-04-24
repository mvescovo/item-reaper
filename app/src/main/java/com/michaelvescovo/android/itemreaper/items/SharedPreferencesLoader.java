package com.michaelvescovo.android.itemreaper.items;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Michael Vescovo.
 */

class SharedPreferencesLoader extends AsyncTaskLoader<SharedPreferences> {

    private SharedPreferences mPreferences;

    SharedPreferencesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mPreferences != null) {
            deliverResult(mPreferences);
        } else {
            forceLoad();
        }
    }

    @Override
    public SharedPreferences loadInBackground() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public void deliverResult(SharedPreferences data) {
        mPreferences = data;
        super.deliverResult(data);
    }
}
