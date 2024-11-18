package voloshyn.android.domain.models.auth

sealed interface SignInStatus {
    data class SignIn(val user:User):SignInStatus
    data object SignOut : SignInStatus
    data object Failure:SignInStatus
}