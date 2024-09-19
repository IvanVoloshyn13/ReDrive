package voloshyn.android.domain.models.tabs

import java.util.Date

data class Refuel(
    val vehicleId: Long,
    val date: Date,
    val odometer: Int,
    val fuelVolume: Double,
    val unitPrice: Double,
    val notes: String?,
    val fullTank: Boolean,
    val missedPrevious: Boolean
)
