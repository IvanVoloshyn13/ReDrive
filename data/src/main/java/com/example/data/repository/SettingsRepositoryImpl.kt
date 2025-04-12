package com.example.data.repository

import com.example.data.SettingsMapper
import com.example.domain.model.AvgConsumption
import com.example.domain.model.Capacity
import com.example.domain.model.Currency
import com.example.domain.model.DateFormatPattern
import com.example.domain.model.Distance
import com.example.domain.model.Settings
import com.example.domain.repository.SettingsRepository
import com.example.localedatasource.inMemoryAppSettings.InMemoryAppSettingsRepository
import com.example.localedatasource.room.daos.SettingsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val inMemoryAppSettingsRepository: InMemoryAppSettingsRepository,
    private val settingsDao: SettingsDao,
    private val settingsMapper: SettingsMapper,
    private val language: String
) : SettingsRepository {
    override suspend fun getDefaultSettings(): Settings {
        return with(settingsMapper) {
            val response = inMemoryAppSettingsRepository.getDefaultSettings(language)
            response.toSettings()
        }
    }

    override fun observeAppSettings(vehicleId: Long): Flow<Settings> {
        return settingsDao.getSettingsByCurrentVehicleId(vehicleId).map {
            with(settingsMapper) {
                it.toSettings()
            }
        }
    }

    override fun getCurrencyUnits(): List<Currency> {
        return inMemoryAppSettingsRepository.getSettings(language).currencies.map {
            Currency(
                id = it.id,
                unit = it.unit,
                abbreviation = it.abbreviation
            )
        }
    }

    override fun getAvgConsumptionUnits(): List<AvgConsumption> {
        return inMemoryAppSettingsRepository.getSettings(language).avgConsumptions.map {
            AvgConsumption(
                id = it.id,
                unit = it.unit,
                key = it.key
            )
        }
    }

    override fun getCapacityUnits(): List<Capacity> {
        return inMemoryAppSettingsRepository.getSettings(language).capacities.map {
            Capacity(
                id = it.id,
                unit = it.unit,
                abbreviation = it.abbreviation
            )
        }
    }

    override fun getDistanceUnits(): List<Distance> {
        return inMemoryAppSettingsRepository.getSettings(language).distances.map {
            Distance(
                id = it.id,
                unit = it.unit,
                abbreviation = it.abbreviation
            )
        }
    }

    override fun getDateFormatPatterns(): List<DateFormatPattern> {
        return inMemoryAppSettingsRepository.getSettings(language).dateFormats.map {
            DateFormatPattern(
                id = it.id,
                pattern = it.pattern,
                unit = it.unit
            )
        }
    }

    override suspend fun updateSettings(settings: Settings, vehicleId: Long) {
        val entity = with(settingsMapper) {
            settings.toEntity(vehicleId = vehicleId)
        }
        settingsDao.updateSettings(entity)
    }

    override suspend fun getDateFormatPattern(vehicleId: Long?): String {
        return vehicleId?.let {
            inMemoryAppSettingsRepository.getSettings(language).dateFormats.first { json ->
                val key = settingsDao.getDateFormatPatternKey(it)
                json.key == key
            }.pattern
        } ?: inMemoryAppSettingsRepository.getSettings(language).dateFormats[0].pattern
    }
    
}