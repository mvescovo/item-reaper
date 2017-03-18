package com.michaelvescovo.android.itemreaper.itemDetails;

/**
 * @author Michael Vescovo
 */

interface ItemDetailsContract {

    interface View {

        void setPresenter(Presenter presenter);

        void showEditItemUi();
    }

    interface Presenter {

        void openEditItem();
    }
}
