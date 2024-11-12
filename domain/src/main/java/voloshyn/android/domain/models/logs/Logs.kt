package voloshyn.android.domain.models.logs

data class Logs(
    val vehicleId: Long,
    val refuelLogs: List<RefuelLog>,
    //TODO repair logs
)