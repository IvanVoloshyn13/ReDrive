package com.example.redrive.presentation.profile

import androidx.annotation.StringRes
import com.example.domain.model.SignInStatus

const val NO_STRING_RES = 0

data class FragmentProfileState(
    val isLoading: Boolean = false,
    val userInitials: String = "",
    val signedInStatus: SignInStatus = SignInStatus.SignOut,
    val isError: Boolean = false,
    @StringRes val errorMessage: Int = NO_STRING_RES
)
