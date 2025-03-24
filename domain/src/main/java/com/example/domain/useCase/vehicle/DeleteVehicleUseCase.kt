package com.example.domain.useCase.vehicle

import com.example.domain.VehicleException
import com.example.domain.repository.VehiclesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Delete vehicle and all related data for the currently signed-in user.
 *
 * @param vehicleId The vehicleId to be deleted.
 * @throws VehicleException.IsCurrentVehicleException If vehicle user is trying to delete is
 * set as current
 */
class DeleteVehicleUseCase  @Inject constructor(
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