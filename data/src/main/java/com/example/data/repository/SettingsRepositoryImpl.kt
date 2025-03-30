package com.example.data.repository

import com.example.data.toSettings
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
import java.util.Locale
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val inMemoryAppSettingsRepository: InMemoryAppSettingsRepository,
    private val settingsDao: SettingsDao
) : SettingsRepository {
    private val language = Locale.getDefault().language
    override suspend fun getDefaultSettings(): Settings {
        val response = inMemoryAppSettingsRepository.getDefaultSettings(language)
        return response.toSettings()
    }

    override fun observeAppSettings(vehicleId: Long): Flow<Settings> {
        return settingsDao.getSettingsByCurrentVehicleId(vehicleId).map {
            it.toSettings()
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
                abbreviation = it.abbreviation
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
                pattern = it.pattern
            )
        }
    }

    override suspend fun updateSettings(settings: Settings, vehicleId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getDateFormatPattern(): String {
        return settingsDao.getDateFormatPattern().pattern
    }
}