package voloshyn.android.data.firebase

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.AuthenticationError
import voloshyn.android.domain.models.auth.UserCredentials

interface FirebaseAuthManager {

    // Signs in with email and password
    suspend fun signInWithEmail(email: String, password: String): FirebaseUser?

    // Registers a new user with email, password, and additional credentials like displayName
    suspend fun signUpWithEmail(credentials: UserCredentials): FirebaseUser?

    // Sends a password reset email to the provided address
    suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing>

    fun toAppError(e:FirebaseException): AppResult<Nothing, AuthenticationError.AuthError>


    }