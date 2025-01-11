package voloshyn.android.domain.useCase.vehicle

import voloshyn.android.domain.IsCurrentVehicleException
import voloshyn.android.domain.repository.VehiclesRepository
import voloshyn.android.domain.repository.userAuth.UserSessionRepository

class DeleteVehicleUseCase(
    private val repository: VehiclesRepository,
    private val userRepository: UserSessionRepository
) {
    suspend fun invoke(
        vehicleId: Long,
    ) {
        if (vehicleId == repository.currentVehicle.id) throw IsCurrentVehicleException()
        repository.deleteVehicle(vehicleId = vehicleId, uUid = userRepository.user.id)
    }
}