package voloshyn.android.redrive.presentation.splash

sealed class NavigationPath {
    data object ToOnBoard : NavigationPath()
    data object ToAuthentication : NavigationPath()
    data object ToNewVehicle : NavigationPath()
    data object ToTabs : NavigationPath()
}