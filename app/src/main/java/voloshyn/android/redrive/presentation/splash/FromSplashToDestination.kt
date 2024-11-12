package voloshyn.android.redrive.presentation.splash

sealed class FromSplashToDestination {
    data object ToOnBoard:FromSplashToDestination()
    data object ToSignIn:FromSplashToDestination()
    data object ToAddNewVehicle:FromSplashToDestination()
    data object ToTabs:FromSplashToDestination()
}