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

import java.io.File;
import java.io.Serializable;

/**
 * A thin wrapper around Android file APIs to make them more testable and allows the injection of a
 * fake implementation for hermetic UI tests.
 */
public class ImageFileImpl implements ImageFile, Serializable {

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    File mImageFile;

    @Override
    public void create(Context context, String name, String extension) {
        mImageFile = new File(context.getFilesDir(), name);
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
    public Uri getUri(Context context) {
        return FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider", mImageFile);
    }

    @Override
    public String getPath() {
        return mImageFile.getAbsolutePath();
    }
}
