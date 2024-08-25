package voloshyn.android.redrive.presentation.auth

import androidx.annotation.StringRes

data class ValidateErrorState(
    val isValid: Boolean = false,
    @StringRes val message: Int
)

