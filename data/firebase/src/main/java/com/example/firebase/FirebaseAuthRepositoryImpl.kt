package com.example.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : FirebaseAuthRepository {

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user
        return firebaseUser ?: throw NullPointerException()
    }

     override suspend fun signUpWithEmail(credentials: FbUserAuthCredentials): FirebaseUser {
        val result =
            auth.createUserWithEmailAndPassword(credentials.email, credentials.password)
                .await()
        result.user?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(credentials.fullName).build()
        )?.await()
        val firebaseUser = result.user
        return firebaseUser ?: throw NullPointerException()
    }

    override suspend fun sendPasswordReset(email: String) {
        TODO("Not yet implemented")
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