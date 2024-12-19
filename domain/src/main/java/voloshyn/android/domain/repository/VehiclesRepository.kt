package voloshyn.android.domain.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.IsDefaultVehicleException
import voloshyn.android.domain.models.Vehicle

interface VehiclesRepository {
    /** Add new vehicle to local database */
    suspend fun addNewVehicle(vehicle: Vehicle): Long

    /** Edit vehicle  */
    suspend fun updateVehicle(vehicle: Vehicle)

    /** Delete vehicle from database
     * @throws IsDefaultVehicleException() if vehicle trying to be delete is set as Current */
    @Throws(IsDefaultVehicleException::class)
    suspend fun deleteVehicle(vehicleId: Long)

    /** Save vehicle user chose in last session to dataStore
     * for which all information will be displaying */
    suspend fun setVehicleAsDefault(vehicleId: Long)

    /** Observe current vehicle from dataStore */
    fun observeDefaultVehicle(): Flow<Vehicle>

    /**Observe vehicles from database */
    fun observeVehicles(): Flow<List<Vehicle>>

    /**Check if there is any vehicle for current user */
    suspend fun isVehicle(uuid: String): Boolean
}