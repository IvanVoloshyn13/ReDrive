package voloshyn.android.domain.useCase.vehicle

import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.repository.VehiclesRepository

const val ERROR_VEHICLE_ID = -1L

class AddVehicleUseCase(
    private val repository: VehiclesRepository
) {

    /** Add new vehicle to local database also set this vehicle as Default . Maybe i separate it latter: original 2024.12.19 */
    suspend fun invoke(
        vehicle: Vehicle,
    ) {
        val vehicleId = repository.addNewVehicle(vehicle = vehicle)
        if (vehicleId != ERROR_VEHICLE_ID) repository.setVehicleAsDefault(vehicleId)
    }
}