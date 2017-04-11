package com.michaelvescovo.android.itemreaper.widget;

import com.michaelvescovo.android.itemreaper.data.RepositoryComponent;
import com.michaelvescovo.android.itemreaper.util.FragmentScoped;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@FragmentScoped
@Component(dependencies = {RepositoryComponent.class})
interface WidgetComponent {

    void inject(WidgetListService service);
}
