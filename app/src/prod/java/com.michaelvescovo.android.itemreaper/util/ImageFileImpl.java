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
 */

package com.michaelvescovo.android.itemreaper.util;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.FileProvider;

import com.michaelvescovo.android.itemreaper.util.ImageFile;

import java.io.File;
import java.io.IOException;

/**
 * A thin wrapper around Android file APIs to make them more testable and allows the injection of a
 * fake implementation for hermetic UI tests.
 */
public class ImageFileImpl implements ImageFile {

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists() {
        return null != mImageFile && mImageFile.exists();
    }

    @Override
    public void delete() {
        mImageFile = null;
    }

    @Override
    public Uri getUri() {
        return FileProvider.getUriForFile(mContext,
                mContext.getPackageName() + ".fileprovider", mImageFile);
    }

    @Override
    public String getPath() {
        return mImageFile.getAbsolutePath();
    }
}
