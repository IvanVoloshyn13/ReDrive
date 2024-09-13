package voloshyn.android.domain.useCase.onBoard

import voloshyn.android.domain.repository.OnBoardRepository

class OnBoardIsFinishedUseCase(private val repository: OnBoardRepository) {

    suspend fun invoke() = repository.isFinished()
}