package voloshyn.android.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import voloshyn.android.data.firebase.FirebaseAuthManager
import voloshyn.android.data.firebase.onTaskAwait
import voloshyn.android.data.localeStorage.room.dao.AccountsDao
import voloshyn.android.data.localeStorage.room.entities.AccountEntity
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.Credentials
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.account.EmailAuthRepository
import javax.inject.Inject

class EmailAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseAuthManager: FirebaseAuthManager,
    private val accountsDao: AccountsDao
) : EmailAuthRepository {

    private var currentUser: User = User.EMPTY_USER

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): AppResult<User, AuthenticationError.AuthError> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).onTaskAwait()
            val firebaseUser = result.user
            firebaseUser?.let {
                currentUser = User(
                    id = it.uid,
                    fullName = it.displayName!!,
                    email = it.email!!
                )
            }
            AppResult.Success(data = currentUser)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AppResult.Error(error = AuthenticationError.AuthError.INVALID_CREDENTIALS)

        } catch (e: FirebaseAuthInvalidUserException) {
            AppResult.Error(error = AuthenticationError.AuthError.USER_NOT_FOUND)

        }
    }

    override suspend fun signUpWithEmailPassword(credentials: Credentials): AppResult<User, AuthenticationError.AuthError> {
        var currentUser = User.EMPTY_USER
        return try {
            val firebaseUser = firebaseAuthManager.signUpWithEmail(credentials)

            if (firebaseUser != null) {
                accountsDao.addAccount(AccountEntity.toEntity(firebaseUser))
                currentUser = firebaseUser.toUser()
            }

            AppResult.Success(data = currentUser)

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

    override suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing> {
        TODO("Not yet implemented")
    }
}