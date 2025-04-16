package com.example.localedatasource.di

import com.example.localedatasource.room.AppDatabase
import com.example.localedatasource.room.daos.UsersDao
import com.example.localedatasource.room.daos.VehiclesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    @Singleton
    fun provideUsersDao(db: AppDatabase): UsersDao {
        return db.getUsersDao()
    }

    @Provides
    @Singleton
    fun provideVehiclesDao(db: AppDatabase): VehiclesDao {
        return db.getVehiclesDao()
    }

    @Provides
    @Singleton
    fun provideSettingsDao(db: AppDatabase) = db.getSettingsDao()

    @Provides
    @Singleton
    fun provideRefuelsDao(db: AppDatabase) = db.getRefuelDao()
}