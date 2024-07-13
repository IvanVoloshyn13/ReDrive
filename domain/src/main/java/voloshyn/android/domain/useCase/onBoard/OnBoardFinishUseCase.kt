package voloshyn.android.domain.useCase.onBoard

import voloshyn.android.domain.repository.OnBoardRepository
import java.io.IOException

class OnBoardFinishUseCase(private val repository: OnBoardRepository) {
    @Throws(exceptionClasses = [IOException::class])
    suspend fun invoke(isFinished: Boolean) {
        repository.finish(isFinished)
    }
}
