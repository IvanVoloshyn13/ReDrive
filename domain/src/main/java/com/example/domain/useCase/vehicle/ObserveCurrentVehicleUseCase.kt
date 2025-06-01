package com.example.domain.useCase.vehicle

import com.example.domain.ObserveCurrentUserId
import com.example.domain.model.Vehicle
import com.example.domain.repository.UserSessionRepository
import com.example.domain.repository.VehiclesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveCurrentVehicleUseCase @Inject constructor(
    private val vehiclesRepository: VehiclesRepository,
    private val observeCurrentUserId: ObserveCurrentUserId
) {

    operator fun invoke(): Flow<Vehicle?> {
        return observeCurrentUserId {
            vehiclesRepository.observeCurrentVehicle()
        }
    }

}