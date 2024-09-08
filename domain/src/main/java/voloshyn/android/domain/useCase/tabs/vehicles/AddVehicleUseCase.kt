package voloshyn.android.domain.useCase.tabs.vehicles

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.domain.repository.tabs.VehiclesRepository

class AddVehicleUseCase(private val repository: VehiclesRepository) {
    suspend fun invoke(
        vehicle: Vehicle,
        accountId: String? = null
    ): AppResult<Boolean, DataError.Locale> {
        return repository.addVehicle(vehicle = vehicle, accountId = accountId)
    }
}