package voloshyn.android.domain.useCase.user

import voloshyn.android.domain.repository.userAuth.UserSessionRepository

class ObserveCurrentUserUseCase(private val repository: UserSessionRepository) {

    fun invoke() = repository.observeCurrentUser()

}