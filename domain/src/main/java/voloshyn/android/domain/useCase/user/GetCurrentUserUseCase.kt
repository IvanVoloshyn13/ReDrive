package voloshyn.android.domain.useCase.user

import voloshyn.android.domain.repository.userAuth.UserSessionRepository

class GetCurrentUserUseCase(private val repository: UserSessionRepository) {
    fun invoke() = repository.user
}