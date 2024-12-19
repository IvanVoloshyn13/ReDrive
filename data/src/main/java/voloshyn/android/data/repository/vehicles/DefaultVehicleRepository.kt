package voloshyn.android.data.repository.vehicles

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.Vehicle

interface DefaultVehicleRepository {

    val defaultVehicle: Vehicle

    fun observeDefaultVehicle(): Flow<Vehicle>

    suspend fun setDefaultVehicle(vehicleId: Long)

    fun isDefault(vehicleId: Long): Boolean = defaultVehicle.id == vehicleId
}