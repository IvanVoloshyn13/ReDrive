package voloshyn.android.redrive.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.data.repository.refuel.RefuelRepositoryImpl
import voloshyn.android.domain.repository.AppSettingsRepository
import voloshyn.android.domain.repository.OnBoardRepository
import voloshyn.android.domain.repository.RefuelLogsRepository
import voloshyn.android.domain.repository.userAuth.EmailAuthRepository
import voloshyn.android.domain.repository.userAuth.UserSessionRepository
import voloshyn.android.domain.useCase.sign_in.SignInWithEmailUseCase
import voloshyn.android.domain.useCase.sign_up.SignUpWithEmailUseCase
import voloshyn.android.domain.useCase.onBoard.FinishOnBoardUseCase
import voloshyn.android.domain.useCase.onBoard.OnBoardIsFinishedUseCase
import voloshyn.android.domain.useCase.settings.SaveSettingsUseCase
import voloshyn.android.domain.useCase.logs.GetLogsUseCase
import voloshyn.android.domain.useCase.refuel.AddNewRefuelUseCase
import voloshyn.android.domain.useCase.sign_in.IsSignedInUseCase
import voloshyn.android.domain.useCase.user.SignOutUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    // USER_SESSION_AUTH
    @Provides
    @ViewModelScoped
    fun provideIsSignedInUseCase(repository: UserSessionRepository): IsSignedInUseCase {
        return IsSignedInUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideEmailPasswordSignUpUseCase(repository: EmailAuthRepository): SignUpWithEmailUseCase {
        return SignUpWithEmailUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideEmailPasswordSignInUseCase(repository: EmailAuthRepository): SignInWithEmailUseCase {
        return SignInWithEmailUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideUserSignOutUseCase(repository: UserSessionRepository): SignOutUseCase {
        return SignOutUseCase(repository)
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
    fun provideOnBoardFinishUseCase(repository: OnBoardRepository): FinishOnBoardUseCase {
        return FinishOnBoardUseCase(repository)
    }
    // ON_BOARD


    @Provides
    @ViewModelScoped
    fun provideAddNewRefuelUseCase(repository: RefuelRepositoryImpl): AddNewRefuelUseCase {
        return AddNewRefuelUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRefuelLogsUseCase(repository: RefuelLogsRepository): GetLogsUseCase {
        return GetLogsUseCase(repository)
    }


    @Provides
    @ViewModelScoped
    fun provideSaveSettingsUseCase(repository: AppSettingsRepository): SaveSettingsUseCase {
        return SaveSettingsUseCase(repository)
    }


}