package voloshyn.android.redrive.presentation.auth.signUp

import androidx.annotation.StringRes
import voloshyn.android.domain.models.auth.User

data class FragmentSignUpState(
    val validationState: Boolean = false,
    val signUpState: SignUpState = SignUpState.InProgress,
    val loading: Boolean = false,
    val currentUser: User = User.EMPTY_USER,
    @StringRes val errorMessage: Int = 0
)

sealed interface SignUpState {
    data object Success : SignUpState
    data class Failure(@StringRes val errorMessage: Int) : SignUpState
    data object InProgress : SignUpState
}