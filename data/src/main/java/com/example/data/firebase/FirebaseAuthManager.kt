package com.example.data.firebase

import com.example.domain.appResult.AppResult
import com.example.domain.appResult.AuthException
import com.example.domain.model.UserAuthCredentials
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthManager {

    // Signs in with email and password
    suspend fun signInWithEmail(email: String, password: String): FirebaseUser

    // Registers a new user with email, password, and additional credentials like displayName
    suspend fun signUpWithEmail(credentials: UserAuthCredentials): FirebaseUser

    // Sends a password reset email to the provided address
    suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing>

    fun toAppError(e:FirebaseException): AuthException


    }