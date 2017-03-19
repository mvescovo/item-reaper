package com.michaelvescovo.android.itemreaper.itemDetails;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelvescovo.android.itemreaper.data.Item;

/**
 * @author Michael Vescovo
 */

interface ItemDetailsContract {

    interface View {

        void setPresenter(Presenter presenter);

        void showEditItemUi();

        void showNoItemsText(boolean active);

        void showItemExpiredMessage(int resourceId, int duration, @Nullable Item item);
    }

    interface Presenter {

        void openEditItem();

        void expireItem(@NonNull Item item, int itemsSize);

        void unexpireItem(@NonNull Item item);
    }
}
