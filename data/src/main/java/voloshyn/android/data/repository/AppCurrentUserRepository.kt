package voloshyn.android.data.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.auth.User

interface AppCurrentUserRepository {
    val user: User
     fun observeCurrentUser(): Flow<User>
    suspend fun setUserUid(uid: String)
    suspend fun clearUserUid()
}