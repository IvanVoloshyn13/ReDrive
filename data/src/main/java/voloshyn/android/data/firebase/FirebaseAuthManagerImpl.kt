package voloshyn.android.data.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.UserCredentials
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class FirebaseAuthManagerImpl @Inject constructor(
    private val auth: FirebaseAuth
) : FirebaseAuthManager {

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, password).onTaskAwait()
        val firebaseUser = result.user
        return firebaseUser
    }

    override suspend fun signUpWithEmail(credentials: UserCredentials): FirebaseUser? {
        val result =
            auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
                .onTaskAwait()
        result.user?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(credentials.fullName).build()
        )?.onTaskAwait()
        val firebaseUser = result.user
        return firebaseUser
    }

    override suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing> {
        TODO("Not yet implemented")
    }

    override fun toAppError(e: FirebaseException): AppResult<Nothing, AuthenticationError.AuthError> {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> AppResult.Error(error = AuthenticationError.AuthError.INVALID_CREDENTIALS)
            is FirebaseAuthInvalidUserException -> AppResult.Error(error = AuthenticationError.AuthError.USER_NOT_FOUND)
            is FirebaseAuthUserCollisionException -> AppResult.Error(error = AuthenticationError.AuthError.USER_ALREADY_EXISTS)
            is FirebaseAuthException -> AppResult.Error(error = AuthenticationError.AuthError.AUTHENTICATION_FAILED)
            else -> AppResult.Error(error = AuthenticationError.AuthError.UNKNOWN_ERROR)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun <T> Task<T>.onTaskAwait(): T {
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