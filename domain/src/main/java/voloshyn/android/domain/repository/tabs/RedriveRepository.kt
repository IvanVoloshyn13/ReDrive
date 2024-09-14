package voloshyn.android.domain.repository.tabs

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.profile.UserTuple
import voloshyn.android.domain.models.tabs.redrive.LastRefuel
import voloshyn.android.domain.models.tabs.redrive.RefuelsTuple

interface RedriveRepository {
    suspend fun currentUser(email: String): AppResult<UserTuple, DataError.Locale>
    suspend fun allTimeAvgConsumption(): List<Double>
    suspend fun costPerKm(): List<Double>
    suspend fun costPerMiles():Double
    suspend fun lastRefuel():LastRefuel
    suspend fun summary():RefuelsTuple
}