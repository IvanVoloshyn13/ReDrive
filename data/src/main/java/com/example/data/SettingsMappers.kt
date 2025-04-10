package com.example.data

import com.example.domain.model.Settings
import com.example.localedatasource.inMemoryAppSettings.models.DefaultAppSettingsResponse
import com.example.localedatasource.room.entity.SettingsEntity

fun DefaultAppSettingsResponse.toSettings(): Settings {
    return Settings(
        currencyAbbr = this.currency.abbreviation,
        capacityAbbr = this.capacity.abbreviation,
        distanceAbbr = this.distance.abbreviation,
        avgConsumptionAbbr = this.avgConsumption.abbreviation,
        dateFormatPattern = this.dateFormat.pattern
    )
}

fun SettingsEntity.toSettings(): Settings {
    return Settings(
        id = this.id,
        currencyAbbr = this.currency,
        capacityAbbr = this.capacity,
        distanceAbbr = this.distance,
        avgConsumptionAbbr = this.avgConsumption,
        dateFormatPattern = this.dateFormatPatter.uppercase()
    )
}

fun Settings.toEntity(vehicleId: Long? = null): SettingsEntity {
    return SettingsEntity(
        id = this.id,
        vehicleId = vehicleId ?: 0,
        currency = this.currencyAbbr,
        capacity = this.capacityAbbr,
        distance = this.distanceAbbr,
        avgConsumption = this.avgConsumptionAbbr,
        dateFormatPatter = this.dateFormatPattern
    )
}
