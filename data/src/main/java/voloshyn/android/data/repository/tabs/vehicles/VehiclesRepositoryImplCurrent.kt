package voloshyn.android.data.repository.tabs.vehicles

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.localeStorage.datastorePreferences.PreferencesKeys
import voloshyn.android.data.localeStorage.room.dao.VehiclesDao
import voloshyn.android.data.localeStorage.room.entities.VehicleEntity
import voloshyn.android.data.mappers.throwAppException
import voloshyn.android.data.repository.AppCurrentUserRepository
import voloshyn.android.data.repository.CurrentVehicleProvider
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.domain.models.VehicleType
import voloshyn.android.domain.repository.tabs.VehiclesRepository
import javax.inject.Inject

const val NON_VEHICLE_KEY_VALUE = -1L

class VehiclesRepositoryImplCurrent @Inject constructor(
    @DispatcherIo private val ioDispatcher: CoroutineDispatcher,
    private val vehiclesDao: VehiclesDao,
    private val dataStore: DataStore<Preferences>,
    private val appCurrentUserRepository: AppCurrentUserRepository
) : VehiclesRepository, CurrentVehicleProvider,
    AppCurrentUserRepository by appCurrentUserRepository {

    override val currentVehicle = getVehicle()

    override suspend fun saveNewVehicle(
        vehicle: Vehicle,
        accountId: String
    ): Long {
        val userId = appCurrentUserRepository.user.id
        val id = vehiclesDao.add(
            VehicleEntity(
                id = 0,
                userId = userId,
                name = vehicle.name,
                currentMileage = vehicle.currentMileage,
                vehicleType = vehicle.type.name
            )
        )
        return id
    }


    override suspend fun deleteVehicle(id: Long) {
        vehiclesDao.delete(id)
    }

    override suspend fun rememberVehicle(vehicleId: Long) {
        dataStore.edit {
            it[PreferencesKeys.REMEMBER_VEHICLE] = vehicleId
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getVehicle(): Flow<Vehicle> {
        return dataStore.data.map {
            val vehicleId = it[PreferencesKeys.REMEMBER_VEHICLE] ?: NON_VEHICLE_KEY_VALUE

//        Here is multiple invoke because of lifecycle i think TODO()
            vehicleId

        }.distinctUntilChanged()
            .flatMapLatest {
                if (it != NON_VEHICLE_KEY_VALUE) {
                    vehiclesDao.currentVehicle(it)
                } else flowOf(VehicleEntity.emptyVehicle)
            }.mapNotNull { vehicleEntity ->
                vehicleEntity.toVehicle()
            }.catch { exception ->
                exception.throwAppException()
            }.flowOn(ioDispatcher)
    }

    override fun currentVehicle(): Flow<Vehicle> {
        return getVehicle()
    }

    override fun vehicles(): Flow<List<Vehicle>> {
        return vehiclesDao.getAll()
            .map { vehicleEntities ->
                val vehicles = vehicleEntities.map { vehicleEntity ->
                    Vehicle(
                        id = vehicleEntity.id,
                        name = vehicleEntity.name,
                        currentMileage = vehicleEntity.currentMileage,
                        type = VehicleType.valueOf(vehicleEntity.vehicleType)
                    )
                }
                vehicles
            }.catch { exception ->
                exception.throwAppException()
            }
    }

    override suspend fun isVehicle(userId: String): Boolean {
        return vehiclesDao.isVehicle(userId = userId)
    }

}








