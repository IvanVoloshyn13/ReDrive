package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.repository.account.UserSessionRepository

class IsUserSignInUseCase(private val repository: UserSessionRepository) {
    fun invoke(): SignInStatus {
        return repository.isUserSignedIn()
    }
}