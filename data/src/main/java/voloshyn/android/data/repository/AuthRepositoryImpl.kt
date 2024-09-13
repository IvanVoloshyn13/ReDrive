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
import kotlinx.coroutines.suspendCancellableCoroutine
import voloshyn.android.data.dataSource.datastorePreferences.PreferencesKeys
import voloshyn.android.data.safeUpdateData
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthError
import voloshyn.android.domain.models.tabs.profile.User
import voloshyn.android.domain.models.tabs.profile.UserTuple
import voloshyn.android.domain.repository.AuthRepository
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    init {
        currentFirebaseUser()
    }

    override lateinit var currentUser: UserTuple


    override suspend fun signIn(
        email: String,
        password: String
    ): AppResult<UserTuple, AuthError.FirebaseAuth> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).onTaskAwait()
            val firebaseUser = result.user
            firebaseUser?.let {
                currentUser = UserTuple(
                    id = it.uid,
                    fullName = it.displayName!!,
                    email = it.email!!
                )
            }
            AppResult.Success(data = currentUser)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AppResult.Error(error = AuthError.FirebaseAuth.INVALID_CREDENTIALS)
        } catch (e: FirebaseAuthInvalidUserException) {
            AppResult.Error(error = AuthError.FirebaseAuth.NO_USER_DETECTED)
        }
    }

    override suspend fun signUp(user: User): AppResult<UserTuple, AuthError.FirebaseAuth> {
        var currentUser = UserTuple.EMPTY_USER
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email, user.password).onTaskAwait()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(user.fullName).build()
            )?.onTaskAwait()
            val firebaseUser = result.user
            firebaseUser?.let {
                currentUser = UserTuple(
                    id = it.uid,
                    fullName = it.displayName!!,
                    email = it.email!!
                )
            }
            AppResult.Success(data = currentUser)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AppResult.Error(error = AuthError.FirebaseAuth.INVALID_CREDENTIALS)
        } catch (e: FirebaseAuthUserCollisionException) {
            AppResult.Error(error = AuthError.FirebaseAuth.USER_COLLISION)
        } catch (e: FirebaseAuthException) {
            AppResult.Error(error = AuthError.FirebaseAuth.FIREBASE_AUTH_ERROR)
        } catch (e: Exception) {
            AppResult.Error(error = AuthError.FirebaseAuth.UNKNOWN_ERROR)
        }

    }

    override suspend fun forgotPassword() {
        TODO("Not yet implemented")
    }

    override suspend fun rememberMe(rememberMe: Boolean) {
        safeUpdateData {
            dataStore.edit {
                it[PreferencesKeys.REMEMBER_ME] = rememberMe
            }
        }
    }

    override suspend fun logout() {
        TODO("$currentUser must be null in this method")
        TODO("Not yet implemented")

    }

    override fun isSignedIn(): AppResult<UserTuple?, AuthError.FirebaseAuth> {
        val firebaseUser = auth.currentUser
        return if (firebaseUser != null) {
            AppResult.Success(
                data = UserTuple(
                    id = firebaseUser.uid,
                    fullName = firebaseUser.displayName!!,
                    email = firebaseUser.email!!
                )
            )
        } else AppResult.Error(null, AuthError.FirebaseAuth.NO_USER_DETECTED)

    }

    private fun currentFirebaseUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            currentUser = UserTuple(
                id = firebaseUser.uid,
                fullName = firebaseUser.displayName!!,
                email = firebaseUser.email!!
            )
        }
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