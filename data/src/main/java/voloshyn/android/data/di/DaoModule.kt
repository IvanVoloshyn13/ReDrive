package voloshyn.android.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.dataSource.room.AppDatabase

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    fun providesVehiclesDao(db: AppDatabase) = db.getVehiclesDao()

    @Provides
    fun providesAccountsDao(db: AppDatabase) = db.getAccountDao()

    @Provides
    fun providesRefuelsDao(db: AppDatabase) = db.getRefuelsDao()
}