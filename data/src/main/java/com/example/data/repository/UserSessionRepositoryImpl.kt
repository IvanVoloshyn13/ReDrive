package com.example.data.repository

import com.example.data.di.DispatcherIo
import com.example.data.toUser
import com.example.domain.model.User
import com.example.domain.repository.UserSessionRepository
import com.example.firebase.FirebaseAuthRepository
import com.example.localedatasource.dataStore.AppUserPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserSessionRepositoryImpl @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val appUserPreferences: AppUserPreferences,
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher
) : UserSessionRepository {

    override fun observeCurrentUser(): Flow<User?> {
        return firebaseAuthRepository.getAuthState().map {
            toggleUserIdPreferences(it?.uid ?: "")
            it?.toUser()
        }
    }

    override fun observeCurrentUserId(): Flow<String?> {
        return appUserPreferences.observeUserId()
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