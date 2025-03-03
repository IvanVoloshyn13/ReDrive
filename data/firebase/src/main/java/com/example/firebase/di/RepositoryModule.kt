package com.example.firebase.di

import com.example.firebase.FirebaseAuthRepository
import com.example.firebase.FirebaseAuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindFirebaseAuthRepository(impl: FirebaseAuthRepositoryImpl): FirebaseAuthRepository
}