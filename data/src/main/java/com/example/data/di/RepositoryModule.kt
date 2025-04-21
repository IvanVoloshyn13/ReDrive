package com.example.data.di

import com.example.data.mappers.UnitPreferencesMapper
import com.example.data.mappers.UnitPreferencesMapperImpl
import com.example.data.repository.EmailAuthRepositoryImpl
import com.example.data.repository.OverviewRepositoryImpl
import com.example.data.repository.RefuelRepositoryImpl
import com.example.data.repository.UnitPreferencesRepositoryImpl
import com.example.data.repository.UserSessionRepositoryImpl
import com.example.data.repository.VehiclesRepositoryImpl
import com.example.domain.repository.EmailAuthRepository
import com.example.domain.repository.OverviewRepository
import com.example.domain.repository.RefuelRepository
import com.example.domain.repository.UnitPreferencesRepository
import com.example.domain.repository.UserSessionRepository
import com.example.domain.repository.VehiclesRepository
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
    fun bindSettingsRepository(impl: UnitPreferencesRepositoryImpl): UnitPreferencesRepository

    @Binds
    @Singleton
    fun bindRefuelRepository(impl: RefuelRepositoryImpl): RefuelRepository

    @Binds
    @Singleton
    fun bindSettingsMapperRepository(impl: UnitPreferencesMapperImpl): UnitPreferencesMapper

    @Binds
    @Singleton
    fun bindOverviewRepository(impl: OverviewRepositoryImpl): OverviewRepository
}

