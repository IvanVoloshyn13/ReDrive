package com.example.domain.repository

import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow


interface UserSessionRepository {

    /**
     * Observes the authentication state of the current user.
     * Emits the current `User` object when authenticated, or `null` if not authenticated.
     */
    fun observeAuthState(): Flow<User?>

    /**
     * Observes the ID of the current user.
     * Emits the current user's ID as a string, or `null` if no user is signed in.
     */
    fun observeCurrentUserId(): Flow<String?>

    /**
     * Observes the current user details.
     * Emits the `User` object representing the current user, or `null` if no user is signed in.
     */
    fun observeCurrentUser(): Flow<User?>

    /**
     * Signs out the currently authenticated user.
     * This will clear the session and log the user out.
     */
    suspend fun signOut()


}