package voloshyn.android.domain.useCase.auth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.UserCredentials
import voloshyn.android.domain.models.auth.SignUpStatus
import voloshyn.android.domain.repository.userAuth.EmailAuthRepository

class SignUpWithEmailUseCase(private val authRepository: EmailAuthRepository) {
    suspend fun invoke(credentials: UserCredentials): AppResult<SignUpStatus,AuthenticationError.AuthError> {
        return authRepository.signUpWithEmail(credentials)
    }
}