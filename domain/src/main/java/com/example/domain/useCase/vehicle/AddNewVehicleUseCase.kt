package com.example.domain.useCase.vehicle

import com.example.domain.UserException
import com.example.domain.model.Vehicle
import com.example.domain.repository.UnitPreferencesRepository
import com.example.domain.repository.UserSessionRepository
import com.example.domain.repository.VehiclesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Adds a new vehicle to the database for the currently signed-in user
 * and set it as current.
 *
 * @param vehicle The vehicle to be added.
 * @throws UserException.NoUserDetectedException If no user is currently signed in but an attempt
 * is made to add a vehicle.
 */
class AddNewVehicleUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository,
    private val vehiclesRepository: VehiclesRepository,
    private val unitPreferencesRepository: UnitPreferencesRepository
) {
    suspend operator fun invoke(vehicle: Vehicle) {
        val uUid = userSessionRepository.observeCurrentUserId().first()
        if (uUid.isNullOrEmpty()) throw UserException.NoUserDetectedException()
        val defaultSettings = unitPreferencesRepository.getDefaultUnitPreferences()
        val vehicleId = vehiclesRepository.saveVehicleWithSettings(
            uUid = uUid,
            vehicle = vehicle,
            unitPreferences = defaultSettings
        )
        vehiclesRepository.setVehicleAsCurrent(vehicleId)
    }

}