package com.example.domain.useCase.stats

import com.example.domain.model.VehicleWithStats
import com.example.domain.useCase.units.ObserveUnitPreferencesUseCase
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveVehicleWithStatsUseCase @Inject constructor(
    private val currentVehicleUseCase: ObserveCurrentVehicleUseCase,
    private val observeSummary: ObserveSummary,
    private val observeLastRefuelLog: ObserveLastRefuelLog,
    private val preferences: ObserveUnitPreferencesUseCase,
    private val observeAvgConsumptionByType: ObserveAvgConsumptionByType,
    private val observeDrivingCost: ObserveDrivingCost
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun invoke(): Flow<VehicleWithStats?> {
        return currentVehicleUseCase.invoke()
            .distinctUntilChanged()
            .flatMapLatest {
                it?.let { vehicle ->
                    return@flatMapLatest preferences.invoke(vehicleId = vehicle.id)
                        .flatMapLatest { preferences ->
                            combine(
                                observeSummary.invoke(vehicleId = vehicle.id, preferences),
                                observeLastRefuelLog.invoke(vehicle, preferences),
                                observeAvgConsumptionByType.invoke(vehicle.id, preferences),
                                observeDrivingCost.invoke(vehicle.id, preferences)
                            ) { summary, lastRefLog, avgCons, cost ->
                                VehicleWithStats(
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