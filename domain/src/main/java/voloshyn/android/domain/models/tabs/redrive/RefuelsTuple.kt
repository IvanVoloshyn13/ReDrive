package voloshyn.android.domain.models.tabs.redrive

data class RefuelsTuple(
    val distances: List<Int>,
    val fuelVolumes: List<Double>,
    val payments: List<Double>
)