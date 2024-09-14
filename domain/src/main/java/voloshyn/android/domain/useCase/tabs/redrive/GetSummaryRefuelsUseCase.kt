package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.models.tabs.redrive.Summary
import voloshyn.android.domain.repository.tabs.RedriveRepository

class GetSummaryRefuelsUseCase(private val repository: RedriveRepository) {

    suspend fun invoke(): Summary {
        val refuels = repository.summary()
        return if (refuels.distances.isNotEmpty() && refuels.payments.isNotEmpty() && refuels.fuelVolumes.isNotEmpty()) {
            summary(
                distances = refuels.distances,
                fuelVolumes = refuels.fuelVolumes,
                payments = refuels.payments
            )
        } else Summary(
            travelledDistance = 0,
            totalFuelVolume = Double.NaN,
            totalPayments = Double.NaN
        )

    }

    private fun summary(
        distances: List<Int>,
        fuelVolumes: List<Double>,
        payments: List<Double>
    ): Summary {
        val travelledDistance = distances.sum()
        val totalFuelVolume = fuelVolumes.sum()
        val totalPayments = payments.sum()

        return Summary(
            travelledDistance = travelledDistance,
            totalFuelVolume = totalFuelVolume,
            totalPayments = totalPayments
        )
    }

}

