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

        void addItem(Item item);

        void changeItem(Item item);

        void removeItem(Item item);

        void moveItem();

        void showItemDetailsUi(String itemId);

        void showEditItemUi();

        void showAboutUi();

        void showAuthUi();

        void showNoItemsText(boolean active);

        void showItemExpiredMessage(int resourceId, int duration, @Nullable Item item);

        void showSignIn();
    }

    interface Presenter {

        void checkItemsExit(int sortBy);

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
