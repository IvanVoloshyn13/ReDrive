package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.repository.tabs.RedriveRepository

class GetCostPerKmUseCase(private val repository: RedriveRepository) {
    suspend fun invoke(): AppResult<Double, DataError.Locale> {
        TODO()
    }

    private fun costPerKm(costsPerKm: List<Double>): Double {
        return costsPerKm.average()
    }
}




