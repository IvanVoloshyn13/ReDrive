package voloshyn.android.redrive.di.repositoryModules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.data.repository.AndroidEmailValidatorImpl
import voloshyn.android.data.repository.AuthRepositoryImpl
import voloshyn.android.data.repository.OnBoardRepositoryImpl
import voloshyn.android.data.repository.tabs.VehiclesRepositoryImpl
import voloshyn.android.domain.repository.AuthRepository
import voloshyn.android.domain.repository.EmailValidatorRepository
import voloshyn.android.domain.repository.OnBoardRepository
import voloshyn.android.domain.repository.tabs.VehiclesRepository

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    @ViewModelScoped
    fun bindOnBoardRepository(repository: OnBoardRepositoryImpl): OnBoardRepository

    @Binds
    @ViewModelScoped
    fun bindUserRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    @ViewModelScoped
    fun bindEmailValidatorRepository(repository: AndroidEmailValidatorImpl): EmailValidatorRepository


    @Binds
    @ViewModelScoped
    fun bindVehicleRepository(repository: VehiclesRepositoryImpl): VehiclesRepository


}