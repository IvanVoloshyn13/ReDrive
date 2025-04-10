package com.example.data

import com.example.domain.AppException
import com.example.domain.AuthException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

fun FirebaseException.toAppAuthException(): AppException {
    return when (this) {
        is FirebaseAuthInvalidCredentialsException -> AuthException.InvalidPasswordException()
        is FirebaseAuthInvalidUserException -> AuthException.UserNotFoundException()
        is FirebaseAuthUserCollisionException -> AuthException.UserAlreadyExistsException()
        is FirebaseAuthException -> AuthException.AuthenticationFailed()
        else -> AuthException.UnknownException()

    }

}