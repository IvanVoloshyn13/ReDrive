package com.example.domain.useCase.settings

import com.example.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsFacade @Inject constructor(
    private val repository: SettingsRepository
) {
    fun getCurrencyUnits() = repository.getCurrencyUnits()
    fun getDistanceUnits() = repository.getDistanceUnits()
    fun getAvgConsumptionUnits() = repository.getAvgConsumptionUnits()
    fun getCapacityUnits() = repository.getCapacityUnits()
    fun getDateFormatPatterns() = repository.getDateFormatPatterns()
}