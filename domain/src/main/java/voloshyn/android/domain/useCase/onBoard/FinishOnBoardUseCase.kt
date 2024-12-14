package voloshyn.android.domain.useCase.onBoard

import voloshyn.android.domain.models.OnBoardStatus
import voloshyn.android.domain.repository.OnBoardRepository

class FinishOnBoardUseCase(private val repository: OnBoardRepository) {

    suspend fun invoke(status: OnBoardStatus) {
        repository.onFinish(status)
    }
}
