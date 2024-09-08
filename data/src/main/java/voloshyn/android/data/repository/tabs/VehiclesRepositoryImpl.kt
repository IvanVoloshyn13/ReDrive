package voloshyn.android.data.repository.tabs

import android.database.sqlite.SQLiteException
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.datastorePreferences.PreferencesKeys
import voloshyn.android.data.dataSource.room.dao.VehiclesDao
import voloshyn.android.data.dataSource.room.entities.VehicleEntity
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.safeLocalCall
import voloshyn.android.data.safeLocaleCallWithReturn
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.domain.repository.tabs.VehiclesRepository
import javax.inject.Inject

class VehiclesRepositoryImpl @Inject constructor(
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher,
    private val vehiclesDao: VehiclesDao,
    private val dataStore: DataStore<Preferences>
) : VehiclesRepository {
    override suspend fun addVehicle(
        vehicle: Vehicle,
        accountId: String?
    ): AppResult<Boolean, DataError.Locale> {
        return safeDbCallWithReturn(dispatcherIo) {
          val  vehicleId = vehiclesDao.add(
                VehicleEntity(
                    id = 0,
                    accountId = accountId,
                    name = vehicle.name,
                    currentMileage = vehicle.currentMileage
                )
            )
            vehicleId != -1L
        }
    }

    override suspend fun deleteVehicle(id: Long) {
        safeDbCall(dispatcherIo) {
            vehiclesDao.delete(id)
        }
    }


    override suspend fun rememberVehicle(vehicleId: Long, name: String) {
        safeLocalCall {
            dataStore.edit {
                it[PreferencesKeys.REMEMBER_VEHICLE] = setOf("$vehicleId", name)
            }
        }
    }

    override suspend fun getRememberedVehicle(): AppResult<Set<String>, DataError.Locale> {
        val defaultValue = setOf("0", "Please add vehicle")
        return safeLocaleCallWithReturn(defaultValue = defaultValue) {
            val data = dataStore.data.map {
                it[PreferencesKeys.REMEMBER_VEHICLE] ?: defaultValue
            }
            data.first()
        }
    }

    override fun vehicles(): Flow<AppResult<List<Vehicle>, DataError.Locale>> =
        channelFlow<AppResult<List<Vehicle>, DataError.Locale>> {
            try {
                vehiclesDao.getAll().collectLatest {
                    if (it.isNotEmpty()) {
                        val vehicles = it.mapIndexed { index, vehicleEntity ->
                            Vehicle(
                                id = vehicleEntity.id,
                                currentMileage = vehicleEntity.currentMileage,
                                name = vehicleEntity.name
                            )
                        }
                        send(AppResult.Success(data = vehicles))
                    } else {
                        send(AppResult.Success(data = Vehicle.NULL))
                    }
                }
            } catch (e: SQLiteException) {
                e.printStackTrace()
                send(AppResult.Error(error = DataError.Locale.STORAGE_ERROR))
            } catch (e: NullPointerException) {
                e.printStackTrace()
                send(AppResult.Error(error = DataError.Locale.DATA_NOT_FOUND))
            } catch (e: Exception) {
                e.printStackTrace()
                send(AppResult.Error(error = DataError.Locale.UNKNOWN_ERROR))
            }
        }.flowOn(dispatcherIo)
}