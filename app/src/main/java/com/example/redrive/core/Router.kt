package com.example.redrive.core

import com.example.domain.model.Refuel
import com.example.domain.model.Vehicle

object Router {
    sealed class SplashDirections : RedriveDirection {
        data object ToRedrive : SplashDirections()
        data object ToProfile : SplashDirections()
    }

    sealed class ProfileDirections : RedriveDirection {
        data object ToSettings : ProfileDirections()
        data object ToEditVehicles : ProfileDirections()
        data object ToSignIn : ProfileDirections()
    }

    sealed class SignInDirections : RedriveDirection {
        data object ToSignUp : SignInDirections()
        data object ToProfile : SignInDirections()
    }

    sealed class SignUpDirections : RedriveDirection {
        data object ToSignIn : SignUpDirections()
        data object ToProfile : SignUpDirections()
    }

    sealed class SettingsDirection : RedriveDirection {
        data object ToProfile : SettingsDirection()
    }

    sealed class VehiclesDirections : RedriveDirection {
        data object ToNewVehicle : VehiclesDirections()
        data class ToEditVehicle(val vehicle: Vehicle) : VehiclesDirections()
    }

    sealed class NewVehicleDirections : RedriveDirection {
        data object ToVehicles : NewVehicleDirections()
    }

    sealed class EditVehicleDirections : RedriveDirection {
        data object ToVehicles : NewVehicleDirections()
    }

    sealed class LogsDirections : RedriveDirection {
        data object ToVehicles : LogsDirections()
        data object ToRefuel : LogsDirections()
        data class ToEditRefuel(val refuelId: Long) : LogsDirections()
    }

    sealed class RefuelDirection : RedriveDirection {
        data object ToLogs : LogsDirections()

    }
}

interface RedriveDirection