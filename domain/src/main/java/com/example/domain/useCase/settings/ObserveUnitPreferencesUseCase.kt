package com.example.domain.useCase.settings

import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.repository.UnitPreferencesRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveUnitPreferencesUseCase @Inject constructor(
    private val repository: UnitPreferencesRepository,
    private val observeCurrentVehicleUseCase: ObserveCurrentVehicleUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<UnitsPreferencesAbbreviation> {
        return observeCurrentVehicleUseCase.invoke().flatMapLatest {
            return@flatMapLatest it?.let { vehicle ->
                repository.observeUnitPreferences(vehicle.id)
            } ?: flowOf(repository.getDefaultUnitPreferences())
        }
    }
}