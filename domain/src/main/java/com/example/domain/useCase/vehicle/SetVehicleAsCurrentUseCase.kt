package com.example.domain.useCase.vehicle

import com.example.domain.repository.VehiclesRepository

class SetVehicleAsCurrentUseCase(private val repository: VehiclesRepository) {
    suspend operator fun invoke(vehicleId: Long) {
        repository.setVehicleAsCurrent(vehicleId)
    }
}