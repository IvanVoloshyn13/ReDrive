package voloshyn.android.domain.repository.tabs

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.DataStoreException
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.domain.models.tabs.redrive.VehicleTuple

interface VehiclesRepository {
    /** Add new vehicle to local database and return Boolean value true if success and false if failure */
    suspend fun addVehicle(
        vehicle: Vehicle,
        accountId: String?
    )

    /** Delete vehicle from database*/
    suspend fun deleteVehicle(id: Long)

    /** Save vehicle user chose in last session to dataStore for which all information will be displaying
     * @throws DataStoreException */
    suspend fun rememberVehicle(vehicleId: Long, name: String)

    /** Get saved vehicle from dataStore */
     fun currentVehicle(): Flow<VehicleTuple>

    /** Get all vehicles from database */
    fun vehicles(): Flow<List<Vehicle>>
}