package voloshyn.android.domain.useCase.tabs.vehicle

import kotlinx.coroutines.flow.first
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.repository.tabs.VehiclesRepository
import voloshyn.android.domain.repository.userAuth.UserSessionRepository

class AddVehicleUseCase(
    private val repository: VehiclesRepository,
    private val userRepository: UserSessionRepository
) {
    suspend fun invoke(
        vehicle: Vehicle,
    ): Long {
        val userId = userRepository.user.id
        return if (userId.isNotEmpty()) {
            repository.saveNewVehicle(vehicle = vehicle, accountId = userId)
        } else {
            0L
        }
    }
}