package com.example.data

import com.example.domain.appResult.AuthException
import com.example.localedatasource.room.UserEntity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUserEntity(): UserEntity {
    return UserEntity(
        id = uid,
        email = email!!,
        fullName = displayName!!
    )
}

fun FirebaseException.toAppError(e: FirebaseException): AuthException {
    return when (e) {
        is FirebaseAuthInvalidCredentialsException -> AuthException.INVALID_PASSWORD
        is FirebaseAuthInvalidUserException -> AuthException.USER_NOT_FOUND
        is FirebaseAuthUserCollisionException -> TODO() // AuthException.USER_ALREADY_EXISTS
        is FirebaseAuthException -> TODO()// AuthException.AUTHENTICATION_FAILED
        else -> AuthException.UNKNOWN_ERROR

    }

}