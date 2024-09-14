package voloshyn.android.redrive.di.useCases

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.domain.repository.tabs.VehiclesRepository
import voloshyn.android.domain.useCase.tabs.redrive.GetCurrentVehicleUseCase
import voloshyn.android.domain.useCase.tabs.redrive.RememberCurrentVehicleUseCase
import voloshyn.android.domain.useCase.tabs.vehicles.AddVehicleUseCase
import voloshyn.android.domain.useCase.tabs.vehicles.GetVehiclesUseCase

@Module
@InstallIn(ViewModelComponent::class)
object Vehicles {

    @Provides
    @ViewModelScoped
    fun provideAddVehicleUseCase(repository: VehiclesRepository): AddVehicleUseCase {
        return AddVehicleUseCase(repository)
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

}