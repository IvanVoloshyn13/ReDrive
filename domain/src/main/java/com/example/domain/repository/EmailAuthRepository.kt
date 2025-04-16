package com.example.domain.repository


import com.example.domain.AuthException
import com.example.domain.model.account.UserAuthCredentials

interface EmailAuthRepository {
    /** Signs in with email and password
     * @throws AuthException.UserNotFoundException if the user account corresponding to email does not exist or has been disabled
     * @throws AuthException.InvalidPasswordException if the password is wrong*/
    @Throws(
        AuthException.UserNotFoundException::class,
        AuthException.InvalidPasswordException::class
    )
    suspend fun signInWithEmailAndPassword(email: String, password: String)

    /** Registers a new user with email, password, and additional credentials like displayName
     * @throws AuthException.UserAlreadyExistsException if there already exists an account with the given email address  */
    @Throws(AuthException.UserAlreadyExistsException::class)
    suspend fun signUpWithEmail(credentials: UserAuthCredentials)


    /** Sends a password reset email to the provided address
     * @throws AuthException.UserNotFoundException if there is no user corresponding to the given email address
     */
    @Throws(AuthException.UserNotFoundException::class)
    suspend fun sendPasswordReset(email: String)
}