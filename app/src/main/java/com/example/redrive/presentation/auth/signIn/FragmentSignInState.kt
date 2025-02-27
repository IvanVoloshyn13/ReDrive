package com.example.redrive.presentation.auth.signIn

import androidx.annotation.StringRes
import com.example.domain.model.SignInStatus

const val NO_STRING_RES = 0

data class FragmentSignInState(
    val loading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val signInStatus: SignInStatus = SignInStatus.SignOut,
    val isError: Boolean = false,
    @StringRes val errorMessage: Int = NO_STRING_RES
)
