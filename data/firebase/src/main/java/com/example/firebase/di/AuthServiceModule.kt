package com.example.firebase.di

import com.example.firebase.FirebaseAuthService
import com.example.firebase.FirebaseAuthServiceImpl
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