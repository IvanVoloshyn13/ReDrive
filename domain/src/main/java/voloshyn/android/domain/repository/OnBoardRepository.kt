package voloshyn.android.domain.repository

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError

interface OnBoardRepository {
    suspend fun onFinish(isFinished: Boolean)
    suspend fun isFinished(): AppResult<Boolean, DataError.Locale>
}