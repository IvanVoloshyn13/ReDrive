package com.example.domain.repository

import com.example.domain.model.SignInStatus
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow


interface UserSessionRepository {

    // Observes the current authenticated user, emitting updates as the authentication state changes
    fun observeCurrentUser(): Flow<User?>

    fun observeCurrentUserId(): Flow<String?>

    // Logs out the current user
    suspend fun signOut()


}