package voloshyn.android.domain.repository

import voloshyn.android.domain.models.OnBoardStatus

interface OnBoardRepository {
    suspend fun onFinish(status: OnBoardStatus)
    suspend fun isFinished(): OnBoardStatus
}