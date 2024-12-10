package voloshyn.android.domain.models.auth

sealed interface SignUpStatus {
    data object SignUp : SignUpStatus
    data object SignOut : SignUpStatus
    data object Failure:SignUpStatus
    data object InProgress:SignUpStatus
}