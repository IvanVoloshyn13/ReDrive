package voloshyn.android.redrive.presentation.tabs.newRefuel

import voloshyn.android.redrive.presentation.tabs.redrive.VehicleId

data class NewRefuelState(
    val currentVehicleId: Long = 0L,
    val date: Long = System.currentTimeMillis(),
    val odometer: String = "",
    val fuelVolume: String = "",
    val unitPrice: String = "",
    val notes: String = "",
    val fullTank: Boolean = true,
    val missedPrevious: Boolean = false,
)
