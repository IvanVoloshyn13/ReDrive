package voloshyn.android.redrive.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.repository.user.UserSessionRepositoryImpl
import voloshyn.android.domain.repository.AppSettingsRepository
import voloshyn.android.domain.useCase.settings.GetSettingItemUnitsUseCase
import voloshyn.android.domain.useCase.settings.ObserveSettingsUseCase
import voloshyn.android.domain.useCase.user.GetCurrentUserUseCase
import voloshyn.android.domain.useCase.user.ObserveCurrentUserUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonUseCaseModule {
    @Provides
    @Singleton
    fun provideObserveCurrentUserUseCase(repository: UserSessionRepositoryImpl): ObserveCurrentUserUseCase {
        return ObserveCurrentUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(repository: UserSessionRepositoryImpl): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideObserveSettingsUseCase(repository: AppSettingsRepository): ObserveSettingsUseCase {
        return ObserveSettingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUnitsUseCase(repository: AppSettingsRepository): GetSettingItemUnitsUseCase {
        return GetSettingItemUnitsUseCase(repository)
    }
}

