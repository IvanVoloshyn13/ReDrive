package voloshyn.android.redrive.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.domain.repository.userAuth.UserSessionRepository
import voloshyn.android.domain.repository.tabs.VehiclesRepository
import voloshyn.android.domain.useCase.tabs.vehicle.AddVehicleUseCase
import voloshyn.android.domain.useCase.tabs.vehicle.GetCurrentVehicleUseCase
import voloshyn.android.domain.useCase.tabs.vehicle.GetVehiclesUseCase
import voloshyn.android.domain.useCase.tabs.vehicle.IsVehicleUseCase
import voloshyn.android.domain.useCase.tabs.vehicle.RememberCurrentVehicleUseCase

@Module
@InstallIn(ViewModelComponent::class)
object Vehicles {

    @Provides
    @ViewModelScoped
    fun provideAddVehicleUseCase(
        repository: VehiclesRepository,
        userRepository: UserSessionRepository
    ): AddVehicleUseCase {
        return AddVehicleUseCase(repository, userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetVehiclesUseCase(repository: VehiclesRepository): GetVehiclesUseCase {
        return GetVehiclesUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideRememberCurrentVehicleUseCase(repository: VehiclesRepository): RememberCurrentVehicleUseCase {
        return RememberCurrentVehicleUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCurrentVehicleUseCase(repository: VehiclesRepository): GetCurrentVehicleUseCase {
        return GetCurrentVehicleUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideIsVehicleUseCase(repository: VehiclesRepository): IsVehicleUseCase {
        return IsVehicleUseCase(repository)
    }

}