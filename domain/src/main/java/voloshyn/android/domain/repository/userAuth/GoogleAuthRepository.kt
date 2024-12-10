package voloshyn.android.domain.repository.userAuth

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.User

interface GoogleAuthRepository {
    // Signs in with Google using an ID token
    suspend fun signInWithGoogle(idToken: String): AppResult<User, AuthenticationError>
}