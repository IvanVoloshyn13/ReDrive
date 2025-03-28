package com.example.redrive.di

import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.AppStringResProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppStringResProvider(
        impl: AppStringResProviderImpl
    ): AppStringResProvider
}