package voloshyn.android.domain.models.logs

import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.refuel.Refuel

data class VehicleWithRefuels(
    val vehicle: Vehicle,
    val refuelLogs: List<Refuel>,

    //TODO() repair logs
)
