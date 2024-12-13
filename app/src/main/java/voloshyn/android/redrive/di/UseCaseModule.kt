package voloshyn.android.redrive.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.data.repository.UserSessionRepositoryImpl
import voloshyn.android.data.repository.tabs.refuel.RefuelRepositoryImpl
import voloshyn.android.domain.repository.AppSettingsRepository
import voloshyn.android.domain.repository.EmailValidatorRepository
import voloshyn.android.domain.repository.OnBoardRepository
import voloshyn.android.domain.repository.tabs.RefuelLogsRepository
import voloshyn.android.domain.repository.userAuth.EmailAuthRepository
import voloshyn.android.domain.repository.userAuth.UserSessionRepository
import voloshyn.android.domain.useCase.auth.SignInWithEmailUseCase
import voloshyn.android.domain.useCase.auth.SignUpWithEmailUseCase
import voloshyn.android.domain.useCase.auth.ValidateEmailUseCase
import voloshyn.android.domain.useCase.onBoard.OnBoardFinishUseCase
import voloshyn.android.domain.useCase.onBoard.OnBoardIsFinishedUseCase
import voloshyn.android.domain.useCase.settings.GetUnitsUseCase
import voloshyn.android.domain.useCase.settings.ObserveSettingsUseCase
import voloshyn.android.domain.useCase.settings.SaveSettingsUseCase
import voloshyn.android.domain.useCase.tabs.logs.GetLogsUseCase
import voloshyn.android.domain.useCase.tabs.refuel.AddNewRefuelUseCase
import voloshyn.android.domain.useCase.user.GetCurrentUserUseCase
import voloshyn.android.domain.useCase.user.IsUserSignInUseCase
import voloshyn.android.domain.useCase.user.ObserveCurrentUserUseCase
import voloshyn.android.domain.useCase.user.UserSignOutUseCase

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

    @Provides
    fun provideUserSignOutUseCase(repository: UserSessionRepository): UserSignOutUseCase {
        return UserSignOutUseCase(repository)
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

    @Provides
    fun provideObserveCurrentUserUseCase(repository: UserSessionRepositoryImpl): ObserveCurrentUserUseCase {
        return ObserveCurrentUserUseCase(repository)
    }

    @Provides
    fun provideGetCurrentUserUseCase(repository: UserSessionRepositoryImpl): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(repository)
    }

    @Provides
    fun provideGetSettingsUseCase(repository: AppSettingsRepository): ObserveSettingsUseCase {
        return ObserveSettingsUseCase(repository)
    }

    @Provides
    fun provideGetUnitsUseCase(repository: AppSettingsRepository): GetUnitsUseCase {
        return GetUnitsUseCase(repository)
    }

    @Provides
    fun provideSaveSettingsUseCase(repository: AppSettingsRepository): SaveSettingsUseCase {
        return SaveSettingsUseCase(repository)
    }


}