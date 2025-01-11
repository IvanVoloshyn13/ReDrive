package voloshyn.android.domain.useCase.vehicle

import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.repository.VehiclesRepository
import voloshyn.android.domain.repository.userAuth.UserSessionRepository

class UpdateVehicleUseCase(
    private val repository: VehiclesRepository,
    private val userRepository: UserSessionRepository

) {
    suspend fun invoke(
        vehicle: Vehicle,
    ) {
        repository.updateVehicle(vehicle = vehicle, uUid = userRepository.user.id)
    }
}