package voloshyn.android.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.data.repository.AndroidEmailValidatorImpl
import voloshyn.android.data.repository.user.EmailAuthRepositoryImpl
import voloshyn.android.data.repository.OnBoardRepositoryImpl
import voloshyn.android.data.repository.RefuelsProvider
import voloshyn.android.data.repository.logs.RefuelLogsRepositoryImpl
import voloshyn.android.data.repository.refuel.RefuelRepositoryImpl
import voloshyn.android.domain.repository.EmailValidatorRepository
import voloshyn.android.domain.repository.OnBoardRepository
import voloshyn.android.domain.repository.RefuelLogsRepository
import voloshyn.android.domain.repository.userAuth.EmailAuthRepository

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    // USER_SESSION_AUTH

    @Binds
    @ViewModelScoped
    fun bindEmailValidatorRepository(repository: AndroidEmailValidatorImpl): EmailValidatorRepository

    @Binds
    @ViewModelScoped
    fun bindEmailAuthRepository(repository: EmailAuthRepositoryImpl): EmailAuthRepository

    // USER_SESSION_AUTH

    @Binds
    @ViewModelScoped
    fun bindRefuelsProvider(repository: RefuelRepositoryImpl): RefuelsProvider

    @Binds
    @ViewModelScoped
    fun bindOnBoardRepository(repository: OnBoardRepositoryImpl): OnBoardRepository

    @Binds
    @ViewModelScoped
    fun bindRefuelLogsRepository(repository: RefuelLogsRepositoryImpl): RefuelLogsRepository

}