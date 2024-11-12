package voloshyn.android.data.repository.tabs.refuel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.localeStorage.room.dao.RefuelsDao
import voloshyn.android.data.localeStorage.room.entities.RefuelEntity
import voloshyn.android.data.repository.tabs.logs.RefuelsProvider
import voloshyn.android.domain.models.logs.RefuelLog
import voloshyn.android.domain.models.refuel.Refuel
import voloshyn.android.domain.models.refuel.RefuelTuple
import voloshyn.android.domain.repository.tabs.RefuelRepository
import javax.inject.Inject

class RefuelRepositoryImpl @Inject constructor(
    @DispatcherIo private val dispatcherIo: CoroutineDispatcher,
    private val refuelsDao: RefuelsDao,
) : RefuelRepository, RefuelsProvider {

    override fun refuelsForVehicle(vehicleId: Long): Flow<List<Refuel>> {
        return refuelsDao.getRefuels(vehicleId).map { entityList ->
            entityList.map { refuelEntity ->
                refuelEntity.toRefuel()
            }
        }.flowOn(dispatcherIo)
    }

    override suspend fun addNew(refuel: Refuel) {
        refuelsDao.addRefuel(refuel = RefuelEntity.toEntity(refuel))
    }

    override suspend fun edit(refuel: Refuel) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(refuelId: Long) {
        TODO("Not yet implemented")
    }


    override suspend fun refuels(): Flow<Refuel> {
        TODO()
    }

    override fun refuelTuple(): Flow<RefuelTuple> {
        TODO("Not yet implemented")
    }

    override fun lastRefuelLog(): Flow<RefuelLog> {
        TODO("Not yet implemented")
    }




}