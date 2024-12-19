package voloshyn.android.domain.useCase.vehicle

import voloshyn.android.domain.repository.VehiclesRepository

class IsVehicleUseCase(private val repository: VehiclesRepository) {
    suspend fun invoke(uuid:String): Boolean {
        return repository.isVehicle(uuid)
    }
}