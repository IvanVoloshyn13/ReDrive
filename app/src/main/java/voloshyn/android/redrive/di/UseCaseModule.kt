package voloshyn.android.redrive.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.domain.repository.EmailValidatorRepository
import voloshyn.android.domain.repository.OnBoardRepository
import voloshyn.android.domain.repository.AuthRepository
import voloshyn.android.domain.repository.InitRepository
import voloshyn.android.domain.useCase.auth.RememberMeUseCase
import voloshyn.android.domain.useCase.auth.SignInUseCase
import voloshyn.android.domain.useCase.init.IsSignedInUseCase
import voloshyn.android.domain.useCase.init.OnBoardIsFinishedUseCase
import voloshyn.android.domain.useCase.onBoard.OnBoardFinishUseCase
import voloshyn.android.domain.useCase.auth.SignUpUseCase
import voloshyn.android.domain.useCase.auth.ValidateEmailUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideOnBoardIsFinishedUseCase(repository: InitRepository): OnBoardIsFinishedUseCase {
        return OnBoardIsFinishedUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideIsSignedInUseCase(repository: InitRepository): IsSignedInUseCase {
        return IsSignedInUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideRememberMeInUseCase(repository: AuthRepository): RememberMeUseCase {
        return RememberMeUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideOnBoardFinishUseCase(repository: OnBoardRepository): OnBoardFinishUseCase {
        return OnBoardFinishUseCase(repository)
    }

    @Provides
    fun provideSignUpUseCase(repository: AuthRepository): SignUpUseCase {
        return SignUpUseCase(repository)
    }

    @Provides
    fun provideSignInUseCase(repository: AuthRepository): SignInUseCase {
        return SignInUseCase(repository)
    }

    @Provides
    fun provideEmailValidatorUseCase(repository: EmailValidatorRepository): ValidateEmailUseCase {
        return ValidateEmailUseCase(repository)
    }
}