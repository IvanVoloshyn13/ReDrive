package com.example.data.mappers

import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.localedatasource.inMemoryAppSettings.InMemoryAppUnitPreferencesRepository
import com.example.localedatasource.inMemoryAppSettings.models.DefaultPreferencesResponse
import com.example.localedatasource.inMemoryAppSettings.models.PreferencesResponse
import com.example.localedatasource.room.entity.UnitPreferencesEntity
import javax.inject.Inject

class UnitPreferencesMapperImpl @Inject constructor(
    inMemoryAppUnitPreferencesRepository: InMemoryAppUnitPreferencesRepository,
    language: String
) : UnitPreferencesMapper {
    private var preferencesResponse: PreferencesResponse = inMemoryAppUnitPreferencesRepository.getPreferences(language)

    override fun UnitPreferencesEntity.toPreferences(): UnitsPreferencesAbbreviation {
        return UnitsPreferencesAbbreviation(
            id = this.id,
            currency = preferencesResponse.currencies.first { it.key == this.currencyKey }.abbreviation,
            capacity = preferencesResponse.capacities.first { it.key == this.capacityKey }.abbreviation,
            distance = preferencesResponse.distances.first { it.key == this.distanceKey }.abbreviation,
            avgConsumption = preferencesResponse.avgConsumptions.first { it.key == this.avgConsumptionKey }.abbreviation,
            dateFormatPattern = preferencesResponse.dateFormats.first { it.key == this.dateFormatPatternKey }.displayName,
        )
    }

    override fun DefaultPreferencesResponse.toPreferences(): UnitsPreferencesAbbreviation {
        return UnitsPreferencesAbbreviation(
            currency = this.currency.abbreviation,
            capacity = this.capacity.abbreviation,
            distance = this.distance.abbreviation,
            avgConsumption = this.avgConsumption.abbreviation,
            dateFormatPattern = this.dateFormat.displayName
        )
    }

    override fun UnitsPreferencesAbbreviation.toEntity(vehicleId: Long?): UnitPreferencesEntity {
        return UnitPreferencesEntity(
            id = this.id,
            vehicleId = vehicleId ?: 0L,
            currencyKey = preferencesResponse.currencies.first { it.abbreviation == this.currency }.key,
            capacityKey = preferencesResponse.capacities.first { it.abbreviation == this.capacity }.key,
            distanceKey = preferencesResponse.distances.first { it.abbreviation == this.distance }.key,
            avgConsumptionKey = preferencesResponse.avgConsumptions.first { it.abbreviation == this.avgConsumption }.key,
            dateFormatPatternKey = preferencesResponse.dateFormats.first { it.displayName == this.dateFormatPattern }.key
        )
    }
}