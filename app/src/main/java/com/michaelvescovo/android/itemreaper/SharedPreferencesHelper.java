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

public class SharedPreferencesHelper {

    static final String KEY_USER_ID = "user_id";
    static final String KEY_DELETED_ITEM_ID = "deleted_item_id";
    private final SharedPreferences mSharedPreferences;

    SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public boolean saveUserId(String userId){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        return editor.commit();
    }

    public String getUserId() {
        return mSharedPreferences.getString(KEY_USER_ID, "unknown_user");
    }

    public boolean saveDeletedItemId(String itemId){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_DELETED_ITEM_ID, itemId);
        return editor.commit();
    }

    public String getDeletedItemId() {
        return mSharedPreferences.getString(KEY_DELETED_ITEM_ID, "unknown_item_id");
    }

    public void removeDeletedItemId() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(KEY_DELETED_ITEM_ID);
        editor.apply();
    }
}
