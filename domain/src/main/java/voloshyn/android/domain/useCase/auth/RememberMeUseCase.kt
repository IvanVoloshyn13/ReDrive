package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.repository.AuthRepository

class RememberMeUseCase(private val repository: AuthRepository) {
    suspend fun invoke(rememberMe: Boolean) {
        repository.setRememberMe(rememberMe)
    }
}