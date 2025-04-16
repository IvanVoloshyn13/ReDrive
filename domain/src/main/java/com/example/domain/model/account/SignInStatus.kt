package com.example.domain.model.account

sealed interface SignInStatus {
    data object SignedIn : SignInStatus
    data object SignOut : SignInStatus
    data object Failure: SignInStatus
}