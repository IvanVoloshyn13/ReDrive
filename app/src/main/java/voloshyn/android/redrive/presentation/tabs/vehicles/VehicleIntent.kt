package voloshyn.android.redrive.presentation.tabs.vehicles

import voloshyn.android.domain.models.Vehicle

sealed interface VehicleIntent {
    class AddVehicle(val vehicle: Vehicle, val accountId:String?):VehicleIntent
    class OnVehicleChange(val vehicleId: Long, val accountId:String?):VehicleIntent
}