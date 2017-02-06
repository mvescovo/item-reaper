package com.michaelvescovo.android.itemreaper.items;

import com.michaelvescovo.android.itemreaper.ApplicationComponent;
import com.michaelvescovo.android.itemreaper.data.RepositoryComponent;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@Component(modules = ItemsModule.class, dependencies = {RepositoryComponent.class,
        ApplicationComponent.class})
interface ItemsComponent {

    ItemsPresenter getItemsPresenter();
}
