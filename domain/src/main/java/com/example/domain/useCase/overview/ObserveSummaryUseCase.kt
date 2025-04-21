package com.example.domain.useCase.overview

import com.example.domain.model.Summary
import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.repository.OverviewRepository
import com.example.domain.repository.UnitPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import java.math.RoundingMode
import javax.inject.Inject

class ObserveSummaryUseCase @Inject constructor(
    private val repository: OverviewRepository,
) {
    fun invoke(vehicleId: Long,preferences:UnitsPreferencesAbbreviation): Flow<Summary?> {
        return combine(
            repository.observePaymentsSum(vehicleId),
            repository.observeTravelledDistance(vehicleId),
            repository.observeFullAmountSum(vehicleId)
        ) {  payments, travDistance, fuelAmount ->
            Summary(
                travelledDistance = travDistance?.concatenateValueWithUnit(preferences.distance),
                fuelAmountSum = fuelAmount?.formatToScale()
                    .concatenateValueWithUnit(preferences.capacity),
                paymentsSum = payments?.formatToScale()
                    .concatenateValueWithUnit(preferences.currency)
            )
        }

    }

    private fun <T> T?.concatenateValueWithUnit(unit: String): String? {
        if (this == null) return null
        return "$this $unit"
    }

    private fun Double?.formatToScale(): Double? {
        if (this == null) return null
        return if (this <= 0) 1.0 else
            this.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }


}