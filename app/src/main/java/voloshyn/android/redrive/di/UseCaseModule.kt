package voloshyn.android.redrive.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.data.repository.tabs.refuel.RefuelRepositoryImpl
import voloshyn.android.domain.repository.EmailValidatorRepository
import voloshyn.android.domain.repository.OnBoardRepository
import voloshyn.android.domain.repository.account.EmailAuthRepository
import voloshyn.android.domain.repository.account.UserSessionRepository
import voloshyn.android.domain.repository.tabs.RefuelLogsRepository
import voloshyn.android.domain.useCase.auth.IsUserSignInUseCase
import voloshyn.android.domain.useCase.auth.SignInWithEmailUseCase
import voloshyn.android.domain.useCase.auth.SignUpWithEmailUseCase
import voloshyn.android.domain.useCase.auth.ValidateEmailUseCase
import voloshyn.android.domain.useCase.onBoard.OnBoardFinishUseCase
import voloshyn.android.domain.useCase.onBoard.OnBoardIsFinishedUseCase
import voloshyn.android.domain.useCase.tabs.logs.GetLogsUseCase
import voloshyn.android.domain.useCase.tabs.refuel.AddNewRefuelUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {


    // USER_SESSION_AUTH
    @Provides
    @ViewModelScoped
    fun provideIsSignedInUseCase(repository: UserSessionRepository): IsUserSignInUseCase {
        return IsUserSignInUseCase(repository)
    }

    @Provides
    fun provideEmailPasswordSignUpUseCase(repository: EmailAuthRepository): SignUpWithEmailUseCase {
        return SignUpWithEmailUseCase(repository)
    }

    @Provides
    fun provideEmailPasswordSignInUseCase(repository: EmailAuthRepository): SignInWithEmailUseCase {
        return SignInWithEmailUseCase(repository)
    }

    @Provides
    fun provideEmailValidatorUseCase(repository: EmailValidatorRepository): ValidateEmailUseCase {
        return ValidateEmailUseCase(repository)
    }
    // USER_SESSION_AUTH


    // ON_BOARD
    @Provides
    @ViewModelScoped
    fun provideOnBoardIsFinishedUseCase(repository: OnBoardRepository): OnBoardIsFinishedUseCase {
        return OnBoardIsFinishedUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideOnBoardFinishUseCase(repository: OnBoardRepository): OnBoardFinishUseCase {
        return OnBoardFinishUseCase(repository)
    }
    // ON_BOARD


    @Provides
    fun provideAddNewRefuelUseCase(repository: RefuelRepositoryImpl): AddNewRefuelUseCase {
        return AddNewRefuelUseCase(repository)
    }

    @Provides
    fun provideGetRefuelLogsUseCase(repository: RefuelLogsRepository): GetLogsUseCase {
        return GetLogsUseCase(repository)
    }
    

}