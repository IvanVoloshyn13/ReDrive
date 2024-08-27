package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.domain.repository.tabs.RedriveRepository
import voloshyn.android.domain.useCase.toResult

class GetVehiclesUseCase(private val repository: RedriveRepository) {

    suspend fun invoke(): AppResult<Array<Vehicle>, DataError.Locale> {
        val result = toResult(
            call = { repository.vehicles() },
            error = {
                AppResult.Error(error = it)
            }
        ){
            AppResult.Success(data = it)
        }
        return result
    }
}