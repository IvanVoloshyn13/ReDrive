package voloshyn.android.domain.models.refuel

data class Refuel(
    val id:Long,
    val vehicleId: Long,
    val date: Long,
    val odometer: Int,
    val fuelVolume: Double,
    val unitPrice: Double,
    val notes: String,
    val fullTank: Boolean,
    val missedPrevious: Boolean
)
