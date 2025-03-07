package com.example.domain.appResult

enum class AuthException:AppException {
    USER_NOT_FOUND,INVALID_PASSWORD,UNKNOWN_ERROR,USER_ALREADY_EXISTS,AUTHENTICATION_FAILED
}