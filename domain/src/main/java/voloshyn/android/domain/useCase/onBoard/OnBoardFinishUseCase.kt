package voloshyn.android.domain.useCase.onBoard

import voloshyn.android.domain.repository.OnBoardRepository

class OnBoardFinishUseCase(private val repository: OnBoardRepository) {

    suspend fun invoke(isFinished: Boolean) {
        repository.onFinish(isFinished)
    }
}
