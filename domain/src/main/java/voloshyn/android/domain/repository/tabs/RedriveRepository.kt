package voloshyn.android.domain.repository.tabs

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.UserTuple
import voloshyn.android.domain.models.tabs.redrive.LastRefuel
import voloshyn.android.domain.models.tabs.redrive.RefuelsTuple
import voloshyn.android.domain.models.tabs.redrive.Summary
import voloshyn.android.domain.models.tabs.redrive.Vehicle

interface RedriveRepository {
    val currentUser: UserTuple?
    suspend fun vehicles(): AppResult<Array<Vehicle>, DataError.Locale>
    suspend fun totalAvgConsumption(): AppResult<List<Double>, DataError.Locale>
    suspend fun costPerKm(): AppResult<List<Double>, DataError.Locale>
    suspend fun costPerMiles(): AppResult<Double, DataError.Locale>
    suspend fun lastRefuel(): AppResult<LastRefuel, DataError.Locale>
    suspend fun summary(): AppResult<RefuelsTuple, DataError.Locale>
}