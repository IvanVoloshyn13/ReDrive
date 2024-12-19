package voloshyn.android.domain.useCase.vehicle

import voloshyn.android.domain.repository.VehiclesRepository

class ObserveVehiclesUseCase(private val repository: VehiclesRepository) {

     fun invoke() = repository.observeVehicles()
}