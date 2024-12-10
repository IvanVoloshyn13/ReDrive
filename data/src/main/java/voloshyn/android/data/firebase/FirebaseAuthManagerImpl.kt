package voloshyn.android.data.firebase

import com.google.android.gms.tasks.Task
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
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).onTaskAwait()
            val firebaseUser = result.user
            firebaseUser
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw e
        } catch (e: FirebaseAuthInvalidUserException) {
            throw e
        }
    }

    override suspend fun signUpWithEmail(credentials: UserCredentials): FirebaseUser? {
        var currentUser: FirebaseUser? = null
        return try {
            val result =
                auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
                    .onTaskAwait()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(credentials.fullName).build()
            )?.onTaskAwait()
            val firebaseUser = result.user
            firebaseUser?.let {
                currentUser = it
            }
            currentUser
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw e  // TODO() need some logs later
        } catch (e: FirebaseAuthUserCollisionException) {
            throw e
        } catch (e: FirebaseAuthException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing> {
        TODO("Not yet implemented")
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