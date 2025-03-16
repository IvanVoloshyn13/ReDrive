package com.example.domain.repository

import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow


interface UserSessionRepository {

    // Observes the current authenticated user, emitting updates as the authentication state changes
    fun observeAuthState(): Flow<User?>

    fun observeCurrentUserId(): Flow<String?>

    fun observeCurrentUser(): Flow<User?>

    // Logs out the current user
    suspend fun signOut()


}