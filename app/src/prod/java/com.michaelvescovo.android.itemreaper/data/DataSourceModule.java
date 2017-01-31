package com.michaelvescovo.android.itemreaper.data;

import dagger.Module;
import dagger.Provides;

/**
 * @author Michael Vescovo
 */

@Module
class DataSourceModule {

    @Provides
    static DataSource provideDataSource() {
        return new RemoteDataSource();
    }
}
