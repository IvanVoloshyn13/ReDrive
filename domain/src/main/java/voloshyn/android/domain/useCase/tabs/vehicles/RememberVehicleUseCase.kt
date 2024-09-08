package voloshyn.android.domain.useCase.tabs.vehicles

import voloshyn.android.domain.repository.tabs.VehiclesRepository

class RememberVehicleUseCase(private val repository: VehiclesRepository) {
    suspend fun invoke(id: Long, name: String) = repository.rememberVehicle(id, name)
}