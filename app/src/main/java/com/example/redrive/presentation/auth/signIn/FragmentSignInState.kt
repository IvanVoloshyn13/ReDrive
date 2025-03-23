package com.example.redrive.presentation.auth.signIn

import com.example.domain.model.SignInStatus

data class FragmentSignInState(
    val loading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val signInStatus: SignInStatus = SignInStatus.SignOut,
    val isError: Boolean = false,
    val errorMessage: String = ""
)
