package voloshyn.android.domain.useCase.tabs.vehicle

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
        println(userId + "TUTAJ")
        return if (userId.isNotEmpty()) {
            repository.saveNewVehicle(vehicle = vehicle, accountId = userId)
        } else {
            println(userId + "TUTAJ1")
            0L
        }
    }
}