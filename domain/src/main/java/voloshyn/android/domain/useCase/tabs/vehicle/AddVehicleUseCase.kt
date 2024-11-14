package voloshyn.android.domain.useCase.tabs.vehicle

import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.repository.tabs.VehiclesRepository

class AddVehicleUseCase(private val repository: VehiclesRepository) {
    suspend fun invoke(
        vehicle: Vehicle,
        accountId: String? = null
    ):Long  {
     return  repository.addVehicle(vehicle = vehicle, accountId = accountId)
    }
}