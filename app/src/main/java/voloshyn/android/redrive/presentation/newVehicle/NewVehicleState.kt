package voloshyn.android.redrive.presentation.newVehicle

import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.VehicleType

private const val EMPTY_ODOMETER_VALUE = ""

data class NewVehicleState(
    val vehicleType: VehicleType = VehicleType.Car,
    val vehicleName: String = "",
    val odometer: String = EMPTY_ODOMETER_VALUE,
    val isLoading: Boolean = false
)


