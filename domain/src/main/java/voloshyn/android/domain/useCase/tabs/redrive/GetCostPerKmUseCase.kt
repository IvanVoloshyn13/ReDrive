package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.repository.tabs.RedriveRepository
import voloshyn.android.domain.useCase.toResult

class GetCostPerKmUseCase(private val repository: RedriveRepository) {
    suspend fun invoke(): AppResult<Double, DataError.Locale> {
        val result = toResult(
            call = { repository.costPerKm() },
            error = {
                return@toResult AppResult.Error(error = it)
            }

        ) {
            return@toResult if (it.isEmpty()) AppResult.Success(data = Double.NaN) else
                AppResult.Success(data = costPerKm(it))
        }

        return result
    }

    private fun costPerKm(costsPerKm: List<Double>): Double {
        return costsPerKm.average()
    }
}




