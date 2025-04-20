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
            type = VehicleType.Car, isCurrentVehicle = false
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vehicle) return false
        return id == other.id &&
                name == other.name &&
                initialOdometerValue == other.initialOdometerValue &&
                type == other.type
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + initialOdometerValue
        result = 31 * result + type.hashCode()
        return result
    }
}

enum class VehicleType {
    Car, Bike
}
