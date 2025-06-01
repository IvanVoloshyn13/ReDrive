package com.example.data.di

import com.example.data.mappers.UnitPreferencesMapper
import com.example.data.mappers.UnitPreferencesMapperImpl
import com.example.data.repository.EmailAuthRepositoryImpl
import com.example.data.repository.VehicleStatsRepositoryImpl
import com.example.data.repository.RefuelRepositoryImpl
import com.example.data.repository.VehicleUnitPreferencesRepositoryImpl
import com.example.data.repository.UserSessionRepositoryImpl
import com.example.data.repository.VehiclesRepositoryImpl
import com.example.data.repository.sync.UnitsPrefSendDataStatusCheckerImpl
import com.example.data.repository.sync.VehiclesSendDataStatusCheckerImpl
import com.example.domain.repository.EmailAuthRepository
import com.example.domain.repository.VehicleStatsRepository
import com.example.domain.repository.RefuelRepository
import com.example.domain.repository.VehicleUnitPreferencesRepository
import com.example.domain.repository.UserSessionRepository
import com.example.domain.repository.VehiclesRepository
import com.example.domain.sync.UnitPreferencesSyncChecker
import com.example.domain.sync.SendDataStatusChecker
import com.example.domain.sync.VehiclesSyncChecker
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
    fun bindEmailAuthRepository(impl: EmailAuthRepositoryImpl): EmailAuthRepository

    @Binds
    @Singleton
    fun bindUserSessionRepository(impl: UserSessionRepositoryImpl): UserSessionRepository

    @Binds
    @Singleton
    fun bindVehiclesRepository(impl: VehiclesRepositoryImpl): VehiclesRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(impl: VehicleUnitPreferencesRepositoryImpl): VehicleUnitPreferencesRepository

    @Binds
    @Singleton
    fun bindRefuelRepository(impl: RefuelRepositoryImpl): RefuelRepository

    @Binds
    @Singleton
    fun bindSettingsMapperRepository(impl: UnitPreferencesMapperImpl): UnitPreferencesMapper

    @Binds
    @Singleton
    fun bindOverviewRepository(impl: VehicleStatsRepositoryImpl): VehicleStatsRepository

    @Binds
    @Singleton
    @VehiclesSyncChecker
    fun bindVehiclesSyncStatusChecker(impl: VehiclesSendDataStatusCheckerImpl): SendDataStatusChecker

    @Binds
    @Singleton
    @UnitPreferencesSyncChecker
    fun bindSettingsSyncStatusChecker(impl: UnitsPrefSendDataStatusCheckerImpl): SendDataStatusChecker
}

