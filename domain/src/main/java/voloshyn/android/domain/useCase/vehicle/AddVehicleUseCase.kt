package voloshyn.android.domain.useCase.vehicle

import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.repository.VehiclesRepository
import voloshyn.android.domain.repository.userAuth.UserSessionRepository

const val ERROR_VEHICLE_ID = -1L

class AddVehicleUseCase(
    private val repository: VehiclesRepository,
    private val userRepository: UserSessionRepository
) {

    /** Add new vehicle to local database also set this vehicle as Default . Maybe i separate it latter: original 2024.12.19 */
    suspend fun invoke(
        vehicle: Vehicle,
    ) {

        val vehicleId = repository.addNewVehicle(vehicle = vehicle, uUid = userRepository.user.id)
        if (vehicleId != ERROR_VEHICLE_ID) repository.setVehicleAsCurrent(vehicleId)
    }
}