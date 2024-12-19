package voloshyn.android.data.repository.vehicles

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.localeStorage.room.dao.VehiclesDao
import voloshyn.android.data.localeStorage.room.entities.VehicleEntity
import voloshyn.android.data.mappers.throwAppException
import voloshyn.android.data.repository.user.AppCurrentUserRepository
import voloshyn.android.domain.IsDefaultVehicleException
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.VehicleType
import voloshyn.android.domain.repository.VehiclesRepository
import javax.inject.Inject


const val NON_VEHICLE_KEY_VALUE = -1L

class VehiclesRepositoryImpl @Inject constructor(
    @DispatcherIo private val ioDispatcher: CoroutineDispatcher,
    private val vehiclesDao: VehiclesDao,
    private val appCurrentUserRepository: AppCurrentUserRepository,
    private val defaultVehicleRepository: DefaultVehicleRepository
) : VehiclesRepository, AppCurrentUserRepository by appCurrentUserRepository,
    DefaultVehicleRepository by defaultVehicleRepository {

    override suspend fun addNewVehicle(
        vehicle: Vehicle
    ): Long {
        return withContext(ioDispatcher) {
            val userId = appCurrentUserRepository.user.id
            val vehicleId = vehiclesDao.add(
                VehicleEntity(
                    id = 0,
                    userId = userId,
                    name = vehicle.name,
                    initialOdometerValue = vehicle.initialOdometerValue,
                    vehicleType = vehicle.type.name
                )
            )
            vehicleId
        }
    }

    override suspend fun updateVehicle(vehicle: Vehicle) {
        val userId = appCurrentUserRepository.user.id
        vehiclesDao.update(
            VehicleEntity(
                id = vehicle.id,
                userId = userId,
                name = vehicle.name,
                initialOdometerValue = vehicle.initialOdometerValue,
                vehicleType = vehicle.type.name
            )
        )
    }


    override suspend fun deleteVehicle(vehicleId: Long) {
        if (isDefault(vehicleId)) throw IsDefaultVehicleException()
        vehiclesDao.delete(vehicleId)
    }

    override suspend fun setVehicleAsDefault(vehicleId: Long) {
        setDefaultVehicle(vehicleId)
    }

    override fun observeDefaultVehicle(): Flow<Vehicle> {
        return defaultVehicleRepository.observeDefaultVehicle()
    }

    override fun observeVehicles(): Flow<List<Vehicle>> {
        return vehiclesDao.getAll()
            .map { vehicleEntities ->
                val vehicles = vehicleEntities.map { vehicleEntity ->
                    Vehicle(
                        id = vehicleEntity.id,
                        name = vehicleEntity.name,
                        initialOdometerValue = vehicleEntity.initialOdometerValue,
                        type = VehicleType.valueOf(vehicleEntity.vehicleType)
                    )
                }
                vehicles
            }.catch { exception ->
                exception.throwAppException()
            }
    }

    override suspend fun isVehicle(uuid: String): Boolean {
        return vehiclesDao.isVehicle(uUid = uuid)
    }


}









