package voloshyn.android.redrive.presentation.auth.signUp

import androidx.annotation.StringRes
import voloshyn.android.domain.models.auth.User

data class FragmentSignUpState(
    val validationState: Boolean = false,
    val signUpStatus: SignUpStatus = SignUpStatus.InProgress,
    val loading: Boolean = false,
    val currentUser: User = User.EMPTY_USER,
    @StringRes val errorMessage: Int = 0
)

sealed interface SignUpStatus {
    data object Success : SignUpStatus
    data class Failure(@StringRes val errorMessage: Int) : SignUpStatus
    data object InProgress : SignUpStatus
}