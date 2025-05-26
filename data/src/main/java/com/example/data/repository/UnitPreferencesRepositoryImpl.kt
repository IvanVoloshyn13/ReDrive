package com.example.data.repository

import com.example.data.mappers.UnitPreferencesMapper
import com.example.domain.model.AvgConsumption
import com.example.domain.model.Capacity
import com.example.domain.model.Currency
import com.example.domain.model.DateFormatPattern
import com.example.domain.model.Distance
import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.repository.UnitPreferencesRepository
import com.example.localedatasource.appPreferencesFromAssets.AssetsDataSource
import com.example.localedatasource.room.daos.SettingsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject

class UnitPreferencesRepositoryImpl @Inject constructor(
    private val assetsDataSource: AssetsDataSource,
    private val settingsDao: SettingsDao,
    private val unitPreferencesMapper: UnitPreferencesMapper,
) : UnitPreferencesRepository {
    private val language = Locale.getDefault().language
    override fun getDefaultUnitPreferences(): UnitsPreferencesAbbreviation {
        return with(unitPreferencesMapper) {
            val response = assetsDataSource.getDefaultPreferences(language)
            response.toPreferences()
        }
    }

    override fun observeUnitPreferences(vehicleId: String): Flow<UnitsPreferencesAbbreviation> {
        return settingsDao.getUnitPreferencesByCurrentVehicleId(vehicleId).map {
            with(unitPreferencesMapper) {
                it.toPreferences()
            }
        }
    }

    override suspend fun getAvgConsumptionTypeKey(vehicleId: String): String {
        return settingsDao.getAvgConsumptionKey(vehicleId)
    }

    override suspend fun getDistanceTypeKey(vehicleId: String): String {
        return settingsDao.getDistanceKey(vehicleId)
    }

    override fun getCurrencies(): List<Currency> {
        return assetsDataSource.getPreferences(language).currencies.map {
            Currency(
                id = it.id,
                displayName = it.displayName,
                abbreviation = it.abbreviation
            )
        }
    }

    override fun getAvgConsumptions(): List<AvgConsumption> {
        return assetsDataSource.getPreferences(language).avgConsumptions.map {
            AvgConsumption(
                id = it.id,
                abbreviation = it.abbreviation,
                key = it.key
            )
        }
    }

    override fun getCapacities(): List<Capacity> {
        return assetsDataSource.getPreferences(language).capacities.map {
            Capacity(
                id = it.id,
                displayName = it.displayName,
                abbreviation = it.abbreviation
            )
        }
    }

    override fun getDistances(): List<Distance> {
        return assetsDataSource.getPreferences(language).distances.map {
            Distance(
                id = it.id,
                displayName = it.displayName,
                abbreviation = it.abbreviation
            )
        }
    }

    override fun getDateFormatPatterns(): List<DateFormatPattern> {
        return assetsDataSource.getPreferences(language).dateFormats.map {
            DateFormatPattern(
                id = it.id,
                pattern = it.pattern,
                displayName = it.displayName
            )
        }
    }

    override suspend fun updatePreferences(
        unitPreferences: UnitsPreferencesAbbreviation,
        vehicleId: String
    ) {
        val entity = with(unitPreferencesMapper) {
            unitPreferences.toEntity(vehicleId = vehicleId)
        }
        settingsDao.updatePreferences(entity)
    }

    override suspend fun getCurrentDateFormatPattern(vehicleId: String?): String {
        return vehicleId?.let {
            assetsDataSource.getPreferences(language).dateFormats.first { json ->
                val key = settingsDao.getDateFormatPatternKey(it)
                json.key == key
            }.pattern
        } ?: assetsDataSource.getPreferences(language).dateFormats[0].pattern
    }

}