package voloshyn.android.domain.repository.userAuth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.UserCredentials
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.models.auth.SignUpStatus

interface EmailAuthRepository {
    // Signs in with email and password
    suspend fun signInWithEmailAndPassword(email: String, password: String): AppResult<SignInStatus, AuthenticationError.AuthError>

    // Registers a new user with email, password, and additional credentials like displayName
    suspend fun signUpWithEmail(credentials: UserCredentials): AppResult<SignUpStatus, AuthenticationError.AuthError>

    // Sends a password reset email to the provided address
    suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing>
}