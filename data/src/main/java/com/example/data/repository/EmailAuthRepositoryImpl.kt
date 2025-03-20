package com.example.data.repository

import com.example.data.di.DispatcherIo
import com.example.data.toAppError
import com.example.data.toFbUserAuthCredentials
import com.example.firebase.FirebaseAuthRepository
import com.example.data.toUserEntity
import com.example.domain.appResult.AppResult
import com.example.domain.appResult.AuthError
import com.example.domain.model.SignInStatus
import com.example.domain.model.UserAuthCredentials
import com.example.domain.repository.EmailAuthRepository
import com.example.localedatasource.room.UsersDao
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmailAuthRepositoryImpl @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher,
    private val usersDao: UsersDao,
) : EmailAuthRepository {
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AppResult<SignInStatus, AuthError> {
        return withContext(dispatcherIo) {
            try {
                val fbUser = firebaseAuthRepository.signInWithEmail(email, password)
                saveNewUserToLocalDb(fbUser)
                AppResult.Success(result = SignInStatus.SignedIn)
            } catch (e: FirebaseException) {
                AppResult.Error(exception = e.toAppError(e))
            } catch (e: NullPointerException) {
                AppResult.Error(exception = AuthError.USER_NOT_FOUND)
            } catch (e: Exception) {
                AppResult.Error(exception = AuthError.UNKNOWN_ERROR)
            }
        }

    }

    override suspend fun signUpWithEmail(credentials: UserAuthCredentials): AppResult<SignInStatus, AuthError> {
        return withContext(dispatcherIo) {
            try {
                val fbUser =
                    firebaseAuthRepository.signUpWithEmail(credentials.toFbUserAuthCredentials())
                saveNewUserToLocalDb(fbUser)
                AppResult.Success(result = SignInStatus.SignedIn)
            } catch (e: FirebaseException) {
                AppResult.Error(exception = e.toAppError(e))
            } catch (e: NullPointerException) {
                AppResult.Error(exception = AuthError.USER_NOT_FOUND)
            } catch (e: Exception) {
                AppResult.Error(exception = AuthError.UNKNOWN_ERROR)
            }
        }
    }

    override suspend fun sendPasswordReset(email: String): AppResult<Unit, Nothing> {
        TODO("Not yet implemented")
    }

    private suspend fun saveNewUserToLocalDb(fbUser: FirebaseUser): Boolean {
        val userEntity = usersDao.observeCurrentUser(fbUser.uid).firstOrNull()
        return if (userEntity != null) {
            false
        } else {
            usersDao.saveNewUser(fbUser.toUserEntity())
            true
        }
    }
}