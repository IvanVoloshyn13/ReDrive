package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.repository.AuthRepository

class SignInStatusUseCase(private val repository: AuthRepository) {
    fun invoke(): SignInStatus {
        return repository.isUserSignedIn()
    }
}