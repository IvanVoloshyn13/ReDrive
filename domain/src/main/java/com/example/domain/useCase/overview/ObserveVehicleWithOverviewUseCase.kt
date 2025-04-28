package com.example.domain.useCase.overview

import com.example.domain.model.VehicleWithOverview
import com.example.domain.useCase.settings.ObserveUnitPreferencesUseCase
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveVehicleWithOverviewUseCase @Inject constructor(
    private val currentVehicleUseCase: ObserveCurrentVehicleUseCase,
    private val observeSummaryUseCase: ObserveSummaryUseCase,
    private val observeLastRefuelLogUseCase: ObserveLastRefuelLogUseCase,
    private val preferences: ObserveUnitPreferencesUseCase,
    private val observeAvgConsumptionByType: ObserveAvgConsumptionByType,
    private val observeDrivingCostUseCase: ObserveDrivingCostUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun invoke(): Flow<VehicleWithOverview?> {
        return currentVehicleUseCase.invoke()
            .distinctUntilChanged()
            .flatMapLatest {
                it?.let { vehicle ->
                    return@flatMapLatest preferences.invoke(vehicleId = vehicle.id)
                        .flatMapLatest { preferences ->
                            combine(
                                observeSummaryUseCase.invoke(vehicleId = vehicle.id, preferences),
                                observeLastRefuelLogUseCase.invoke(vehicle, preferences),
                                observeAvgConsumptionByType.invoke(vehicle.id, preferences),
                                observeDrivingCostUseCase.invoke(vehicle.id, preferences)
                            ) { summary, lastRefLog, avgCons, cost ->
                                VehicleWithOverview(
                                    vehicle = vehicle,
                                    avgConsumption = avgCons,
                                    drivingCost = cost,
                                    summary = summary,
                                    lastRefuelLog = lastRefLog
                                )
                            }
                        }

                } ?: flowOf(null)
            }
    }
}