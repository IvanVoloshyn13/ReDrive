package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.repository.tabs.RedriveRepository
import voloshyn.android.domain.useCase.toResult

class GetTotalAvgConsumptionUseCase(private val repository: RedriveRepository) {
    suspend fun invoke(): AppResult<Double, DataError.Locale> {
        val result = toResult(
            call = { repository.totalAvgConsumption() },
            error = {
                return@toResult AppResult.Error(error = it)
            })
        {
            return@toResult if (it.isEmpty()) AppResult.Success(data = Double.NaN) else AppResult.Success(
                data = totalAvgConsumption(it)
            )
        }
        return result
    }

    private fun totalAvgConsumption(avgConsumptions: List<Double>): Double {
        var avgConsumptionsSum = 0.0
        avgConsumptions.forEach {
            avgConsumptionsSum += it
        }
        return avgConsumptionsSum / avgConsumptions.size
    }

}