package voloshyn.android.domain.models.refuel

data class RefuelTuple(
    val distances: List<Int>,
    val fuelVolumes: List<Double>,
    val payments: List<Double>
)