package voloshyn.android.data.repository.user

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.firebase.FirebaseAuthManager
import voloshyn.android.data.localeStorage.room.dao.UsersDao
import voloshyn.android.data.localeStorage.room.entities.UserEntity
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.models.auth.SignUpStatus
import voloshyn.android.domain.models.auth.UserCredentials
import voloshyn.android.domain.repository.userAuth.EmailAuthRepository
import javax.inject.Inject

class EmailAuthRepositoryImpl @Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    private val usersDao: UsersDao,
    private val currentUserRepository: AppCurrentUserRepository,
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher
) : EmailAuthRepository, AppCurrentUserRepository by currentUserRepository {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AppResult<SignInStatus, AuthenticationError.AuthError> {
        return withContext(dispatcherIo) {
            try {
                val firebaseUser = firebaseAuthManager.signInWithEmail(email, password)

                firebaseUser?.let { fbUser ->
                    val userEntity = usersDao.currentUser(fbUser.uid).first()
                    if (userEntity == null) {
                        createAndSaveUser(fbUser)
                    } else {
                        currentUserRepository.setUserUid(fbUser.uid)
                    }
                }
                AppResult.Success(data = SignInStatus.SignIn)
            } catch (e: FirebaseException) {
                firebaseAuthManager.toAppError(e)
            } catch (e: Exception) {
                AppResult.Error(error = AuthenticationError.AuthError.UNKNOWN_ERROR)
            }
        }
    }

    override suspend fun signUpWithEmail(
        credentials: UserCredentials
    ): AppResult<SignUpStatus, AuthenticationError.AuthError> {
        return withContext(dispatcherIo) {
            try {
                val firebaseUser = firebaseAuthManager.signUpWithEmail(credentials)
                if (firebaseUser != null) {
                    createAndSaveUser(firebaseUser)
                }
                AppResult.Success(data = SignUpStatus.SignUp)
            } catch (e: FirebaseException) {
                firebaseAuthManager.toAppError(e)
            } catch (e: Exception) {
                AppResult.Error(error = AuthenticationError.AuthError.UNKNOWN_ERROR)

            }
        }
    }

    private suspend fun createAndSaveUser(firebaseUser: FirebaseUser) {
        usersDao.addUser(UserEntity.toEntity(firebaseUser))
        currentUserRepository.setUserUid(firebaseUser.uid)
    }

    override suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing> {
        TODO("Not yet implemented")
    }
}