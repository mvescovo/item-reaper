package com.michaelvescovo.android.itemreaper.data;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Michael Vescovo
 */

@Singleton
@Component(modules = DataSourceModule.class)
public interface RepositoryComponent {

    Repository getRepository();
}
