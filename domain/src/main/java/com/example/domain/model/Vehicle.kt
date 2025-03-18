package com.example.domain.model

data class Vehicle(
    val id: Long,
    val name: String,
    val initialOdometerValue: Int,
    val type: VehicleType
)

enum class VehicleType {
    Car, Bike, Default
}
