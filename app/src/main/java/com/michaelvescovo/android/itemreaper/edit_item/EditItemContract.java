package com.michaelvescovo.android.itemreaper.edit_item;

import android.support.annotation.NonNull;

import com.michaelvescovo.android.itemreaper.data.Item;

/**
 * @author Michael Vescovo
 */

interface EditItemContract {

    interface View {

        void setPresenter(Presenter presenter);

        void setProgressBar(boolean visible);

    }

    interface Presenter {

        void saveItem(@NonNull String userId, @NonNull Item item);
    }
}
