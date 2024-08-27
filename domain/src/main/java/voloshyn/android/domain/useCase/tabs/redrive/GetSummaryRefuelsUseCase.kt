package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.redrive.Summary
import voloshyn.android.domain.repository.tabs.RedriveRepository
import voloshyn.android.domain.useCase.toResult

class GetSummaryRefuelsUseCase(private val repository: RedriveRepository) {

    suspend fun invoke(): AppResult<Summary, DataError.Locale> {
        val result = toResult(call = { repository.summary() },
            error = {
                return@toResult AppResult.Error(error = it)
            }
        )
        {
            return@toResult if (it.distances.isNotEmpty() && it.payments.isNotEmpty() && it.fuelVolumes.isNotEmpty()) {
                AppResult.Success(
                    data = summary(
                        distances = it.distances,
                        fuelVolumes = it.fuelVolumes,
                        payments = it.payments
                    )
                )
            } else return@toResult AppResult.Success(
                data = Summary(
                    travelledDistance = 0,
                    totalFuelVolume = Double.NaN,
                    totalPayments = Double.NaN
                )
            )
        }

        return result
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