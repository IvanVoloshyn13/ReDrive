package com.example.redrive.presentation.auth.signIn

data class FragmentSignInState(
    val loading: Boolean = false,
    val email: String = "",
    val password: String = "",

)
