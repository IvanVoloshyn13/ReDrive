package voloshyn.android.domain.useCase.vehicle

import voloshyn.android.domain.repository.VehiclesRepository

class ObserveCurrentVehicleUseCase(private val repository: VehiclesRepository) {

     fun invoke()=repository.observeDefaultVehicle()
}