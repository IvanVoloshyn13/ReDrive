package com.example.data.repository

import android.util.Log
import com.example.data.di.DispatcherIo
import com.example.data.toAppAuthException
import com.example.data.toFbUserAuthCredentials
import com.example.firebase.FirebaseAuthRepository
import com.example.data.toUserEntity
import com.example.domain.AuthException
import com.example.domain.model.UserAuthCredentials
import com.example.domain.repository.EmailAuthRepository
import com.example.localedatasource.room.daos.UsersDao
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmailAuthRepositoryImpl @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher,
    private val usersDao: UsersDao
) : EmailAuthRepository {
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ) {
        return withContext(dispatcherIo) {
            try {
                val fbUser = firebaseAuthRepository.signInWithEmail(email, password)
                saveNewUserToLocalDb(fbUser)
            } catch (e: FirebaseException) {
                val appException = e.toAppAuthException()
                throw appException
            } catch (e: NullPointerException) {
                throw AuthException.UserNotFoundException()
            } catch (e: Exception) {
                throw AuthException.UnknownException()
            }
        }

    }

    override suspend fun signUpWithEmail(credentials: UserAuthCredentials) {
        return withContext(dispatcherIo) {
            try {
                val fbUser =
                    firebaseAuthRepository.signUpWithEmail(credentials.toFbUserAuthCredentials())
                saveNewUserToLocalDb(fbUser)
            } catch (e: FirebaseException) {
                throw e.toAppAuthException()
            } catch (e: NullPointerException) {
                throw AuthException.UserNotFoundException()
            } catch (e: Exception) {
                throw AuthException.UnknownException()
            }
        }
    }

    private suspend fun saveNewUserToLocalDb(fbUser: FirebaseUser) {
        val userEntity = usersDao.observeCurrentUser(fbUser.uid).firstOrNull()
        if (userEntity != null) {
            return
        } else {
            usersDao.saveNewUser(fbUser.toUserEntity())
        }
    }

    override suspend fun sendPasswordReset(email: String) {
        TODO("Not yet implemented")
    }


}