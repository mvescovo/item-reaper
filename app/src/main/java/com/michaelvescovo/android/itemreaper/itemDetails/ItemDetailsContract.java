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

        void showItemExpiredMessage(int resourceId, int duration, @Nullable Item item);

        void showItem(@NonNull Item item);

        void showExpireMenuButton(boolean visible);
    }

    interface Presenter {

        void displayItem(@NonNull String itemId);

        void openEditItem();

        void expireItem(@NonNull Item item);

        void unexpireItem(@NonNull Item item);
    }
}
