package com.example.data.repository

import android.util.Log
import com.example.data.di.DispatcherIo
import com.example.data.toUser
import com.example.domain.model.User
import com.example.domain.repository.UserSessionRepository
import com.example.firebase.FirebaseAuthRepository
import com.example.localedatasource.dataStore.AppUserPreferences
import com.example.localedatasource.room.UsersDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserSessionRepositoryImpl @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val usersDao: UsersDao,
    private val appUserPreferences: AppUserPreferences,
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher
) : UserSessionRepository {

    override fun observeAuthState(): Flow<User?> {
        return firebaseAuthRepository.getAuthState().map {
            toggleUserIdPreferences(it?.uid ?: "")
            it?.toUser()
        }
    }

    override fun observeCurrentUserId(): Flow<String?> {
        return appUserPreferences.observeUserId()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeCurrentUser(): Flow<User?> {
        return appUserPreferences.observeUserId().flatMapLatest {
            if (it != null) {
                usersDao.observeCurrentUser(it).map {
                    return@map it!!.toUser()
                }
            } else return@flatMapLatest flowOf(null)
        }
    }

    override suspend fun signOut() {
        firebaseAuthRepository.signOut()
    }

    private suspend fun toggleUserIdPreferences(uUid: String) = withContext(dispatcherIo) {
        if (uUid.isEmpty()) {
            appUserPreferences.clearCurrentUserId()
        } else appUserPreferences.setCurrentUserId(uUid)
    }

}