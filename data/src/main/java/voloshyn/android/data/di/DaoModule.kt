package voloshyn.android.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.dataSource.room.AppDatabase

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    fun bindVehiclesDao(db: AppDatabase) = db.getVehiclesDao()

    @Provides
    fun bindAccountsDao(db: AppDatabase) = db.getAccountDao()
}