package com.example.domain.useCase.vehicle

import com.example.domain.repository.VehiclesRepository
import javax.inject.Inject

class SetVehicleAsCurrentUseCase @Inject constructor(
    private val repository: VehiclesRepository
) {
    suspend operator fun invoke(vehicleId: Long) {
        repository.setVehicleAsCurrent(vehicleId)
    }
}