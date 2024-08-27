package voloshyn.android.domain.useCase.tabs.redrive

import voloshyn.android.domain.models.UserTuple
import voloshyn.android.domain.repository.tabs.RedriveRepository

class CurrentUserUseCase(private val repository: RedriveRepository) {
    suspend fun invoke(): UserTuple {
        val user = repository.currentUser ?: UserTuple.EMPTY_USER
        return user
    }
}