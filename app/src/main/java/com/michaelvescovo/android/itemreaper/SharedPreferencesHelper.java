/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications have been made by Michael Vescovo.
 */

package com.michaelvescovo.android.itemreaper;

import android.content.SharedPreferences;

import static com.michaelvescovo.android.itemreaper.items.ItemsFragment.STATE_CURRENT_SORT;
import static com.michaelvescovo.android.itemreaper.items.SortItemsDialogFragment.SORT_BY_EXPIRY;

public class SharedPreferencesHelper {

    private static final String KEY_IMAGE_UPLOADING = "image_uploading_";
    private final SharedPreferences mSharedPreferences;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public boolean saveSortBy(int sortBy) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(STATE_CURRENT_SORT, sortBy);
        return editor.commit();
    }

    public int getSortBy() {
        return mSharedPreferences.getInt(STATE_CURRENT_SORT, SORT_BY_EXPIRY);
    }

    public boolean imageUploading(String itemId) {
        return mSharedPreferences.getBoolean(KEY_IMAGE_UPLOADING + itemId, false);
    }

    public void setImageUploading(String itemId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(KEY_IMAGE_UPLOADING + itemId, true);
        editor.apply();
    }

    public void removeImageUploading(String itemId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(KEY_IMAGE_UPLOADING + itemId);
        editor.apply();
    }
}
