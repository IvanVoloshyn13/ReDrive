package com.example.domain.useCase.units

import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.repository.VehicleUnitPreferencesRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveUnitPreferencesUseCase @Inject constructor(
    private val repository: VehicleUnitPreferencesRepository,
    private val observeCurrentVehicleUseCase: ObserveCurrentVehicleUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(vehicleId: String? = null): Flow<UnitsPreferencesAbbreviation> {
        return if (vehicleId != null) {
            repository.observeUnitPreferences(vehicleId).distinctUntilChanged()
        } else observeCurrentVehicleUseCase.invoke().flatMapLatest {
            return@flatMapLatest it?.let { vehicle ->
                repository.observeUnitPreferences(vehicle.id)
            } ?: flowOf(repository.getDefaultUnitPreferences())
        }
    }
}