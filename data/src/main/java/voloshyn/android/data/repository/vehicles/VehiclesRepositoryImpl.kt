package voloshyn.android.data.repository.vehicles

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.localeStorage.datastorePreferences.PreferencesKeys
import voloshyn.android.data.localeStorage.room.dao.VehiclesDao
import voloshyn.android.data.localeStorage.room.entities.VehicleEntity
import voloshyn.android.data.mappers.throwAppException
import voloshyn.android.domain.IsCurrentVehicleException
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.VehicleType
import voloshyn.android.domain.repository.VehiclesRepository
import javax.inject.Inject


const val NON_VEHICLE_KEY_VALUE = -1L

class VehiclesRepositoryImpl @Inject constructor(
    @DispatcherIo private val ioDispatcher: CoroutineDispatcher,
    private val vehiclesDao: VehiclesDao,
    private val dataStore: DataStore<Preferences>
) : VehiclesRepository {

    private var _currentVehicle = Vehicle.DEFAULT_VEHICLE
    override val currentVehicle: Vehicle
        get() = _currentVehicle

    override suspend fun addNewVehicle(
        uUid: String, vehicle: Vehicle
    ): Long {
        return withContext(ioDispatcher) {

            val vehicleId = vehiclesDao.add(
                VehicleEntity(
                    id = 0,
                    userId = uUid,
                    name = vehicle.name,
                    initialOdometerValue = vehicle.initialOdometerValue,
                    vehicleType = vehicle.type.name
                )
            )
            vehicleId
        }
    }

    override suspend fun updateVehicle(uUid: String, vehicle: Vehicle) {
        vehiclesDao.update(
            VehicleEntity(
                id = vehicle.id,
                userId = uUid,
                name = vehicle.name,
                initialOdometerValue = vehicle.initialOdometerValue,
                vehicleType = vehicle.type.name
            )
        )
    }

    override suspend fun deleteVehicle(uUid: String, vehicleId: Long) {
        vehiclesDao.delete(vehicleId)
    }

    override suspend fun setVehicleAsCurrent(vehicleId: Long) {
        dataStore.edit {
            it[PreferencesKeys.REMEMBER_VEHICLE] = vehicleId
        }
    }

    override fun observeCurrentVehicle(): Flow<Vehicle> {
        return dataStore.data.map {
            val vehicleId = it[PreferencesKeys.REMEMBER_VEHICLE] ?: NON_VEHICLE_KEY_VALUE

//        Here is multiple invoke because of lifecycle i think TODO()
            vehicleId

        }.distinctUntilChanged()
            .flatMapLatest {
                if (it != NON_VEHICLE_KEY_VALUE) {
                    vehiclesDao.currentVehicle(it)
                } else flowOf(VehicleEntity.emptyVehicle)
            }.map { vehicleEntity ->
                _currentVehicle = vehicleEntity.toVehicle()
                _currentVehicle
            }.catch { exception ->
                exception.throwAppException()
            }.flowOn(ioDispatcher)
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









