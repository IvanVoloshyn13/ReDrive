package com.example.domain.useCase.overview

import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.model.log.ValueWithUnit
import com.example.domain.repository.OverviewRepository
import com.example.domain.useCase.logs.RefuelLogBuilder.formatToScale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveDrivingCostUseCase @Inject constructor(
    private val repository: OverviewRepository
) {

    fun invoke(vehicleId: Long, preferences: UnitsPreferencesAbbreviation): Flow<ValueWithUnit?> {
        return combine(
            repository.observePaymentsSum(vehicleId),
            repository.observeTravelledDistance(vehicleId)
        ) { payments, distance ->
            findCostByType(
                payments,
                distance,
                preferences.currency,
                preferences.distance
            )
        }
    }

    private fun findCostByType(
        payments: Double?,
        distance: Int?,
        unitCurrency: String,
        unitDistance: String
    ): ValueWithUnit? {
        return if (payments == null || distance == null) {
            null
        } else {
            ValueWithUnit(
                value = (payments / distance).formatToScale().toString(),
                unit = "$unitCurrency/$unitDistance"
            )
        }
    }

}