package com.michaelvescovo.android.itemreaper.items;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.michaelvescovo.android.itemreaper.data.Item;

/**
 * @author Michael Vescovo
 */

interface ItemsContract {

    interface View {

        void setPresenter(Presenter presenter);

        void setProgressBar(boolean active);

        void showItem(Item item);

        void showItemDetailsUi(String itemId);

        void showEditItemUi();

        void showAboutUi();

        void showAuthUi();

        void showNoItemsText(boolean active);

        void showItemExpiredMessage(int resourceId, int duration, @Nullable Item item);

        void clearItems();

        void itemLoadingFinished();
    }

    interface Presenter {

        void getItems(boolean forceUpdate);

        void openItemDetails(String itemId);

        void openAddItem();

        void openAbout();

        void openSignOut();

        void restoreItem(@NonNull Item item);

        void expireItem(@NonNull Item item);

        void unexpireItem(@NonNull Item item);

        void itemsSizeChanged(int newSize);
    }
}
