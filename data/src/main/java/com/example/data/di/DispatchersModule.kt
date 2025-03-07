package com.example.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @DispatcherIo
    @Singleton
    fun provideDispatcherIo(): CoroutineDispatcher = Dispatchers.IO

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DispatcherDefault

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DispatcherIo