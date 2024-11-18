package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.Credentials
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.account.EmailAuthRepository

class SignUpWithEmailUseCase(private val authRepository: EmailAuthRepository) {
    suspend fun invoke(credentials: Credentials): AppResult<User,AuthenticationError.AuthError> {
        return authRepository.signUpWithEmailPassword(credentials)
    }
}