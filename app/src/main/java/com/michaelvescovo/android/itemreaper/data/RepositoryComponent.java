package com.michaelvescovo.android.itemreaper.data;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

public @Component(modules = DataSourceModule.class)
interface RepositoryComponent {

    Repository getRepository();
}
