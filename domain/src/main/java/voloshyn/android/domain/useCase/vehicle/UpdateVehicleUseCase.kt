package voloshyn.android.domain.useCase.vehicle

import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.repository.VehiclesRepository

class UpdateVehicleUseCase(
    private val repository: VehiclesRepository,

    ) {
    suspend fun invoke(
        vehicle: Vehicle,
    ) {
            repository.updateVehicle(vehicle = vehicle)
    }
}