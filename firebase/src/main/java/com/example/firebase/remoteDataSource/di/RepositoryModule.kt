package com.example.firebase.remoteDataSource.di

import com.example.firebase.remoteDataSource.realtimeDatabase.unitPreferences.RemoteUnitsPrefSource
import com.example.firebase.remoteDataSource.realtimeDatabase.unitPreferences.RemoteUnitsPrefSourceImpl
import com.example.firebase.remoteDataSource.realtimeDatabase.vehicles.RemoteVehicleSource
import com.example.firebase.remoteDataSource.realtimeDatabase.vehicles.RemoteVehicleSourceImpl
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

}