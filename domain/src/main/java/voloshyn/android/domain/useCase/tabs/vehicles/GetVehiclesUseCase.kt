package voloshyn.android.domain.useCase.tabs.vehicles

import voloshyn.android.domain.repository.tabs.VehiclesRepository

class GetVehiclesUseCase(private val repository: VehiclesRepository) {

     fun invoke() = repository.vehicles()
}