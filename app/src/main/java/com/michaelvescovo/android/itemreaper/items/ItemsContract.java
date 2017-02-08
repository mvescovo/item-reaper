package com.michaelvescovo.android.itemreaper.items;

import com.michaelvescovo.android.itemreaper.data.Item;

import java.util.Map;

/**
 * @author Michael Vescovo
 */

public class ItemsContract {

    interface View {

        void setPresenter(Presenter presenter);

        void setProgressBar(boolean visible);

        void showItems(Map<String, Item> items);

        void showItemDetailsUi(String itemId);

        void showAddItemUi();

        void showAboutUi();

        void showAuthUi();
    }

    interface Presenter {

        void getItems();

        void openItemDetails(Item item);

        void openAddItem();

        void openAbout();

        void openSignOut();
    }

}
