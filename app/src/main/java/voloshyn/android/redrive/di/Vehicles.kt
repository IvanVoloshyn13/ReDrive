package voloshyn.android.redrive.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.domain.repository.VehiclesRepository
import voloshyn.android.domain.useCase.vehicle.AddVehicleUseCase
import voloshyn.android.domain.useCase.vehicle.DeleteVehicleUseCase
import voloshyn.android.domain.useCase.vehicle.IsVehicleUseCase
import voloshyn.android.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import voloshyn.android.domain.useCase.vehicle.ObserveVehiclesUseCase
import voloshyn.android.domain.useCase.vehicle.SwitchCurrentVehicleUseCase
import voloshyn.android.domain.useCase.vehicle.UpdateVehicleUseCase

@Module
@InstallIn(ViewModelComponent::class)
object Vehicles {

    @Provides
    @ViewModelScoped
    fun provideAddVehicleUseCase(
        repository: VehiclesRepository
    ): AddVehicleUseCase {
        return AddVehicleUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetVehiclesUseCase(repository: VehiclesRepository): ObserveVehiclesUseCase {
        return ObserveVehiclesUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideRememberCurrentVehicleUseCase(repository: VehiclesRepository): SwitchCurrentVehicleUseCase {
        return SwitchCurrentVehicleUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCurrentVehicleUseCase(repository: VehiclesRepository): ObserveCurrentVehicleUseCase {
        return ObserveCurrentVehicleUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideIsVehicleUseCase(repository: VehiclesRepository): IsVehicleUseCase {
        return IsVehicleUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateVehicleUseCase(repository: VehiclesRepository): UpdateVehicleUseCase {
        return UpdateVehicleUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteVehicleUseCase(repository: VehiclesRepository): DeleteVehicleUseCase {
        return DeleteVehicleUseCase(repository)
    }

}