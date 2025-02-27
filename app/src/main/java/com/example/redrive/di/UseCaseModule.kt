package com.example.redrive.di

import com.example.domain.repository.EmailAuthRepository
import com.example.domain.useCase.SignInWithEmailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideSignInWithEmailUseCase(repository: EmailAuthRepository): SignInWithEmailUseCase {
        return SignInWithEmailUseCase(repository)
    }
}