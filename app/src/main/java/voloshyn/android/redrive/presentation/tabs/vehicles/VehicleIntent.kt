package voloshyn.android.redrive.presentation.tabs.vehicles

import voloshyn.android.domain.models.Vehicle

sealed interface VehicleIntent {
    class AddVehicle(val vehicle: Vehicle):VehicleIntent
    class OnVehicleChange(val vehicleId: Long):VehicleIntent
    class OnDeleteVehicle(val vehicleId: Long):VehicleIntent
}