package com.example.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Named
import javax.inject.Singleton

const val LOCALE_LANGUAGE = "locale_language"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    @Named(LOCALE_LANGUAGE)
    fun provideAppLanguage(): String = Locale.getDefault().language
}