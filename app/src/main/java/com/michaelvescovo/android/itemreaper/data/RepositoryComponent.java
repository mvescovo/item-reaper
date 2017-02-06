package com.michaelvescovo.android.itemreaper.data;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@Singleton
public @Component(modules = DataSourceModule.class)
interface RepositoryComponent {

    Repository getRepository();
}
