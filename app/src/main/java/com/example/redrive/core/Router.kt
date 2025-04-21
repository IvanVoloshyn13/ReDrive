package com.example.redrive.core

import com.example.domain.model.Vehicle

object Router {
    sealed class SplashDirections : AppDirection {
        data object ToApp : SplashDirections()
        data object ToProfile : SplashDirections()
    }

    sealed class ProfileDirections : AppDirection {
        data object ToSettings : ProfileDirections()
        data object ToEditVehicles : ProfileDirections()
        data object ToSignIn : ProfileDirections()
    }

    sealed class SignInDirections : AppDirection {
        data object ToSignUp : SignInDirections()
        data object ToProfile : SignInDirections()
    }

    sealed class SignUpDirections : AppDirection {
        data object ToSignIn : SignUpDirections()
        data object ToProfile : SignUpDirections()
    }

    sealed class SettingsDirection : AppDirection {
        data object ToProfile : SettingsDirection()
    }

    sealed class VehiclesDirections : AppDirection {
        data object ToNewVehicle : VehiclesDirections()
        data class ToEditVehicle(val vehicle: Vehicle) : VehiclesDirections()
    }

    sealed class NewVehicleDirections : AppDirection {
        data object ToVehicles : NewVehicleDirections()
    }

    sealed class EditVehicleDirections : AppDirection {
        data object ToVehicles : EditVehicleDirections()
    }

    sealed class LogsDirections : AppDirection {
        data object ToVehicles : LogsDirections()
        data object ToRefuel : LogsDirections()
        data class ToEditRefuel(val refuelId: Long) : LogsDirections()
    }

    sealed class RefuelDirection : AppDirection {
        data object ToLogs : RefuelDirection()

    }

    sealed class ReDriveDirection : AppDirection {
        data object ToRefuel : ReDriveDirection()
        data object ToVehicles : ReDriveDirection()

    }
}

interface AppDirection