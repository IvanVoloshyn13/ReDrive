package com.example.domain.useCase.settings

import com.example.domain.VehicleException
import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.repository.UnitPreferencesRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateUnitPreferencesUseCase @Inject constructor(
    private val repository: UnitPreferencesRepository,
    private val observeCurrentVehicleUseCase: ObserveCurrentVehicleUseCase
) {
    suspend operator fun invoke(unitPreferences: UnitsPreferencesAbbreviation) {
        val vehicle = observeCurrentVehicleUseCase.invoke().first()
        vehicle?.let {
            repository.updatePreferences(unitPreferences = unitPreferences, vehicleId = it.id)
        }
            ?: throw VehicleException.NoCurrentVehicleException() //maybe i can find better Exception name
    }

}