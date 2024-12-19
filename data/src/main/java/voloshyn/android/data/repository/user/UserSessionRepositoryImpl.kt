package voloshyn.android.data.repository.user

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.userAuth.UserSessionRepository
import javax.inject.Inject

class UserSessionRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val currentUserRepository: AppCurrentUserRepository
) : UserSessionRepository {
    override val user: User
        get() = currentUserRepository.user

    override fun observeCurrentUser(): Flow<User?> {
        return currentUserRepository.observeCurrentUser()
    }

    //TODO move this method to AppCurrentUserRepo also save SignInMethod for signOut
    override suspend fun signOut() {
        auth.signOut()
        currentUserRepository.clearUserUid()
    }

    override fun isSignedIn(): SignInStatus {
        val firebaseUser = auth.currentUser
        return if (firebaseUser != null) {
            SignInStatus.SignIn
        } else SignInStatus.SignOut
    }

}





