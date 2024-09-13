package voloshyn.android.domain.repository

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.models.tabs.profile.User
import voloshyn.android.domain.models.tabs.profile.UserTuple

interface AuthRepository {

    val currentUser: UserTuple

    suspend fun signIn(email: String, password: String): AppResult<UserTuple, AuthError.FirebaseAuth>

    suspend fun signUp(user: User): AppResult<UserTuple, AuthError.FirebaseAuth>

    suspend fun forgotPassword()

    suspend fun rememberMe(rememberMe:Boolean)

    suspend fun logout()

    fun isSignedIn(): AppResult<UserTuple?, AuthError.FirebaseAuth>


}