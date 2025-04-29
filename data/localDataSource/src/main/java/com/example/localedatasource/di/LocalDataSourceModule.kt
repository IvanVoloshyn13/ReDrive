package com.example.localedatasource.di

import com.example.localedatasource.appPreferencesFromAssets.FromAssetsUnitPreferencesDataSource
import com.example.localedatasource.appPreferencesFromAssets.FromAssetsUnitPreferencesDataSourceImpl
import com.example.localedatasource.dataStore.AppUserPreferences
import com.example.localedatasource.dataStore.AppUserPreferencesImpl
import com.example.localedatasource.dataStore.AppVehiclePreferences
import com.example.localedatasource.dataStore.AppVehiclePreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LocalDataSourceModule {
    @Binds
    fun bindAppUserPreferences(impl: AppUserPreferencesImpl): AppUserPreferences

    @Binds
    fun bindAppVehiclesPreferences(impl: AppVehiclePreferencesImpl): AppVehiclePreferences

    @Binds
    fun bindAppSettingsFromAssets(impl: FromAssetsUnitPreferencesDataSourceImpl): FromAssetsUnitPreferencesDataSource

}