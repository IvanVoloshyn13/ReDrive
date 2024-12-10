package voloshyn.android.domain.useCase.user

import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.repository.userAuth.UserSessionRepository

class IsUserSignInUseCase(private val repository: UserSessionRepository) {
    fun invoke(): SignInStatus {
        return repository.isSignedIn()
    }
}