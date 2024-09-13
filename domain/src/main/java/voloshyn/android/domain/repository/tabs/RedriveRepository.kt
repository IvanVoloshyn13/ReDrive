package voloshyn.android.domain.repository.tabs

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.profile.UserTuple
import voloshyn.android.domain.models.tabs.redrive.LastRefuel
import voloshyn.android.domain.models.tabs.redrive.RefuelsTuple

interface RedriveRepository {
    suspend fun currentUser(email: String): AppResult<UserTuple, DataError.Locale>
    suspend fun allTimeAvgConsumption(): AppResult<List<Double>, DataError.Locale>
    suspend fun costPerKm(): AppResult<List<Double>, DataError.Locale>
    suspend fun costPerMiles(): AppResult<Double, DataError.Locale>
    suspend fun lastRefuel(): AppResult<LastRefuel, DataError.Locale>
    suspend fun summary(): AppResult<RefuelsTuple, DataError.Locale>
}