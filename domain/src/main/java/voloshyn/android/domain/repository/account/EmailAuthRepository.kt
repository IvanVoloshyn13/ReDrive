package voloshyn.android.domain.repository.account

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.Credentials
import voloshyn.android.domain.models.auth.User

interface EmailAuthRepository {
    // Signs in with email and password
    suspend fun signInWithEmailPassword(email: String, password: String): AppResult<User, AuthenticationError.AuthError>

    // Registers a new user with email, password, and additional credentials like displayName
    suspend fun signUpWithEmailPassword(credentials: Credentials): AppResult<User, AuthenticationError.AuthError>

    // Sends a password reset email to the provided address
    suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing>
}