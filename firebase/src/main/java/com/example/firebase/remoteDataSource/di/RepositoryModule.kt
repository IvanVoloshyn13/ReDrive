package com.example.firebase.remoteDataSource.di

import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteRefuelSource
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteRefuelSourceImpl
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteUnitsPrefSource
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteUnitsPrefSourceImpl
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteVehicleSource
import com.example.firebase.remoteDataSource.realtimeDatabase.RemoteVehicleSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindRemoteVehicleSource(impl: RemoteVehicleSourceImpl): RemoteVehicleSource

    @Binds
    @Singleton
    fun bindSettingsSource(impl: RemoteUnitsPrefSourceImpl): RemoteUnitsPrefSource

    @Binds
    @Singleton
    fun bindRefuelSource(impl: RemoteRefuelSourceImpl): RemoteRefuelSource

}