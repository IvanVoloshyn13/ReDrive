package voloshyn.android.data.repository.tabs

import android.database.sqlite.SQLiteException
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import voloshyn.android.data.dataSource.datastorePreferences.PreferencesKeys
import voloshyn.android.data.dataSource.room.dao.VehiclesDao
import voloshyn.android.data.dataSource.room.entities.VehicleEntity
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.safeDbCall
import voloshyn.android.data.safeDbCallWithReturn
import voloshyn.android.data.safeUpdateData
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.domain.models.tabs.redrive.VehicleTuple
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
            val vehicleId = vehiclesDao.add(
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
        safeUpdateData {
            dataStore.edit {
                it[PreferencesKeys.REMEMBER_VEHICLE] = setOf("$vehicleId", name)
            }
        }
    }


    override fun currentVehicle(): Flow<AppResult<VehicleTuple, DataError.Locale>> {
        val defaultValue = VehicleTuple(0L, "Please add vehicle")
        var vehicle = defaultValue
        return try {
            dataStore.data.map {
                val vehicleSet = it[PreferencesKeys.REMEMBER_VEHICLE]
                if (vehicleSet != null) let {
                    vehicleSet.toList().apply {
                        vehicle = VehicleTuple(id = this[0].toLong(), name = this[1])
                    }
                }
                AppResult.Success(data = vehicle)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            val appError = when (exception) {
                is SQLiteException -> DataError.Locale.STORAGE_ERROR
                is NullPointerException -> DataError.Locale.DATA_NOT_FOUND
                else -> DataError.Locale.UNKNOWN_ERROR
            }
            flow { emit(AppResult.Error(error = appError)) }
        }

    }




    override fun vehicles(): Flow<AppResult<List<Vehicle>, DataError.Locale>> {
        return try {
            vehiclesDao.getAll()
                .map { vehicleEntities ->
                    val vehicles = vehicleEntities.map { vehicleEntity ->
                        Vehicle(
                            id = vehicleEntity.id,
                            name = vehicleEntity.name,
                            currentMileage = vehicleEntity.currentMileage
                        )
                    }
                    AppResult.Success(data = vehicles)
                }
        } catch (exception: Exception) {
            exception.printStackTrace()
            val appError = when (exception) {
                is SQLiteException -> DataError.Locale.STORAGE_ERROR
                is NullPointerException -> DataError.Locale.DATA_NOT_FOUND
                else -> DataError.Locale.UNKNOWN_ERROR
            }
            flow { emit(AppResult.Error(error = appError)) }
        }

    }

}






