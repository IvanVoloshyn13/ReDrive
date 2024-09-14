package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.repository.tabs.RedriveRepository

class GetAllTimeAvgConsumptionUseCase(private val repository: RedriveRepository) {
    suspend fun invoke(): Double {
        val avgConsumptions = repository.allTimeAvgConsumption()
        return if (avgConsumptions.isEmpty()) Double.NaN else avgConsumption(avgConsumptions)

    }

    private fun avgConsumption(avgConsumptions: List<Double>): Double {
        var avgConsumptionsSum = 0.0
        avgConsumptions.forEach {
            avgConsumptionsSum += it
        }
        return avgConsumptionsSum / avgConsumptions.size
    }

}