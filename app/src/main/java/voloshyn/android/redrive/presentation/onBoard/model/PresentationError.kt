package voloshyn.android.redrive.presentation.onBoard.model

import androidx.annotation.StringRes

data class PresentationError(
    val isError: Boolean = false,
    @StringRes val message: Int = 0
)
