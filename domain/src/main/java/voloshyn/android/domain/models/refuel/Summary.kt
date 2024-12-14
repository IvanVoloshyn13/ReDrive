package voloshyn.android.domain.models.refuel

import voloshyn.android.domain.models.logs.RefuelLog

data class Summary(
    val travelledDistance: Int,
    val totalFuelVolume: Double,
    val totalPayments: Double,
    val totalAvgConsumption: TotalAvgConsumption,
    val totalCostPerUnit: TotalCostPerUnit,
    val lastRefuel: RefuelLog
)
