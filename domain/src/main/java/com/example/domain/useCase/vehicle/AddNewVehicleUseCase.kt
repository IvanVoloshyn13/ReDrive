package com.example.domain.useCase.vehicle

import com.example.domain.UserException
import com.example.domain.model.Vehicle
import com.example.domain.repository.UserSessionRepository
import com.example.domain.repository.VehiclesRepository
import kotlinx.coroutines.flow.first

class AddNewVehicleUseCase(
    private val userSessionRepository: UserSessionRepository,
    private val vehiclesRepository: VehiclesRepository
) {

    suspend operator fun invoke(vehicle: Vehicle) {
        try {
            val uUid = userSessionRepository.observeCurrentUserId().first()
            if (!uUid.isNullOrEmpty()) {
                val vehicleId = vehiclesRepository.addNewVehicle(uUid = uUid, vehicle = vehicle)
                vehiclesRepository.setVehicleAsCurrent(vehicleId)
            } else throw UserException.NoUserDetectedException()
        } catch (e: UserException.NoUserDetectedException) {
            throw e
        }
    }

}