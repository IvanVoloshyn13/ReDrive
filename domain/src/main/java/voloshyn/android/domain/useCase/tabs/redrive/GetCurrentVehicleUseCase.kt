package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.repository.tabs.VehiclesRepository

class GetCurrentVehicleUseCase(private val repository: VehiclesRepository) {

     fun invoke()=repository.currentVehicle()
}