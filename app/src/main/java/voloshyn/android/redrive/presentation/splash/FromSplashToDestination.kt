package voloshyn.android.redrive.presentation.splash

import voloshyn.android.domain.models.auth.User

sealed class FromSplashToDestination {
    data object ToOnBoard : FromSplashToDestination()
    data object ToSignIn : FromSplashToDestination()
    data object ToAddNewVehicle : FromSplashToDestination()
    data class ToTabs(val user: User) : FromSplashToDestination()
}