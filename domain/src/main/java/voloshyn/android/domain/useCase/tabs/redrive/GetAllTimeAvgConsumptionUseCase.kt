package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.repository.tabs.RedriveRepository

class GetAllTimeAvgConsumptionUseCase(private val repository: RedriveRepository) {
    suspend fun invoke(): AppResult<Double, DataError.Locale> {
        val result = repository.allTimeAvgConsumption()
        return when (result) {
            is AppResult.Error -> AppResult.Error(error = result.error)
            is AppResult.Success -> {
                if (result.data.isEmpty()) AppResult.Success(data = Double.NaN) else AppResult.Success(
                    data = AvgConsumption(result.data)
                )
            }
        }

    }

    private fun AvgConsumption(avgConsumptions: List<Double>): Double {
        var avgConsumptionsSum = 0.0
        avgConsumptions.forEach {
            avgConsumptionsSum += it
        }
        return avgConsumptionsSum / avgConsumptions.size
    }

}