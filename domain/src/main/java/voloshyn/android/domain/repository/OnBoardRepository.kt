package voloshyn.android.domain.repository

interface OnBoardRepository {
    suspend fun finish(isFinished: Boolean)
}