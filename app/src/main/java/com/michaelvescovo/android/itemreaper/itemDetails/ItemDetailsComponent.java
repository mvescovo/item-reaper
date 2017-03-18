package com.michaelvescovo.android.itemreaper.itemDetails;

import com.michaelvescovo.android.itemreaper.ApplicationComponent;
import com.michaelvescovo.android.itemreaper.data.RepositoryComponent;
import com.michaelvescovo.android.itemreaper.util.FragmentScoped;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@FragmentScoped
@Component(modules = ItemDetailsModule.class, dependencies = {RepositoryComponent.class,
        ApplicationComponent.class})
public interface ItemDetailsComponent {

    ItemDetailsPresenter getItemDetailsPresenter();
}
