package voloshyn.android.domain.repository

import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.models.User
import voloshyn.android.domain.models.UserTuple

interface AuthRepository {

    val currentUser: UserTuple

    suspend fun signIn(email: String, password: String): AppResult<UserTuple, AuthError.Auth>

    suspend fun signUp(user: User): AppResult<UserTuple, AuthError.Auth>

    suspend fun forgotPassword()

    suspend fun rememberMe(rememberMe:Boolean)

    suspend fun logout()


}