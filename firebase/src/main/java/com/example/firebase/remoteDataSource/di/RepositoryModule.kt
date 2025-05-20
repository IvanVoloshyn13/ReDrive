package com.example.firebase.remoteDataSource.di

import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteVehicleSource
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteVehicleSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindRemoteStorageRepository(impl: RemoteVehicleSourceImpl): RemoteVehicleSource
}