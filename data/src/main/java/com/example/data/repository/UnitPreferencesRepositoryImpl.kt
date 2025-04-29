package com.example.data.repository

import com.example.data.mappers.UnitPreferencesMapper
import com.example.domain.model.AvgConsumption
import com.example.domain.model.Capacity
import com.example.domain.model.Currency
import com.example.domain.model.DateFormatPattern
import com.example.domain.model.Distance
import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.repository.UnitPreferencesRepository
import com.example.localedatasource.appPreferencesFromAssets.FromAssetsUnitPreferencesDataSource
import com.example.localedatasource.room.daos.SettingsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UnitPreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataSource: FromAssetsUnitPreferencesDataSource,
    private val settingsDao: SettingsDao,
    private val unitPreferencesMapper: UnitPreferencesMapper,
    private val language: String
) : UnitPreferencesRepository {
    override suspend fun getDefaultUnitPreferences(): UnitsPreferencesAbbreviation {
        return with(unitPreferencesMapper) {
            val response = preferencesDataSource.getDefaultPreferences(language)
            response.toPreferences()
        }
    }

    override fun observeUnitPreferences(vehicleId: Long): Flow<UnitsPreferencesAbbreviation> {
        return settingsDao.getUnitPreferencesByCurrentVehicleId(vehicleId).map {
            with(unitPreferencesMapper) {
                it.toPreferences()
            }
        }
    }

    override suspend fun getAvgConsumptionTypeKey(vehicleId: Long): String {
      return settingsDao.getAvgConsumptionKey(vehicleId)
    }

    override suspend fun getDistanceTypeKey(vehicleId: Long): String {
      return settingsDao.getDistanceKey(vehicleId)
    }

    override fun getCurrencies(): List<Currency> {
        return preferencesDataSource.getPreferences(language).currencies.map {
            Currency(
                id = it.id,
                displayName = it.displayName,
                abbreviation = it.abbreviation
            )
        }
    }

    override fun getAvgConsumptions(): List<AvgConsumption> {
        return preferencesDataSource.getPreferences(language).avgConsumptions.map {
            AvgConsumption(
                id = it.id,
                abbreviation = it.abbreviation,
                key = it.key
            )
        }
    }

    override fun getCapacities(): List<Capacity> {
        return preferencesDataSource.getPreferences(language).capacities.map {
            Capacity(
                id = it.id,
                displayName = it.displayName,
                abbreviation = it.abbreviation
            )
        }
    }

    override fun getDistances(): List<Distance> {
        return preferencesDataSource.getPreferences(language).distances.map {
            Distance(
                id = it.id,
                displayName = it.displayName,
                abbreviation = it.abbreviation
            )
        }
    }

    override fun getDateFormatPatterns(): List<DateFormatPattern> {
        return preferencesDataSource.getPreferences(language).dateFormats.map {
            DateFormatPattern(
                id = it.id,
                pattern = it.pattern,
                displayName = it.displayName
            )
        }
    }

    override suspend fun updatePreferences(unitPreferences: UnitsPreferencesAbbreviation, vehicleId: Long) {
        val entity = with(unitPreferencesMapper) {
            unitPreferences.toEntity(vehicleId = vehicleId)
        }
        settingsDao.updatePreferences(entity)
    }

    override suspend fun getCurrentDateFormatPattern(vehicleId: Long?): String {
        return vehicleId?.let {
            preferencesDataSource.getPreferences(language).dateFormats.first { json ->
                val key = settingsDao.getDateFormatPatternKey(it)
                json.key == key
            }.pattern
        } ?: preferencesDataSource.getPreferences(language).dateFormats[0].pattern
    }
    
}