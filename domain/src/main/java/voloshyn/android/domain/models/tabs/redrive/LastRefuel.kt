package voloshyn.android.domain.models.tabs.redrive

data class LastRefuel(
    val travelledDistance: Int,
    val fuelVolume: Double,
    val payment: Double,
    val unitPrice: Double,
    val avgConsumption: Double
)