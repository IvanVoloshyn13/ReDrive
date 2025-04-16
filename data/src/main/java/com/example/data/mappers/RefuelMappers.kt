package com.example.data.mappers

import com.example.domain.model.Refuel
import com.example.localedatasource.room.entity.RefuelEntity


fun Refuel.toEntity(vehicleId: Long): RefuelEntity {
    return RefuelEntity(
        id = 0,
        vehicleId = vehicleId,
        date = this.refuelDate,
        odometer = this.odometerValue,
        fuelVolume = this.fuelAmount,
        unitPrice = this.pricePerUnit,
        notes = this.notes,
        fullTank = this.fullTank,
        missedPrevious = this.missedPrevious
    )
}

fun RefuelEntity.toRefuel(): Refuel {
    return Refuel(
        id = this.id,
        refuelDate = this.date,
        odometerValue = this.odometer,
        fuelAmount = this.fuelVolume,
        pricePerUnit = this.unitPrice,
        notes = this.notes,
        fullTank = this.fullTank,
        missedPrevious = this.missedPrevious
    )
}