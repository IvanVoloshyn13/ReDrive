package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.models.UserTuple
import voloshyn.android.domain.repository.AuthRepository

class SignInUseCase(private val repository: AuthRepository) {
    suspend fun invoke(email: String, password: String): AppResult<UserTuple, AuthError.Auth> {
        return repository.signIn(email, password)
    }
}