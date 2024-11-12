package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.Credentials
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.AuthRepository

class SignUpWithEmailUseCase(private val repository: AuthRepository) {
    suspend fun invoke(credentials: Credentials): AppResult<User,AuthenticationError> {
        return repository.signUpWithEmail(credentials)
    }
}