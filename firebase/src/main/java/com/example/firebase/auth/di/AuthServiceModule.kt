package com.example.firebase.auth.di

import com.example.firebase.auth.FirebaseAuthService
import com.example.firebase.auth.FirebaseAuthServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthServiceModule {
    @Binds
    @Singleton
    fun bindFirebaseAuthService(impl: FirebaseAuthServiceImpl): FirebaseAuthService
}