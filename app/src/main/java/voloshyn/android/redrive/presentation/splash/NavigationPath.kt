package voloshyn.android.redrive.presentation.splash

sealed class NavigationPath {
    data object ToOnBoard : NavigationPath()
    data object ToSignIn : NavigationPath()
    data object ToAddNewVehicle : NavigationPath()
    data object ToTabs : NavigationPath()
}