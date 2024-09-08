package voloshyn.android.domain.useCase.tabs.vehicles

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.repository.tabs.VehiclesRepository

class GetRememberedVehicleUseCase(private val repository: VehiclesRepository) {

    suspend fun invoke(): AppResult<String, DataError.Locale> {
        TODO()
    }
}