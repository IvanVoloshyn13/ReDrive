package voloshyn.android.domain.useCase.vehicle

import voloshyn.android.domain.repository.VehiclesRepository

class DeleteVehicleUseCase(
    private val repository: VehiclesRepository,
    ) {
    suspend fun invoke(
        vehicleId: Long,
    ) {
            repository.deleteVehicle(vehicleId = vehicleId)
    }
}