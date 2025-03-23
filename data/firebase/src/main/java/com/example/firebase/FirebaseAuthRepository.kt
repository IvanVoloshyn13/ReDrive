package com.example.firebase

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {

    // Signs in with email and password


    suspend fun signInWithEmail(email: String, password: String): FirebaseUser

    // Registers a new user with email, password, and additional credentials like displayName
    suspend fun signUpWithEmail(credentials: FbUserAuthCredentials): FirebaseUser

    suspend fun signOut()

    // Sends a password reset email to the provided address
    suspend fun sendPasswordReset(email: String)

    fun getAuthState(): Flow<FirebaseUser?>

}