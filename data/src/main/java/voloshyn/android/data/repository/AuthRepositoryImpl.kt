package voloshyn.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import voloshyn.android.data.localeStorage.datastorePreferences.PreferencesKeys
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.Credentials
import voloshyn.android.domain.models.auth.SignInStatus
import voloshyn.android.domain.models.auth.User
import voloshyn.android.domain.repository.AuthRepository
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {
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

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): AppResult<User, AuthenticationError> {
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

    override suspend fun signInWithGoogle(idToken: String): AppResult<User, AuthenticationError> {
        TODO("Not yet implemented")
    }

    override suspend fun signUpWithEmail(credentials: Credentials): AppResult<User, AuthenticationError> {
        var currentUser = User.EMPTY_USER
        return try {
            val result =
                auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
                    .onTaskAwait()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(credentials.fullName).build()
            )?.onTaskAwait()
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

    //TODO("Looks stupid for me)
    override suspend fun setRememberMe(rememberMe: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.REMEMBER_ME] = rememberMe
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
            SignInStatus.SignIn
        } else
            SignInStatus.SignOut
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.onTaskAwait(): T {
    return suspendCancellableCoroutine { continuation ->
        addOnCompleteListener {
            if (it.exception != null) {
                continuation.resumeWithException(it.exception!!)
            } else {
                continuation.resume(it.result, null)
            }
        }
    }
}