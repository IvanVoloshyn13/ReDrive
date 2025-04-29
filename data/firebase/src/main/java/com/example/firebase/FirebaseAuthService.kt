package com.example.firebase

import com.example.firebase.models.FirebaseUserProfile
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

interface FirebaseAuthService {

    /**
     * Authenticates the user with the provided email and password credentials.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return The authenticated [FirebaseUser] instance.
     * @throws FirebaseAuthInvalidCredentialsException If the email or password is invalid.
     * @throws FirebaseAuthInvalidUserException If there is no user corresponding to the given email.
     * @throws FirebaseAuthException For other Firebase authentication errors.
     */
    suspend fun signInWithEmail(email: String, password: String): FirebaseUser

    /**
     * Creates a new user account using email, password, and additional credentials like display name.
     *
     * @param profile A [FirebaseUserProfile] object containing the necessary registration information.
     * @return The newly created [FirebaseUser] instance.
     * @throws FirebaseAuthUserCollisionException If there already exists an account with the given email.
     * @throws FirebaseAuthWeakPasswordException If the provided password is not strong enough.
     * @throws FirebaseAuthInvalidCredentialsException If the email address is malformed.
     * @throws FirebaseAuthException For other Firebase authentication errors.
     */
    suspend fun signUpWithEmail(profile: FirebaseUserProfile): FirebaseUser

    /**
     * Signs out the currently logged-in user.
     */
    suspend fun signOut()

    /**
     * Sends a password reset email to the specified address.
     *
     * @param email The email address to which the reset link should be sent.
     * @throws FirebaseAuthInvalidUserException If there is no user corresponding to the given email.
     * @throws FirebaseAuthInvalidCredentialsException If the email is improperly formatted.
     * @throws FirebaseAuthException For other Firebase authentication errors.
     */
    suspend fun sendPasswordReset(email: String)

    /**
     * Observes changes in the authentication state.
     *
     * @return A [Flow] that emits the current [FirebaseUser], or null if no user is signed in.
     */
    fun getAuthState(): Flow<FirebaseUser?>


}