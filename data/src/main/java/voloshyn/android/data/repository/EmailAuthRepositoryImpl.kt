package voloshyn.android.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.firebase.FirebaseAuthManager
import voloshyn.android.data.firebase.onTaskAwait
import voloshyn.android.data.localeStorage.room.dao.UsersDao
import voloshyn.android.data.localeStorage.room.entities.UserEntity
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.UserCredentials
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.models.auth.SignUpStatus
import voloshyn.android.domain.repository.userAuth.EmailAuthRepository
import javax.inject.Inject

class EmailAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
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
                val result = auth.signInWithEmailAndPassword(email, password).onTaskAwait()
                val firebaseUser = result.user

                firebaseUser?.let { fbUser ->
                    val userEntity = usersDao.currentUser(fbUser.uid).first()
                    if (userEntity == null) {
                        createAndSaveUser(fbUser)
                    } else {
                        currentUserRepository.setUserUid(fbUser.uid)
                    }
                }
                AppResult.Success(data = SignInStatus.SignIn)
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                AppResult.Error(error = AuthenticationError.AuthError.INVALID_CREDENTIALS)
            } catch (e: FirebaseAuthInvalidUserException) {
                AppResult.Error(error = AuthenticationError.AuthError.USER_NOT_FOUND)
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
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                AppResult.Error(error = AuthenticationError.AuthError.INVALID_CREDENTIALS)
            } catch (e: FirebaseAuthUserCollisionException) {
                AppResult.Error(error = AuthenticationError.AuthError.USER_ALREADY_EXISTS)
            } catch (e: FirebaseAuthException) {
                AppResult.Error(error = AuthenticationError.AuthError.AUTHENTICATION_FAILED)
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