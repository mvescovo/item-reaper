package com.michaelvescovo.android.itemreaper.item_details;

import com.michaelvescovo.android.itemreaper.data.RepositoryComponent;
import com.michaelvescovo.android.itemreaper.util.FragmentScoped;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@FragmentScoped
@Component(modules = ItemDetailsModule.class, dependencies = {RepositoryComponent.class})
public interface ItemDetailsComponent {

    ItemDetailsPresenter getItemDetailsPresenter();
}
