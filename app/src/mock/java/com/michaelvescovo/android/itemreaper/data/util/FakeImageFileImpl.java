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
 * Modifications have been made.
 */

package com.michaelvescovo.android.itemreaper.data.util;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.FileProvider;

import com.michaelvescovo.android.itemreaper.util.ImageFile;

import java.io.File;
import java.io.IOException;

/**
 * Fake implementation of {@link ImageFile} to inject a fake image in a hermetic test.
 */
public class FakeImageFileImpl implements ImageFile {

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    File mImageFile;
    private Context mContext;

    @Override
    public void create(Context context, String name, String extension) {
        mContext = context;
        File storageDir = context.getFilesDir();
        try {
            mImageFile = File.createTempFile(
                    name,  /* prefix */
                    extension,        /* suffix */
                    storageDir      /* directory */
            );
            mImageFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPath() {
        return "file:///android_asset/black-t-shirt.jpg";
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public void delete() {
        // Do nothing
    }

    @Override
    public Uri getUri() {
        return FileProvider.getUriForFile(mContext,
                mContext.getPackageName() + ".fileprovider", mImageFile);
    }
}