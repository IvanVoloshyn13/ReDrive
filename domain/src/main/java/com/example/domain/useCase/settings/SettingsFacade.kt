package com.example.domain.useCase.settings

import com.example.domain.model.DateFormatPattern
import com.example.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * A facade that provides a simple interface for accessing independent setting units
 * (e.g., currency units, distance units, etc.) from the repository.
 */
class SettingsFacade @Inject constructor(
    private val repository: SettingsRepository
) {
    fun getCurrencyUnits() = repository.getCurrencyUnits()
    fun getDistanceUnits() = repository.getDistanceUnits()
    fun getAvgConsumptionUnits() = repository.getAvgConsumptionUnits()
    fun getCapacityUnits() = repository.getCapacityUnits()
    fun getDateFormatPatterns() = repository.getDateFormatPatterns().map {
        DateFormatPattern(
            id = it.id,
            pattern = it.pattern.uppercase()
        )
    }
}