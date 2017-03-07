package com.michaelvescovo.android.itemreaper.items;

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
    }

    interface Presenter {

        void getItems(boolean forceUpdate);

        void openItemDetails(Item item);

        void openAddItem();

        void openAbout();

        void openSignOut();
    }
}
