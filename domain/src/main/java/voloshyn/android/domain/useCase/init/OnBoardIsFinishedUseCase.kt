package voloshyn.android.domain.useCase.init

import voloshyn.android.domain.repository.InitRepository

class OnBoardIsFinishedUseCase(private val repository: InitRepository) {

    suspend fun invoke() = repository.isFinished()
}