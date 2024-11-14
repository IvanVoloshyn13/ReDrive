package voloshyn.android.domain.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.Credentials
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.models.auth.User

interface AuthRepository {

    // Observes the current authenticated user, emitting updates as the authentication state changes
    fun observeCurrentUser(): Flow<User?>

    // Signs in with email and password
    suspend fun signInWithEmail(email: String, password: String): AppResult<User, AuthenticationError>

    // Signs in with Google using an ID token
    suspend fun signInWithGoogle(idToken: String): AppResult<User, AuthenticationError>

    // Registers a new user with email, password, and additional credentials like displayName
    suspend fun signUpWithEmail(credentials: Credentials): AppResult<User, AuthenticationError.AuthError>

    // Sends a password reset email to the provided address
    suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing>

    // Optionally remembers the user session (managed by the repository)
    suspend fun setRememberMe(rememberMe: Boolean)

    // Logs out the current user
    suspend fun signOut(): AppResult<Unit, Nothing>

    // Checks if there's a currently signed-in user
    fun isUserSignedIn(): SignInStatus

}
