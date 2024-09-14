package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.models.tabs.redrive.VehicleTuple
import voloshyn.android.domain.repository.tabs.VehiclesRepository

class RememberCurrentVehicleUseCase(private val repository: VehiclesRepository) {
    suspend fun invoke(vehicle: VehicleTuple) {
        repository.rememberVehicle(vehicle.id, vehicle.name)
    }
}