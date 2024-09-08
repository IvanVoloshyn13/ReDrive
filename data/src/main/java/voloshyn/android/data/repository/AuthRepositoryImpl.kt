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
import voloshyn.android.data.safeLocalCall
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
    ): AppResult<UserTuple, AuthError.Auth> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).awaitOn()
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
            AppResult.Error(error = AuthError.Auth.INVALID_CREDENTIALS)
        } catch (e: FirebaseAuthInvalidUserException) {
            AppResult.Error(error = AuthError.Auth.NO_USER_DETECTED)
        }
    }

    override suspend fun signUp(user: User): AppResult<UserTuple, AuthError.Auth> {
        var currentUser = UserTuple.EMPTY_USER
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email, user.password).awaitOn()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(user.fullName).build()
            )?.awaitOn()
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
            AppResult.Error(error = AuthError.Auth.INVALID_CREDENTIALS)
        } catch (e: FirebaseAuthUserCollisionException) {
            AppResult.Error(error = AuthError.Auth.USER_COLLISION)
        } catch (e: FirebaseAuthException) {
            AppResult.Error(error = AuthError.Auth.FIREBASE_AUTH_ERROR)
        } catch (e: Exception) {
            AppResult.Error(error = AuthError.Auth.UNKNOWN_ERROR)
        }

    }

    override suspend fun forgotPassword() {
        TODO("Not yet implemented")
    }

    override suspend fun rememberMe(rememberMe: Boolean) {
        safeLocalCall {
            dataStore.edit {
                it[PreferencesKeys.REMEMBER_ME] = rememberMe
            }
        }
    }

    override suspend fun logout() {
        TODO("$currentUser must be null in this method")
        TODO("Not yet implemented")

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
suspend fun <T> Task<T>.awaitOn(): T {
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