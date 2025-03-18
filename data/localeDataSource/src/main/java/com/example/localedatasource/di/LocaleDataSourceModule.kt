package com.example.localedatasource.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.room.Room
import com.example.localedatasource.room.AppDatabase
import com.example.localedatasource.room.UsersDao
import com.example.localedatasource.room.VehiclesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

internal const val PREFERENCE_DATA_STORE_FILE_NAME = "datastore.preferences_pb"
internal const val APP_DATABASE = "redrive.db"

@Module
@InstallIn(SingletonComponent::class)
class LocaleDataSourceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE).build()
    }

    @Provides
    @Singleton
    fun provideDataStorePreferences(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.dataStoreFile(PREFERENCE_DATA_STORE_FILE_NAME) }
        )
    }

    //Dao's
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

}