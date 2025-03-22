package com.example.domain.useCase.vehicle

import com.example.domain.VehicleException
import com.example.domain.model.Vehicle
import com.example.domain.repository.UserSessionRepository
import com.example.domain.repository.VehiclesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf


class ObserveVehiclesUseCase(
    private val repository: VehiclesRepository,
    private val userSessionRepository: UserSessionRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun invoke(): Flow<List<Vehicle>> {
        return userSessionRepository.observeCurrentUserId().flatMapLatest { userId ->
            if (!userId.isNullOrEmpty()) {
                val vehiclesFlow = repository.observeVehicles(userId)
                val currentVehicleFlow = repository.observeCurrentVehicle()

                combine(
                   vehiclesFlow,
                   currentVehicleFlow
                ) { vehicles, currentVehicle ->
                    if (vehicles.isEmpty()) {
                        return@combine emptyList() // If no vehicles exist, return an empty list
                    }
                    vehicles.map { vehicle ->
                        Vehicle(
                            id = vehicle.id,
                            name = vehicle.name,
                            initialOdometerValue = vehicle.initialOdometerValue,
                            type = vehicle.type,
                            isCurrentVehicle = currentVehicle?.id == vehicle.id
                        )
                    }.sortedByDescending {
                        it.isCurrentVehicle
                    }
                }
            } else {
                flowOf(emptyList())
            }
        }
    }

}