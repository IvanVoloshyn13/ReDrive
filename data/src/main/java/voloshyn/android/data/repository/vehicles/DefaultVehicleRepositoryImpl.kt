package voloshyn.android.data.repository.vehicles

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
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.localeStorage.datastorePreferences.PreferencesKeys
import voloshyn.android.data.localeStorage.room.dao.VehiclesDao
import voloshyn.android.data.localeStorage.room.entities.VehicleEntity
import voloshyn.android.data.mappers.throwAppException
import voloshyn.android.domain.models.Vehicle
import javax.inject.Inject


class DefaultVehicleRepositoryImpl @Inject constructor(
    @DispatcherIo private val ioDispatcher: CoroutineDispatcher,
    private val vehiclesDao: VehiclesDao,
    private val dataStore: DataStore<Preferences>,
) : DefaultVehicleRepository {

    private var _defaultVehicle = Vehicle.DEFAULT_VEHICLE

    override val defaultVehicle: Vehicle
        get() = _defaultVehicle

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeDefaultVehicle(): Flow<Vehicle> {
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
                _defaultVehicle = vehicleEntity.toVehicle()
                _defaultVehicle
            }.catch { exception ->
                exception.throwAppException()
            }.flowOn(ioDispatcher)
    }

    override suspend fun setDefaultVehicle(vehicleId: Long) {
        dataStore.edit {
            it[PreferencesKeys.REMEMBER_VEHICLE] = vehicleId
        }
    }
}