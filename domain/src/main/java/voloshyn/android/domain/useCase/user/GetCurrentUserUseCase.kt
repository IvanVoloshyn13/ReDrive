package voloshyn.android.domain.useCase.user

import voloshyn.android.domain.repository.userAuth.UserSessionRepository

/**Use this useCase only after Success SignIn or SignUp !!*/

class GetCurrentUserUseCase(private val repository: UserSessionRepository) {
    fun invoke() = repository.user
}