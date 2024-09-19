package voloshyn.android.domain.models.tabs

data class RefuelLog(
    val odometer: Int,
    val avgFuelConsumption: Double,
    val travelledDistance: Int,
    val fuelVolume: Double,
    val payments: Double,
    val unitPrice: Double
)
