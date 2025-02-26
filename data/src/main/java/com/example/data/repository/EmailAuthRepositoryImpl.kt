package com.example.data.repository

import com.example.data.di.DispatcherIo
import com.example.data.firebase.FirebaseAuthManager
import com.example.domain.appResult.AppResult
import com.example.domain.appResult.AuthException
import com.example.domain.model.SignInStatus
import com.example.domain.model.UserAuthCredentials
import com.example.domain.repository.EmailAuthRepository
import com.google.firebase.FirebaseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmailAuthRepositoryImpl @Inject constructor(
    private val firebaseAuthManager: FirebaseAuthManager,
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher
) : EmailAuthRepository {
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AppResult<SignInStatus, AuthException> {
        return withContext(dispatcherIo) {
            try {
                val firebaseUser = firebaseAuthManager.signInWithEmail(email, password)
                AppResult.Success(result = SignInStatus.SignedIn)
            } catch (e: FirebaseException) {
                AppResult.Error(exception = firebaseAuthManager.toAppError(e))
            } catch (e: NullPointerException) {
                AppResult.Error(exception = AuthException.USER_NOT_FOUND)
            } catch (e: Exception) {
                AppResult.Error(exception = AuthException.UNKNOWN_ERROR)
            }
        }
    }

    override suspend fun signUpWithEmail(credentials: UserAuthCredentials): AppResult<SignInStatus, AuthException> {
        TODO("Not yet implemented")
    }

    override suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing> {
        TODO("Not yet implemented")
    }
}