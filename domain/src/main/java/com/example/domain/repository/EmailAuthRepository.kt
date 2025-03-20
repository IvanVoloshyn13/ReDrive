package com.example.domain.repository

import com.example.domain.appResult.AppResult
import com.example.domain.appResult.AuthError
import com.example.domain.model.SignInStatus
import com.example.domain.model.UserAuthCredentials

interface EmailAuthRepository {

    // Signs in with email and password
    suspend fun signInWithEmailAndPassword(email: String, password: String): AppResult<SignInStatus, AuthError>

    // Registers a new user with email, password, and additional credentials like displayName
    suspend fun signUpWithEmail(credentials: UserAuthCredentials): AppResult<SignInStatus, AuthError>

    // Sends a password reset email to the provided address
    suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing>
}