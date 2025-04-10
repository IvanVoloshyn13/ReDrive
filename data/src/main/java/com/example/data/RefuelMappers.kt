package com.example.data

import com.example.domain.model.Refuel
import com.example.localedatasource.room.entity.RefuelEntity


fun Refuel.toEntity(vehicleId: Long): RefuelEntity {
    return RefuelEntity(
        id = 0,
        vehicleId = vehicleId,
        date = this.refuelDate,
        odometer = this.odometerValue,
        fuelVolume = this.fuelVolume,
        unitPrice = this.pricePerUnit,
        notes = this.notes,
        fullTank = this.fullTank,
        missedPrevious = this.missedPrevious
    )
}

fun RefuelEntity.toRefuel(): Refuel {
    return Refuel(
        refuelDate = this.date,
        odometerValue = this.odometer,
        fuelVolume = this.fuelVolume,
        pricePerUnit = this.unitPrice,
        notes = this.notes,
        fullTank = this.fullTank,
        missedPrevious = this.missedPrevious
    )
}