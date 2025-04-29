package com.example.data.mappers

import com.example.domain.model.account.User
import com.example.domain.model.account.UserAuthCredentials
import com.example.firebase.models.FbAuthCredentials
import com.example.firebase.models.FirebaseUserProfile
import com.example.localedatasource.room.entity.UserEntity
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUserEntity(): UserEntity {
    return UserEntity(
        id = uid,
        email = email!!,
        fullName = displayName!!
    )
}

fun FirebaseUser.toUser(): User {
    return User(
        uUid = this.uid,
        fullName = this.displayName ?: ""
    )
}

fun UserEntity.toUser(): User {
    return User(
        uUid = this.id,
        fullName = this.fullName
    )
}

fun UserAuthCredentials.toFbUserProfile(): FirebaseUserProfile {
    return FirebaseUserProfile(
        credentials = FbAuthCredentials(
            password = password,
            email = email,
        ),
        fullName = fullName
    )
}