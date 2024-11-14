package voloshyn.android.redrive.presentation.tabs.logs

import androidx.annotation.StringRes
import voloshyn.android.app.R
import voloshyn.android.domain.models.logs.RefuelLog
import voloshyn.android.domain.models.Vehicle

data class LogsState(
    val currentVehicle: Vehicle = Vehicle.DEFAULT_VEHICLE,
    val refuelLogs: List<RefuelLog> = ArrayList(),
    val isError: Boolean = false,
    @StringRes val message: Int = R.string.empty_string
)
