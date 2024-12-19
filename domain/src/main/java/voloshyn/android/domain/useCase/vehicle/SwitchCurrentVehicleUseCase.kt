package voloshyn.android.domain.useCase.vehicle

import voloshyn.android.domain.repository.VehiclesRepository

class SwitchCurrentVehicleUseCase(private val repository: VehiclesRepository) {
    suspend fun invoke(vehicleId: Long) {
        repository.setVehicleAsDefault(vehicleId)
    }
}