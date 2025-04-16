package com.example.data.repository

import com.example.data.mappers.toUser
import com.example.domain.UserException
import com.example.domain.model.account.User
import com.example.domain.repository.UserSessionRepository
import com.example.firebase.FirebaseAuthRepository
import com.example.localedatasource.dataStore.AppUserPreferences
import com.example.localedatasource.dataStore.AppVehiclePreferences
import com.example.localedatasource.room.daos.UsersDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSessionRepositoryImpl @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val usersDao: UsersDao,
    private val appUserPreferences: AppUserPreferences,

    private val appVehiclePreferences: AppVehiclePreferences,
) : UserSessionRepository {

    override fun observeAuthState(): Flow<User?> {
        return firebaseAuthRepository.getAuthState().map {
            if (it?.uid == appUserPreferences.observeUserId().first()) {
                return@map it?.toUser()
            }
            toggleUserIdPreferences(it?.uid ?: "")
            it?.toUser()
        }
    }

    override fun observeCurrentUserId(): Flow<String?> {
        return try {
            appUserPreferences.observeUserId()
        } catch (e: NullPointerException) {
            throw UserException.NoUserDetectedException()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeCurrentUser(): Flow<User?> {
        return appUserPreferences.observeUserId().flatMapLatest {
            if (it != null) {
                usersDao.observeCurrentUser(it).map { entity ->
                    return@map entity!!.toUser()
                }
            } else return@flatMapLatest flowOf(null)
        }
    }

    override suspend fun signOut() {
        firebaseAuthRepository.signOut()
    }

    private suspend fun toggleUserIdPreferences(uUid: String) {
        if (uUid.isEmpty()) {
            appUserPreferences.clearCurrentUserId()

            //TODO(create a broadcastReceiver or some helper class to deal with SignIn and LogOut notifying )
            appVehiclePreferences.clearCurrentVehicleId()
        } else
            appUserPreferences.setCurrentUserId(uUid)
    }

}