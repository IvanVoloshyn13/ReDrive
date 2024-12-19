package voloshyn.android.redrive.presentation.tabs.editVehicle

import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.VehicleType

data class EditVehicleState(
    val vehicle: EditVehicleModel = EditVehicleModel(),
    val areVehiclesTheSame: Boolean = true
)

data class EditVehicleModel(
    val id: Long = 0L,
    val name: String = "",
    val initialOdometerValue: String = "",
    val type: VehicleType = VehicleType.Car
) {

    fun toVehicle(): Vehicle {
        return Vehicle(
            id = id,
            name = name,
            initialOdometerValue = initialOdometerValue.toInt(),
            type = type
        )
    }

    companion object {
        fun toEditVehicle(vehicle: Vehicle): EditVehicleModel {
            return EditVehicleModel(
                id = vehicle.id,
                name = vehicle.name,
                initialOdometerValue = vehicle.initialOdometerValue.toString(),
                type = vehicle.type
            )
        }
    }

}

