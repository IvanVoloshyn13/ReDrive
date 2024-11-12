package voloshyn.android.domain.useCase.tabs.profile

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.AuthRepository

class CurrentUserUseCase(private val repository: AuthRepository) {
    suspend fun invoke(email: String): Flow<User?> {
        val user = repository.observeCurrentUser()
        return user
    }
}