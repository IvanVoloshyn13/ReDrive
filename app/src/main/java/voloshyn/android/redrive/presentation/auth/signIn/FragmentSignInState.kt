package voloshyn.android.redrive.presentation.auth.signIn

import androidx.annotation.StringRes
import voloshyn.android.domain.models.auth.SignInStatus

data class FragmentSignInState(
    val loading: Boolean = false,
    val signInStatus: SignInStatus = SignInStatus.SignOut,
    val isError: Boolean = false,
    @StringRes val errorMessage: Int? = null,
    val isVehicle: Boolean = false
)
