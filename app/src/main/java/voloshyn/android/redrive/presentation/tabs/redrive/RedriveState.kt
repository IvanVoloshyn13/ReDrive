package voloshyn.android.redrive.presentation.tabs.redrive

import androidx.annotation.StringRes
import voloshyn.android.app.R
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.refuel.AllTimeAvgConsumption
import voloshyn.android.domain.models.refuel.AllTimeCostPerUnit
import voloshyn.android.domain.models.refuel.Summary
import voloshyn.android.domain.models.Vehicle

data class RedriveState(
    val isLoading: Boolean = false,
    val currentVehicle: Vehicle = Vehicle.DEFAULT_VEHICLE,
    val allTimeCostPerUnit: AllTimeCostPerUnit = AllTimeCostPerUnit(0.0),
    val allTimeAvgConsumption: AllTimeAvgConsumption = AllTimeAvgConsumption(0.0),
    val summary: Summary = TODO()
    ,
    val lastRefuel : Any = TODO() ,
    val error: DataError? = null,
    @StringRes
    val errorMessage: Int = R.string.empty_string
)
