package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.redrive.LastRefuel
import voloshyn.android.domain.repository.tabs.RedriveRepository
import voloshyn.android.domain.useCase.toResult

class LastRefuelUseCase(private val repository: RedriveRepository) {

    suspend fun invoke(): AppResult<LastRefuel, DataError.Locale> {
        val result = toResult(
            call = { repository.lastRefuel() },
            error = {
                AppResult.Error(error = it)
            }
        ) {
            AppResult.Success(data = it)
        }
        return result
    }
}