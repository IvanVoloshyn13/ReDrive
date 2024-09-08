package voloshyn.android.domain.repository.tabs

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.redrive.Vehicle

interface VehiclesRepository {
    /** Add new vehicle to local database and return Boolean value true if success and false if failure */
    suspend fun addVehicle(
        vehicle: Vehicle,
        accountId: String?
    ): AppResult<Boolean, DataError.Locale>

    /** Delete vehicle from database*/
    suspend fun deleteVehicle(id: Long)

    /** Save vehicle user chose in last session to dataStore for which all information will be displaying */
    suspend fun rememberVehicle(vehicleId: Long, name: String)

    /** Get saved vehicle from dataStore */
    suspend fun getRememberedVehicle(): AppResult<Set<String>, DataError.Locale>

    /** Get all vehicles from database */
    fun vehicles(): Flow<AppResult<List<Vehicle>, DataError.Locale>>
}