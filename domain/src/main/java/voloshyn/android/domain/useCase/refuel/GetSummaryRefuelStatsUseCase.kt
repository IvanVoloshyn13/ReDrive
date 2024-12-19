package voloshyn.android.domain.useCase.refuel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import voloshyn.android.domain.models.logs.RefuelLog
import voloshyn.android.domain.models.refuel.RefuelTuple
import voloshyn.android.domain.models.refuel.Summary
import voloshyn.android.domain.models.refuel.TotalAvgConsumption
import voloshyn.android.domain.models.refuel.TotalCostPerUnit
import voloshyn.android.domain.repository.RefuelLogsRepository
import voloshyn.android.domain.repository.RefuelRepository

/**
 * Use case to get a summary of refuel logs and details.
 *
 * @property refuelRepository Repository for refuel data.
 * @property refuelLogRepository Repository for refuel log data.
 */
class GetSummaryRefuelStatsUseCase(
    private val refuelRepository: RefuelRepository,
    private val refuelLogRepository: RefuelLogsRepository
) {

    /**
     * Retrieves the combined summary of refuels and last refuel log.
     *
     * @return A [Flow] emitting [Summary] containing aggregated refuel information.
     */
    fun invoke(): Flow<Summary> {

        val refuels = refuelRepository.refuelTuple()
        val lastRefuelLog = refuelLogRepository.lastRefuel()

        return refuels.combine(lastRefuelLog) { refuelTuple, refuelLog ->
            toSummary(
                refuel = refuelTuple,
                lastRefuel = refuelLog
            )
        }
    }

    /**
     * Generates a summary based on provided refuel data.
     *
     * @param refuel The refuel data containing distances, fuel volumes, and payments.
     * @param lastRefuel The most recent refuel log.
     * @return A [Summary] object containing calculated values.
     */
    private fun toSummary(
        refuel: RefuelTuple,
        lastRefuel: RefuelLog
    ): Summary {

        val travelledDistance = refuel.distances.sum()
        val totalFuelVolume = refuel.fuelVolumes.sum()
        val totalPayments = refuel.payments.sum()

        val avgConsumption = travelledDistance.handleZeroCase {
            (totalFuelVolume / travelledDistance) * 100
        }
        val costPerDistance = travelledDistance.handleZeroCase {
            totalPayments / travelledDistance
        }

        return Summary(
            travelledDistance = travelledDistance,
            totalFuelVolume = totalFuelVolume,
            totalPayments = totalPayments,
            lastRefuel = lastRefuel,
            totalAvgConsumption = TotalAvgConsumption(avgConsumption),
            totalCostPerUnit = TotalCostPerUnit(costPerDistance)
        )
    }

    /**
     * Extension function to handle cases where the number value is zero.
     *
     * @param block The calculation to perform if the value is greater than zero.
     * @return The result of the calculation or zero if the value is zero.
     */
    private inline fun <T : Number> T.handleZeroCase(block: (Double) -> Double): Double {
        val value = this.toDouble()
        return if (value > 0) block(value) else 0.0
    }

}

