package com.michaelvescovo.android.itemreaper.items;

import com.michaelvescovo.android.itemreaper.ApplicationComponent;
import com.michaelvescovo.android.itemreaper.data.RepositoryComponent;
import com.michaelvescovo.android.itemreaper.util.FragmentScoped;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@FragmentScoped
@Component(modules = ItemsModule.class, dependencies = {RepositoryComponent.class,
        ApplicationComponent.class})
interface ItemsComponent {

    void inject(ItemsActivity activity);
}
