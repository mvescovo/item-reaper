package com.michaelvescovo.android.itemreaper.edit_item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelvescovo.android.itemreaper.data.Item;

/**
 * @author Michael Vescovo
 */

interface EditItemContract {

    interface View {

        void setPresenter(Presenter presenter);

        void setProgressBar(boolean active);

        void showExistingItem(Item item);

        void showItemsUi();

    }

    interface Presenter {

        void editItem(@Nullable String itemId);

        void saveItem(@NonNull Item item);

        void doneEditing();
    }
}
