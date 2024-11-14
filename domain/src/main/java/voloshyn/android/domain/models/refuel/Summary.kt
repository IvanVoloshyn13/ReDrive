package voloshyn.android.domain.models.refuel

import voloshyn.android.domain.models.logs.RefuelLog
import java.util.LinkedList

data class Summary(
    val travelledDistance: Int,
    val totalFuelVolume: Double,
    val totalPayments: Double,
    val avgConsumption: AllTimeAvgConsumption,
    val costPerUnit: AllTimeCostPerUnit,
    val lastRefuel: RefuelLog
)
