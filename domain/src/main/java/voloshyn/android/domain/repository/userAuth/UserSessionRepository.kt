package voloshyn.android.domain.repository.userAuth

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.models.auth.User

interface UserSessionRepository {
    val user: User

    // Observes the current authenticated user, emitting updates as the authentication state changes
    fun observeCurrentUser(): Flow<User?>

    // Logs out the current user
    suspend fun signOut(): AppResult<Unit, Nothing>

    // Checks if there's a currently signed-in user
    fun isSignedIn(): SignInStatus
}