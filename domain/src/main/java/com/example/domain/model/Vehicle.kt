package com.example.domain.model

import java.io.Serializable

data class Vehicle(
    val id: Long = 0,
    val name: String,
    val initialOdometerValue: Int,
    val type: VehicleType,
    val isCurrentVehicle: Boolean = false,
) : Serializable {
    companion object {
        val NO_VEHICLE: Vehicle = Vehicle(
            id = 0,
            name = "",
            initialOdometerValue = 0,
            type = VehicleType.Default, isCurrentVehicle = false
        )
    }
}

enum class VehicleType {
    Car, Bike, Default
}
