package voloshyn.android.domain.repository.tabs

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.DataStoreException
import voloshyn.android.domain.models.Vehicle

interface VehiclesRepository {
    /** Add new vehicle to local database and return Boolean value true if success and false if failure */
    suspend fun saveNewVehicle(
        vehicle: Vehicle,
        accountId: String
    ): Long

    /** Delete vehicle from database*/
    suspend fun deleteVehicle(id: Long)

    /** Save vehicle user chose in last session to dataStore for which all information will be displaying
     * @throws DataStoreException */
    suspend fun rememberVehicle(vehicleId: Long)

    /** Get saved vehicle from dataStore */
    fun currentVehicle(): Flow<Vehicle>

    /** Get all vehicles from database */
    fun vehicles(): Flow<List<Vehicle>>

    suspend fun isVehicle(userId: String): Boolean
}