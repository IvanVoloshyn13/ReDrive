package com.example.domain.useCase.vehicle

import com.example.domain.VehicleException
import com.example.domain.model.Vehicle
import com.example.domain.repository.UserSessionRepository
import com.example.domain.repository.VehiclesRepository
import com.example.domain.useCase.userSession.ObserveCurrentUserIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class ObserveCurrentVehicleUseCase(
    private val vehiclesRepository: VehiclesRepository,
    private val userSessionRepository: UserSessionRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Vehicle?> {
        return try {
            userSessionRepository.observeCurrentUserId().flatMapLatest {
                if (!it.isNullOrEmpty()) {
                    return@flatMapLatest vehiclesRepository.observeCurrentVehicle()
                } else {
                    flowOf(null)
                }
            }
        } catch (e: VehicleException.NoCurrentVehicleException) {
            flowOf(Vehicle.NO_VEHICLE)
        }
    }

}