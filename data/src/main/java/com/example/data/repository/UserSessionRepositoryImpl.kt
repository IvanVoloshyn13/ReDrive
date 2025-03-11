package com.example.data.repository

import com.example.data.toUser
import com.example.domain.model.User
import com.example.domain.repository.UserSessionRepository
import com.example.firebase.FirebaseAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSessionRepositoryImpl @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : UserSessionRepository {

    override fun observeCurrentUser(): Flow<User?> {
        return firebaseAuthRepository.getAuthState().map {
            it?.toUser()
        }
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

}