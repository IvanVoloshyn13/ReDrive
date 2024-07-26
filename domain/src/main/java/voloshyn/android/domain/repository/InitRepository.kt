package voloshyn.android.domain.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.UserTuple

interface InitRepository {
     fun isSignedIn(): AppResult<UserTuple?, AuthError.Auth>
    suspend fun isFinished(): AppResult<Flow<Boolean>, DataError.Locale>
}