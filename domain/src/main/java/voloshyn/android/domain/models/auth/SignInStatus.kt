package voloshyn.android.domain.models.auth

sealed interface SignInStatus {
    data object SignIn:SignInStatus
    data object SignOut:SignInStatus
}