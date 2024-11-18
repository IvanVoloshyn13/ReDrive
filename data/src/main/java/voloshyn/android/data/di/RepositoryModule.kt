package voloshyn.android.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.data.firebase.FirebaseAuthManager
import voloshyn.android.data.firebase.FirebaseAuthManagerImpl
import voloshyn.android.data.repository.AndroidEmailValidatorImpl
import voloshyn.android.data.repository.EmailAuthRepositoryImpl
import voloshyn.android.data.repository.OnBoardRepositoryImpl
import voloshyn.android.data.repository.UserSessionRepositoryImpl
import voloshyn.android.data.repository.tabs.logs.CurrentVehicleProvider
import voloshyn.android.data.repository.tabs.logs.RefuelLogsRepositoryImpl
import voloshyn.android.data.repository.tabs.logs.RefuelsProvider
import voloshyn.android.data.repository.tabs.refuel.RefuelRepositoryImpl
import voloshyn.android.data.repository.tabs.vehicles.VehiclesRepositoryImpl
import voloshyn.android.domain.repository.EmailValidatorRepository
import voloshyn.android.domain.repository.OnBoardRepository
import voloshyn.android.domain.repository.account.EmailAuthRepository
import voloshyn.android.domain.repository.account.UserSessionRepository
import voloshyn.android.domain.repository.tabs.RefuelLogsRepository
import voloshyn.android.domain.repository.tabs.VehiclesRepository

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    // USER_SESSION_AUTH

    @Binds
    @ViewModelScoped
    fun bindFirebaseAuthManager(repository: FirebaseAuthManagerImpl): FirebaseAuthManager

    @Binds
    @ViewModelScoped
    fun bindEmailValidatorRepository(repository: AndroidEmailValidatorImpl): EmailValidatorRepository

    @Binds
    @ViewModelScoped
    fun bindEmailAuthRepository(repository: EmailAuthRepositoryImpl): EmailAuthRepository

    @Binds
    @ViewModelScoped
    fun bindUserSessionRepository(repository: UserSessionRepositoryImpl): UserSessionRepository


    @Binds
    @ViewModelScoped
    fun bindRefuelsProvider(repository: RefuelRepositoryImpl): RefuelsProvider

    @Binds
    @ViewModelScoped
    fun bindCurrentVehicleProvider(repository: VehiclesRepositoryImpl): CurrentVehicleProvider


    @Binds
    @ViewModelScoped
    fun bindOnBoardRepository(repository: OnBoardRepositoryImpl): OnBoardRepository


    @Binds
    @ViewModelScoped
    fun bindVehicleRepository(repository: VehiclesRepositoryImpl): VehiclesRepository

    @Binds
    @ViewModelScoped
    fun bindRefuelLogsRepository(repository: RefuelLogsRepositoryImpl): RefuelLogsRepository


}