package voloshyn.android.data.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.refuel.Refuel

interface RefuelsProvider {
     fun refuelsForVehicle(vehicleId:Long):Flow<List<Refuel>>
}