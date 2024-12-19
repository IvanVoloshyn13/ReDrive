package voloshyn.android.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.localeStorage.room.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    @Singleton
    fun providesVehiclesDao(db: AppDatabase) = db.getVehiclesDao()

    @Provides
    @Singleton
    fun providesAccountsDao(db: AppDatabase) = db.getAccountDao()

    @Provides
    @Singleton
    fun providesRefuelsDao(db: AppDatabase) = db.getRefuelsDao()

    @Provides
    @Singleton
    fun providesSettingsDao(db: AppDatabase) = db.getSettingsDao()

}