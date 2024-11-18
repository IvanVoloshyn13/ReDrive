package voloshyn.android.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.account.UserSessionRepository
import javax.inject.Inject

class UserSessionRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : UserSessionRepository {
    private var currentUser: User = User.EMPTY_USER

    override fun observeCurrentUser(): Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            val currentUser = it.currentUser
            val user = currentUser?.let {
                User(
                    id = currentUser.uid,
                    email = currentUser.email!!,
                    fullName = currentUser.displayName!!
                )
            }
            trySend(user)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun signOut(): AppResult<Unit, Nothing> {
        TODO("Not yet implemented")
        TODO("$currentUser must be null in this method")
        TODO("Not yet implemented")
    }

    override fun isUserSignedIn(): SignInStatus {
        val firebaseUser = auth.currentUser
        return if (firebaseUser != null) {
            SignInStatus.SignIn(user = firebaseUser.toUser())
        } else SignInStatus.SignOut
    }


}


fun FirebaseUser.toUser(): User {
    return User(id = this.uid, email = this.email!!, fullName = this.displayName!!)
}




