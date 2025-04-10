package com.example.data

import com.example.domain.model.User
import com.example.domain.model.UserAuthCredentials
import com.example.firebase.FbUserAuthCredentials
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

fun UserAuthCredentials.toFbUserAuthCredentials(): FbUserAuthCredentials {
    return FbUserAuthCredentials(
        password = password,
        email = email,
        fullName = fullName
    )
}