package voloshyn.android.redrive.presentation.tabs.redrive

import androidx.annotation.StringRes
import voloshyn.android.app.R
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.redrive.AllTimeAvgConsumption
import voloshyn.android.domain.models.tabs.redrive.AllTimeCostPerUnit
import voloshyn.android.domain.models.tabs.redrive.LastRefuel
import voloshyn.android.domain.models.tabs.redrive.Summary
import voloshyn.android.domain.models.tabs.redrive.VehicleTuple

typealias VehicleId = Int
typealias VehicleName = String

data class RedriveState(
    val isLoading: Boolean = false,
    val currentVehicle: VehicleTuple = VehicleTuple(0L, ""),
    val allTimeCostPerUnit: AllTimeCostPerUnit = AllTimeCostPerUnit(0.0),
    val allTimeAvgConsumption: AllTimeAvgConsumption = AllTimeAvgConsumption(0.0),
    val summary: Summary = Summary(
        travelledDistance = 0,
        totalFuelVolume = 0.0,
        totalPayments = 0.0
    ),
    val lastRefuel: LastRefuel = LastRefuel(
        travelledDistance = 0, fuelVolume = 0.0, payment = 0.0,
        unitPrice = 0.0, avgConsumption = 0.0
    ),
    val error: DataError? = null,
    @StringRes
    val errorMessage: Int = R.string.empty_string
)
