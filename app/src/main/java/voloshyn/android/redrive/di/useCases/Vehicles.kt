package voloshyn.android.redrive.di.useCases

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.domain.repository.tabs.VehiclesRepository
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

}