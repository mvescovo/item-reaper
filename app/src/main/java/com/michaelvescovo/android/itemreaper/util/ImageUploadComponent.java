package com.michaelvescovo.android.itemreaper.util;

import com.michaelvescovo.android.itemreaper.ApplicationComponent;
import com.michaelvescovo.android.itemreaper.data.RepositoryComponent;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@FragmentScoped
@Component(dependencies = {RepositoryComponent.class, ApplicationComponent.class})
interface ImageUploadComponent {

    void inject(ImageUploadService imageUploadService);
}
