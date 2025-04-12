package com.example.data

import com.example.domain.model.Settings
import com.example.localedatasource.inMemoryAppSettings.InMemoryAppSettingsRepository
import com.example.localedatasource.inMemoryAppSettings.models.DefaultSettingsResponse
import com.example.localedatasource.inMemoryAppSettings.models.SettingsResponse
import com.example.localedatasource.room.entity.SettingsEntity
import javax.inject.Inject

class SettingsMapperImpl @Inject constructor(
    inMemoryAppSettingsRepository: InMemoryAppSettingsRepository,
    language: String
) : SettingsMapper {
    private var appSettings: SettingsResponse = inMemoryAppSettingsRepository.getSettings(language)

    override fun SettingsEntity.toSettings(): Settings {
        return Settings(
            id = this.id,
            currency = appSettings.currencies.first { it.key == this.currencyKey }.abbreviation,
            capacity = appSettings.capacities.first { it.key == this.capacityKey }.abbreviation,
            distance = appSettings.distances.first { it.key == this.distanceKey }.abbreviation,
            avgConsumption = appSettings.avgConsumptions.first { it.key == this.avgConsumptionKey }.unit,
            dateFormatPattern = appSettings.dateFormats.first { it.key == this.dateFormatPatternKey }.unit,
        )
    }

    override fun DefaultSettingsResponse.toSettings(): Settings {
        return Settings(
            currency = this.currency.abbreviation,
            capacity = this.capacity.abbreviation,
            distance = this.distance.abbreviation,
            avgConsumption = this.avgConsumption.unit,
            dateFormatPattern = this.dateFormat.unit
        )
    }

    override fun Settings.toEntity(vehicleId: Long?): SettingsEntity {
        return SettingsEntity(
            id = this.id,
            vehicleId = vehicleId ?: 0L,
            currencyKey = appSettings.currencies.first { it.abbreviation == this.currency }.key,
            capacityKey = appSettings.capacities.first { it.abbreviation == this.capacity }.key,
            distanceKey = appSettings.distances.first { it.abbreviation == this.distance }.key,
            avgConsumptionKey = appSettings.avgConsumptions.first { it.unit == this.avgConsumption }.key,
            dateFormatPatternKey = appSettings.dateFormats.first { it.unit == this.dateFormatPattern }.key,
        )
    }
}