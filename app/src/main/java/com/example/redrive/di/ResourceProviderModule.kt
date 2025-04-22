package com.example.redrive.di

import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.AppStringResProviderImpl
import com.example.redrive.core.logTextFormatter.RefuelMessageFromResProvider
import com.example.redrive.core.logTextFormatter.RefuelMessageFromResProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ResourceProviderModule {

    @Binds
    @Singleton
     fun bindAppStringResProvider(
        impl: AppStringResProviderImpl
    ): AppStringResProvider


    @Binds
    @Singleton
     fun bindLogMessageProvider(
        impl: RefuelMessageFromResProviderImpl
    ): RefuelMessageFromResProvider

}