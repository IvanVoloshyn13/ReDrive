package voloshyn.android.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.firebase.FirebaseAuthManager
import voloshyn.android.data.firebase.FirebaseAuthManagerImpl
import voloshyn.android.data.repository.AppSettingsRepositoryImpl
import voloshyn.android.data.repository.logs.DataStringResProviderImpl
import voloshyn.android.data.repository.logs.DataStringResourceProvider
import voloshyn.android.data.repository.user.AppCurrentUserRepository
import voloshyn.android.data.repository.user.AppCurrentUserRepositoryImpl
import voloshyn.android.data.repository.user.UserSessionRepositoryImpl
import voloshyn.android.data.repository.vehicles.DefaultVehicleRepository
import voloshyn.android.data.repository.vehicles.DefaultVehicleRepositoryImpl
import voloshyn.android.data.repository.vehicles.VehiclesRepositoryImpl
import voloshyn.android.domain.repository.AppSettingsRepository
import voloshyn.android.domain.repository.VehiclesRepository
import voloshyn.android.domain.repository.userAuth.UserSessionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SingletonRepositoryModule {

    @Binds
    @Singleton
    fun bindFirebaseAuthManager(repository: FirebaseAuthManagerImpl): FirebaseAuthManager

    @Binds
    @Singleton
    fun bindUserSessionRepository(repository: UserSessionRepositoryImpl): UserSessionRepository

    @Binds
    @Singleton
    fun bindCurrentUserProvider(repository: AppCurrentUserRepositoryImpl): AppCurrentUserRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(repository: AppSettingsRepositoryImpl): AppSettingsRepository

    @Binds
    @Singleton
    fun bindVehicleRepository(repository: VehiclesRepositoryImpl): VehiclesRepository

    @Binds
    @Singleton
    fun bindDefaultVehicleRepository(repository: DefaultVehicleRepositoryImpl): DefaultVehicleRepository

    @Binds
    @Singleton
    fun bindDataStringResProvider(repository: DataStringResProviderImpl): DataStringResourceProvider
}