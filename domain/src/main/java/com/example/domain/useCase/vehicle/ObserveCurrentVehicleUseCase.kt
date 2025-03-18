package com.example.domain.useCase.vehicle

import com.example.domain.model.Vehicle
import com.example.domain.repository.VehiclesRepository
import com.example.domain.useCase.userSession.ObserveCurrentUserIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class ObserveCurrentVehicleUseCase(
    private val vehiclesRepository: VehiclesRepository,
    private val currentUserIdUseCase: ObserveCurrentUserIdUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Vehicle> {
        TODO()
    }

}