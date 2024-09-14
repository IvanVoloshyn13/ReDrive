package voloshyn.android.data.repository.tabs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.datastorePreferences.PreferencesKeys
import voloshyn.android.data.dataSource.room.dao.VehiclesDao
import voloshyn.android.data.dataSource.room.entities.VehicleEntity
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.mappers.throwAppException
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.domain.models.tabs.redrive.VehicleTuple
import voloshyn.android.domain.repository.tabs.VehiclesRepository
import javax.inject.Inject


private const val GET_VEHICLE_ID = 0
private const val GET_VEHICLE_NAME = 1

class VehiclesRepositoryImpl @Inject constructor(
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher,
    private val vehiclesDao: VehiclesDao,
    private val dataStore: DataStore<Preferences>
) : VehiclesRepository {

    override suspend fun addVehicle(
        vehicle: Vehicle,
        accountId: String?
    ) {
        vehiclesDao.add(
            VehicleEntity(
                id = 0,
                accountId = accountId,
                name = vehicle.name,
                currentMileage = vehicle.currentMileage
            )
        )
    }

    override suspend fun deleteVehicle(id: Long) {
        vehiclesDao.delete(id)
    }


    override suspend fun rememberVehicle(vehicleId: Long, name: String) {
        dataStore.edit {
            it[PreferencesKeys.REMEMBER_VEHICLE] = setOf("$vehicleId", name)
        }
    }


    override fun currentVehicle(): Flow<VehicleTuple> {
        val defaultValue = VehicleTuple(0L, "Please add vehicle")
        var vehicle = defaultValue
        return dataStore.data.map {
            val vehicleSet = it[PreferencesKeys.REMEMBER_VEHICLE]
            if (vehicleSet != null) let {
                vehicleSet.toList().apply {
                    vehicle = VehicleTuple(
                        id = this[GET_VEHICLE_ID].toLong(),
                        name = this[GET_VEHICLE_NAME]
                    )
                }
            }
            vehicle
        }.catch { exception ->
            exception.throwAppException()
        }

    }


    override fun vehicles(): Flow<List<Vehicle>> {
        return vehiclesDao.getAll()
            .map { vehicleEntities ->

                val vehicles = vehicleEntities.map { vehicleEntity ->
                    Vehicle(
                        id = vehicleEntity.id,
                        name = vehicleEntity.name,
                        currentMileage = vehicleEntity.currentMileage
                    )
                }
                vehicles
            }.catch { exception ->
                exception.throwAppException()
            }

    }

}








