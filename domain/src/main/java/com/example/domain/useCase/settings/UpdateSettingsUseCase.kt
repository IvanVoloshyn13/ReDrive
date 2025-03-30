package com.example.domain.useCase.settings

import com.example.domain.VehicleException
import com.example.domain.model.Settings
import com.example.domain.repository.SettingsRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
    private val observeCurrentVehicleUseCase: ObserveCurrentVehicleUseCase
) {
    suspend operator fun invoke(settings: Settings) {
        val vehicle = observeCurrentVehicleUseCase.invoke().first()
        vehicle?.let {
            repository.updateSettings(settings = settings, vehicleId = it.id)
        }
            ?: throw VehicleException.NoCurrentVehicleException() //maybe i can find better Exception name
    }

}