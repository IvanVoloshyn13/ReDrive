package voloshyn.android.domain.useCase.tabs.vehicle

import voloshyn.android.domain.repository.tabs.VehiclesRepository

class IsVehicleUseCase(private val repository: VehiclesRepository) {
    suspend fun invoke(userId: String): Boolean {
        return repository.isVehicle(userId)
    }
}