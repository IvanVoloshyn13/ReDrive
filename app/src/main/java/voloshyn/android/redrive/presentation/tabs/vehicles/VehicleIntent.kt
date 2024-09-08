package voloshyn.android.redrive.presentation.tabs.vehicles

import voloshyn.android.domain.models.tabs.redrive.Vehicle

sealed interface VehicleIntent {
    class AddVehicle(val vehicle: Vehicle,val accountId:String?):VehicleIntent
}