package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.account.EmailAuthRepository

class SignInWithEmailUseCase(private val authRepository: EmailAuthRepository) {
    suspend fun invoke(
        email: String,
        password: String
    ): AppResult<User, AuthenticationError.AuthError> {
        return authRepository.signInWithEmailPassword(email, password)
    }
}