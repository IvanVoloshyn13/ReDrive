package com.example.data.di

import com.example.firebase.FirebaseAuthRepository
import com.example.firebase.FirebaseAuthRepositoryImpl
import com.example.data.repository.EmailAuthRepositoryImpl
import com.example.data.repository.SettingsRepositoryImpl
import com.example.data.repository.UserSessionRepositoryImpl
import com.example.data.repository.VehiclesRepositoryImpl
import com.example.domain.repository.EmailAuthRepository
import com.example.domain.repository.SettingsRepository
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
    fun bindEmailAuthRepository(repository: EmailAuthRepositoryImpl): EmailAuthRepository

    @Binds
    @Singleton
    fun bindUserSessionRepository(repository: UserSessionRepositoryImpl): UserSessionRepository

    @Binds
    @Singleton
    fun bindVehiclesRepository(repository: VehiclesRepositoryImpl): VehiclesRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository
}