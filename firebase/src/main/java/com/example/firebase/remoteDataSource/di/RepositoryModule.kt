package com.example.firebase.remoteDataSource.di

import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteDataStorage
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteDataStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindRemoteStorageRepository(impl: RemoteDataStorageImpl): RemoteDataStorage
}