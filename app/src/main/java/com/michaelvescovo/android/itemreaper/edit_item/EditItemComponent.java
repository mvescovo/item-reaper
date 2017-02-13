package com.michaelvescovo.android.itemreaper.edit_item;

import com.michaelvescovo.android.itemreaper.ApplicationComponent;
import com.michaelvescovo.android.itemreaper.data.RepositoryComponent;
import com.michaelvescovo.android.itemreaper.util.FragmentScoped;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@FragmentScoped
@Component(modules = EditItemModule.class, dependencies = {RepositoryComponent.class,
        ApplicationComponent.class})
interface EditItemComponent {

    EditItemPresenter getEditItemPresenter();
}
