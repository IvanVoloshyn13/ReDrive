package voloshyn.android.data.repository.tabs

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import voloshyn.android.data.dataSource.room.dao.AccountsDao
import voloshyn.android.data.dataSource.room.dao.VehiclesDao
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.domain.LocalStorageException
import voloshyn.android.domain.appResult.AppResult
import voloshyn.android.domain.appResult.DataError
import voloshyn.android.domain.models.tabs.profile.UserTuple
import voloshyn.android.domain.models.tabs.redrive.LastRefuel
import voloshyn.android.domain.models.tabs.redrive.RefuelsTuple
import voloshyn.android.domain.repository.tabs.RedriveRepository
import javax.inject.Inject

class RedriveRepositoryImpl @Inject constructor(
    @DispatcherIo private val dispatcher: CoroutineDispatcher,
    private val accountsDao: AccountsDao,
    private val vehiclesDao: VehiclesDao,

) : RedriveRepository {


    override suspend fun currentUser(email: String): AppResult<UserTuple, DataError.Locale> {
        TODO("Not yet implemented")
    }


    override suspend fun allTimeAvgConsumption(): AppResult<List<Double>, DataError.Locale> {
        TODO("Not yet implemented")
    }

    override suspend fun costPerKm(): AppResult<List<Double>, DataError.Locale> {
        TODO("Not yet implemented")
    }

    override suspend fun costPerMiles(): AppResult<Double, DataError.Locale> {
        TODO("Not yet implemented")
    }

    override suspend fun lastRefuel(): AppResult<LastRefuel, DataError.Locale> {
        TODO("Not yet implemented")
    }

    override suspend fun summary(): AppResult<RefuelsTuple, DataError.Locale> {
        TODO("Not yet implemented")
    }


}

