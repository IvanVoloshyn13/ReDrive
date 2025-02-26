package com.example.data.firebase

import com.example.domain.appResult.AppResult
import com.example.domain.appResult.AuthException
import com.example.domain.model.UserAuthCredentials
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

import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class FirebaseAuthManagerImpl @Inject constructor(
    private val auth: FirebaseAuth
) : FirebaseAuthManager {

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).onTaskAwait()
        val firebaseUser = result.user
        return firebaseUser?:throw NullPointerException()
    }

    override suspend fun signUpWithEmail(credentials: UserAuthCredentials): FirebaseUser {
        val result =
            auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
                .onTaskAwait()
        result.user?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(credentials.fullName).build()
        )?.onTaskAwait()
        val firebaseUser = result.user
        return firebaseUser?:throw NullPointerException()
    }

    override suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing> {
        TODO("Not yet implemented")
    }

    override fun toAppError(e: FirebaseException): AuthException {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> AuthException.INVALID_PASSWORD
            is FirebaseAuthInvalidUserException -> AuthException.USER_NOT_FOUND
            is FirebaseAuthUserCollisionException -> TODO() // AuthException.USER_ALREADY_EXISTS
            is FirebaseAuthException -> TODO()// AuthException.AUTHENTICATION_FAILED
            else -> AuthException.UNKNOWN_ERROR

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

}