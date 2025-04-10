package com.example.domain.useCase.settings

import com.example.domain.repository.SettingsRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetDateFormatPatternUseCase @Inject constructor(
    private val repository: SettingsRepository,
    private val observeCurrentVehicleUseCase: ObserveCurrentVehicleUseCase
) {
    suspend operator fun invoke(): String {
        val vehicle = observeCurrentVehicleUseCase.invoke().first()
        return repository.getDateFormatPattern(vehicle?.id)
    }
}