package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.redrive.Summary
import voloshyn.android.domain.repository.tabs.RedriveRepository

class GetSummaryRefuelsUseCase(private val repository: RedriveRepository) {

    suspend fun invoke(): AppResult<Summary, DataError.Locale> {
        val result = repository.summary()
        return when (result) {
            is AppResult.Error -> AppResult.Error(error = result.error)

            is AppResult.Success ->

                if (result.data.distances.isNotEmpty() && result.data.payments.isNotEmpty() && result.data.fuelVolumes.isNotEmpty()) {
                    AppResult.Success(
                        data = summary(
                            distances = result.data.distances,
                            fuelVolumes = result.data.fuelVolumes,
                            payments = result.data.payments
                        )
                    )
                } else return AppResult.Success(
                    data
                    = Summary(
                        travelledDistance = 0,
                        totalFuelVolume = Double.NaN,
                        totalPayments = Double.NaN
                    )

                )
        }

    }
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

