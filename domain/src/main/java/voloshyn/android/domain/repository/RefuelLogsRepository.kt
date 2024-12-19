package voloshyn.android.domain.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.logs.RefuelLog
import voloshyn.android.domain.models.logs.VehicleWithRefuels

interface RefuelLogsRepository {
    fun getVehicleWithRefuels(): Flow<VehicleWithRefuels>
    fun lastRefuel():Flow<RefuelLog>
}