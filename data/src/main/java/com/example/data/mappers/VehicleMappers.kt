package com.example.data.mappers

import com.example.domain.model.Vehicle
import com.example.domain.model.VehicleType
import com.example.localedatasource.room.entity.VehicleEntity

fun Vehicle.toEntity(uUid: String): VehicleEntity {
    return VehicleEntity(
        id = this.id,
        userId = uUid,
        name = this.name,
        vehicleType = type.name,
        initialOdometerValue = this.initialOdometerValue
    )
}

fun VehicleEntity.toVehicle(): Vehicle {
    return Vehicle(
        id = this.id,
        name = this.name,
        initialOdometerValue = this.initialOdometerValue,
        type = VehicleType.valueOf(this.vehicleType)
    )
}