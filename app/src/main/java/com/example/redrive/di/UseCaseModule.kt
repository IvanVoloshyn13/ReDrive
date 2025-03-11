package com.example.redrive.di

import com.example.domain.repository.EmailAuthRepository
import com.example.domain.repository.UserSessionRepository
import com.example.domain.useCase.userSession.GetUserInitialsUseCase
import com.example.domain.useCase.userSession.IsUserSignedInUseCase
import com.example.domain.useCase.SignInWithEmailUseCase
import com.example.domain.useCase.userSession.SignOutUseCase
import com.example.domain.useCase.SignUpWithEmailUseCase
import com.example.domain.useCase.userSession.ObserveCurrentUserIdUseCase
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

    @Provides
    @ViewModelScoped
    fun provideSignUpWithEmailUseCase(repository: EmailAuthRepository): SignUpWithEmailUseCase {
        return SignUpWithEmailUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideIsUserSignedInUseCase(repository: UserSessionRepository): IsUserSignedInUseCase {
        return IsUserSignedInUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun getUserInitialsUseCase(repository: UserSessionRepository) =
        GetUserInitialsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSignOutUseCae(repository: UserSessionRepository) = SignOutUseCase(repository)


}