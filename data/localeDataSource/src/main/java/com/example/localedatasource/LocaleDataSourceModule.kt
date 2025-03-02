package com.example.localedatasource

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

internal const val APP_DATABASE = "redrive.db"

@Module
@InstallIn(SingletonComponent::class)
class LocaleDataSourceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE).build()
    }

    //Dao's
    @Provides
    @Singleton
    fun provideUsersDao(db: AppDatabase): UsersDao {
        return db.getUsersDao()
    }

}