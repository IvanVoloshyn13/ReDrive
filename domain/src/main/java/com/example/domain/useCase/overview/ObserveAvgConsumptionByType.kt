package com.example.domain.useCase.overview

import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.model.log.ValueWithUnit
import com.example.domain.repository.OverviewRepository
import com.example.domain.repository.UnitPreferencesRepository
import com.example.domain.useCase.AvgConsumptionType
import com.example.domain.useCase.RefuelLogBuilder
import com.example.domain.useCase.RefuelLogBuilder.formatToScale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveAvgConsumptionByType @Inject constructor(
    private val repository: OverviewRepository,
    private val prefRepository: UnitPreferencesRepository
) {
    fun invoke(vehicleId: Long, preferences: UnitsPreferencesAbbreviation): Flow<ValueWithUnit?> {
        return combine(
            repository.observeTravelledDistance(vehicleId),
            repository.observeFullAmountSum(vehicleId)
        ) { travDist, fuelAmount ->
            if (travDist == null || fuelAmount == null) return@combine null
            val unit = preferences.avgConsumption
            val key = prefRepository.getAvgConsumptionTypeKey(vehicleId = vehicleId)
            val avgConsumption = RefuelLogBuilder.getAvgConsumptionByType(
                type = AvgConsumptionType.fromKey(key),
                travelledDistance = travDist,
                fuelAmount = fuelAmount
            )
            ValueWithUnit(value = avgConsumption.formatToScale().toString(), unit = unit)
        }
    }
}