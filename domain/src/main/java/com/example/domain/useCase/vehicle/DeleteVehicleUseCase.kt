package com.example.domain.useCase.vehicle

import com.example.domain.VehicleException
import com.example.domain.repository.VehiclesRepository
import kotlinx.coroutines.flow.first

class DeleteVehicleUseCase(
    private val repository: VehiclesRepository
) {
    suspend operator fun invoke(
        vehicleId: Long,
    ) {
        if (vehicleId == repository.observeCurrentVehicle()
                .first()?.id
        ) throw VehicleException.IsCurrentVehicleException()
        repository.deleteVehicle(
            vehicleId = vehicleId
        )
    }
}