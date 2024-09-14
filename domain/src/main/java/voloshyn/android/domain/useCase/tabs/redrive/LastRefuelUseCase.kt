package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.redrive.LastRefuel
import voloshyn.android.domain.repository.tabs.RedriveRepository

class LastRefuelUseCase(private val repository: RedriveRepository) {

    suspend fun invoke(): LastRefuel {
        return repository.lastRefuel()
    }
}