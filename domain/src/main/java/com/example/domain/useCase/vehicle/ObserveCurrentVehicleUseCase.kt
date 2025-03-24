package com.example.domain.useCase.vehicle

import com.example.domain.VehicleException
import com.example.domain.model.Vehicle
import com.example.domain.repository.UserSessionRepository
import com.example.domain.repository.VehiclesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveCurrentVehicleUseCase  @Inject constructor(
    private val vehiclesRepository: VehiclesRepository,
    private val userSessionRepository: UserSessionRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Vehicle?> {
        return userSessionRepository.observeCurrentUserId().flatMapLatest {
                if (!it.isNullOrEmpty()) {
                    return@flatMapLatest vehiclesRepository.observeCurrentVehicle()
                } else {
                    flowOf(null)
                }
            }

    }

}