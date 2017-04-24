///*
// * Copyright 2015, The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// * Modifications have been made by Michael Vescovo.
// */
//
//package com.michaelvescovo.android.itemreaper;
//
//import android.content.SharedPreferences;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Matchers.eq;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SharedPreferencesHelperTest {
//
//    private static final String USER_ID = "testUser";
//    private SharedPreferencesHelper mMockSharedPreferencesHelper;
//    private SharedPreferencesHelper mMockBrokenSharedPreferencesHelper;
//
//    @Mock
//    SharedPreferences mMockSharedPreferences;
//
//    @Mock
//    SharedPreferences mMockBrokenSharedPreferences;
//
//    @Mock
//    SharedPreferences.Editor mMockEditor;
//
//    @Mock
//    SharedPreferences.Editor mMockBrokenEditor;
//
//    @Before
//    public void initMocks() {
//        mMockSharedPreferencesHelper = createMockSharedPreference();
//        mMockBrokenSharedPreferencesHelper = createBrokenMockSharedPreference();
//    }
//
//    @Test
//    public void sharedPreferencesHelper_SaveAndReadUserId() {
//        boolean success = mMockSharedPreferencesHelper.saveUserId(USER_ID);
//        assertThat("Checking that SharedPreferenceEntry.save... returns true",
//                success, is(true));
//
//        String userId = mMockSharedPreferencesHelper.getUserId();
//        assertThat("Checking that SharedPreferenceEntry.name has been persisted and read correctly",
//                USER_ID, is(equalTo(userId)));
//    }
//
//    @Test
//    public void sharedPreferencesHelper_SaveUserIdFailed_ReturnsFalse() {
//        boolean success = mMockBrokenSharedPreferencesHelper.saveUserId(USER_ID);
//        assertThat("Makes sure writing to a broken SharedPreferencesHelper returns false", success,
//                is(false));
//    }
//
//    private SharedPreferencesHelper createMockSharedPreference() {
//        when(mMockSharedPreferences.getString(eq(SharedPreferencesHelper.KEY_USER_ID),
//                anyString())).thenReturn(USER_ID);
//        when(mMockEditor.commit()).thenReturn(true);
//        when(mMockSharedPreferences.edit()).thenReturn(mMockEditor);
//        return new SharedPreferencesHelper(mMockSharedPreferences);
//    }
//
//    private SharedPreferencesHelper createBrokenMockSharedPreference() {
//        when(mMockBrokenEditor.commit()).thenReturn(false);
//        when(mMockBrokenSharedPreferences.edit()).thenReturn(mMockBrokenEditor);
//        return new SharedPreferencesHelper(mMockBrokenSharedPreferences);
//    }
//}
