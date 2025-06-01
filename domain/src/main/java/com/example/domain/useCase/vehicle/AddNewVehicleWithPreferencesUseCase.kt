package com.example.domain.useCase.vehicle

import com.example.domain.UserException
import com.example.domain.model.Vehicle
import com.example.domain.repository.VehicleUnitPreferencesRepository
import com.example.domain.repository.UserSessionRepository
import com.example.domain.repository.VehiclesRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

/**
 * Adds a new vehicle with default units to the database for the currently signed-in user
 * and set it as current.
 */
class AddNewVehicleWithPreferencesUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository,
    private val vehiclesRepository: VehiclesRepository,
    private val vehicleUnitPreferencesRepository: VehicleUnitPreferencesRepository
) {

    /**
     * @param vehicle The vehicle to be added.
     * @throws UserException.NoUserDetectedException If no user is currently signed in but an attempt
     * is made to add a vehicle.
     */
    suspend operator fun invoke(vehicle: Vehicle) {
        val uUid = userSessionRepository.observeCurrentUserId().first()
        if (uUid.isNullOrEmpty()) throw UserException.NoUserDetectedException()
        val defaultSettings = vehicleUnitPreferencesRepository.getDefaultUnitPreferences()
        val vehicleId = UUID.randomUUID().toString()
        vehiclesRepository.saveVehicle(
            userId = uUid,
            vehicle = vehicle.copy(id = vehicleId)
        )
        vehicleUnitPreferencesRepository.saveUnitPreferences(vehicleId, defaultSettings)
        vehiclesRepository.setVehicleAsCurrent(vehicleId)
    }
}
