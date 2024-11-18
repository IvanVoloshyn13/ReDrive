package voloshyn.android.redrive.presentation.auth.signIn

import androidx.annotation.StringRes
import voloshyn.android.domain.models.auth.SignInStatus

import voloshyn.android.domain.models.auth.User

data class FragmentSignInState(
    val loading: Boolean = false,
    val signInStatus: SignInStatus = SignInStatus.SignOut,
    val user: User = User.EMPTY_USER,
    val isError: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
