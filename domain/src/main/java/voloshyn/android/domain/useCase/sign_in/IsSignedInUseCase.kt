package voloshyn.android.domain.useCase.sign_in

import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.repository.userAuth.UserSessionRepository

class IsSignedInUseCase(private val repository: UserSessionRepository) {
    fun invoke(): SignInStatus {
        return repository.isSignedIn()
    }
}