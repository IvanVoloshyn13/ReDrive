package com.example.domain.useCase.settings

import com.example.domain.model.Settings
import com.example.domain.repository.SettingsRepository
import com.example.domain.useCase.vehicle.ObserveCurrentVehicleUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
    private val observeCurrentVehicleUseCase: ObserveCurrentVehicleUseCase,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Settings> {
        return observeCurrentVehicleUseCase.invoke().flatMapLatest {
            return@flatMapLatest it?.let { vehicle ->
                repository.observeAppSettings(vehicle.id)
            } ?: flowOf(repository.getDefaultSettings())
        }

    }
}