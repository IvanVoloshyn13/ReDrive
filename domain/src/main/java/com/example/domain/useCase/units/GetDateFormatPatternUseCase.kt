package com.example.domain.useCase.units

import com.example.domain.repository.VehicleUnitPreferencesRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetDateFormatPatternUseCase @Inject constructor(
    private val repository: VehicleUnitPreferencesRepository,
    private val observeCurrentVehicleUseCase: ObserveCurrentVehicleUseCase
) {
    suspend operator fun invoke(): String {
        val vehicle = observeCurrentVehicleUseCase.invoke().first()
        return repository.getCurrentDateFormatPattern(vehicle?.id)
    }
}