package voloshyn.android.domain.useCase.tabs.vehicles

import voloshyn.android.domain.repository.tabs.VehiclesRepository

class GetVehiclesUseCase(private val repository: VehiclesRepository) {

    suspend fun invoke() = repository.vehicles()
}