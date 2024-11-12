package voloshyn.android.domain.models.logs

data class RefuelLog(
    val id:Long,
    val vehicleId: Long,
    val odometer: String,
    val avgFuelConsumption: CharSequence,
    val travelledDistance: String,
    val fuelVolume: String,
    val payments: String,
    val unitPrice: String,
    val notes: String,
) {
    companion object {
        val EMPTY_LOG = RefuelLog(
            id = 0L,
            vehicleId = 0L,
            odometer = "",
            avgFuelConsumption = "",
            travelledDistance = "",
            fuelVolume = "",
            payments = "",
            unitPrice = "",
            notes = ""
        )
    }
}
