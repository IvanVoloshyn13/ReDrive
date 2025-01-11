package voloshyn.android.domain.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.IsCurrentVehicleException
import voloshyn.android.domain.models.Vehicle

interface VehiclesRepository {
    val currentVehicle: Vehicle
    /** Add new vehicle to local database */
    suspend fun addNewVehicle(uUid:String,vehicle: Vehicle): Long

    /** Edit vehicle  */
    suspend fun updateVehicle(uUid:String,vehicle: Vehicle)

    /** Delete vehicle from database
     * @throws IsCurrentVehicleException() if vehicle trying to be delete is set as Current */
    @Throws(IsCurrentVehicleException::class)
    suspend fun deleteVehicle(uUid:String,vehicleId: Long)

    /** Save vehicle user chose in last session to dataStore
     * for which all information will be displaying */
    suspend fun setVehicleAsCurrent(vehicleId: Long)

    /** Observe current vehicle from dataStore */
    fun observeCurrentVehicle(): Flow<Vehicle>

    /**Observe vehicles from database */
    fun observeVehicles(): Flow<List<Vehicle>>

    /**Check if there is any vehicle for current user */
    suspend fun isVehicle(uuid: String): Boolean
}