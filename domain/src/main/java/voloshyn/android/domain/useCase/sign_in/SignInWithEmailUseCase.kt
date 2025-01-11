package voloshyn.android.domain.useCase.sign_in

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.repository.userAuth.EmailAuthRepository

class SignInWithEmailUseCase(private val authRepository: EmailAuthRepository) {
    suspend fun invoke(
        email: String,
        password: String
    ): AppResult<SignInStatus, AuthenticationError.AuthError> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}