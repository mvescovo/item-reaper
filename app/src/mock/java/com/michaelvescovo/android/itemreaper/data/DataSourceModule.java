package com.michaelvescovo.android.itemreaper.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Michael Vescovo
 */

@Module
class DataSourceModule {

    @Singleton
    @Provides
    DataSource provideDataSource() {
        return new FakeDataSource();
    }
}
