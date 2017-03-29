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

package com.michaelvescovo.android.itemreaper.util;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Fake implementation of {@link ImageFile} to inject a fake image in a hermetic test.
 */
public class FakeImageFileImpl implements ImageFile, Serializable {

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    File mImageFile;

    @Override
    public void create(Context context, String name, String extension) {
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

        // For the fake image file, copy the sample image in assets to the file.
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = context.getAssets().open("black-t-shirt.jpg");
            outputStream = new FileOutputStream(mImageFile);
            copyFile(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignore) {}
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignore) {}
            }
        }
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, read);
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
    public void delete(Context context) {
        // Do nothing
    }

    @Override
    public Uri getUri(Context context) {
        return FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider", mImageFile);
    }
}
