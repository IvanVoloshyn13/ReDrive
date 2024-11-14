package voloshyn.android.domain.useCase.tabs.vehicle

import voloshyn.android.domain.repository.tabs.VehiclesRepository

class RememberCurrentVehicleUseCase(private val repository: VehiclesRepository) {
    suspend fun invoke(vehicleId: Long) {
        repository.rememberVehicle(vehicleId)
    }
}