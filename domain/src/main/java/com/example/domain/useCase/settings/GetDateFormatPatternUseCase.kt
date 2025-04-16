package com.example.domain.useCase.settings

import com.example.domain.repository.UnitPreferencesRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetDateFormatPatternUseCase @Inject constructor(
    private val repository: UnitPreferencesRepository,
    private val observeCurrentVehicleUseCase: ObserveCurrentVehicleUseCase
) {
    suspend operator fun invoke(): String {
        val vehicle = observeCurrentVehicleUseCase.invoke().first()
        return repository.getCurrentDateFormatPattern(vehicle?.id)
    }
}