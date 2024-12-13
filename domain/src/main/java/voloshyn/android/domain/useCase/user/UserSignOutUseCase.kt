package voloshyn.android.domain.useCase.user

import voloshyn.android.domain.repository.userAuth.UserSessionRepository

class UserSignOutUseCase(private val repository: UserSessionRepository) {
    suspend fun invoke() = repository.signOut()
}