package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.AuthRepository

class SignInWithEmailUseCase(private val repository: AuthRepository) {
    suspend fun invoke(email: String, password: String): AppResult<User, AuthenticationError> {
        return repository.signInWithEmail(email, password)
    }
}