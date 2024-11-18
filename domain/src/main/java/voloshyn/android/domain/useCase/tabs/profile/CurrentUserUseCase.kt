package voloshyn.android.domain.useCase.tabs.profile

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.account.UserSessionRepository

class CurrentUserUseCase(private val repository: UserSessionRepository) {
    fun invoke(): Flow<User?> {
        val user = repository.observeCurrentUser()
        return user
    }
}