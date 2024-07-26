package voloshyn.android.redrive.presentation.profile

import androidx.annotation.StringRes

data class ValidateErrorState(
    val isValid: Boolean = false,
    @StringRes val message: Int
)

