package voloshyn.android.data.repository.tabs

import voloshyn.android.data.dataSource.room.dao.RefuelsDao
import voloshyn.android.data.dataSource.room.entities.RefuelEntity
import voloshyn.android.domain.models.tabs.Refuel
import voloshyn.android.domain.repository.tabs.RefuelsRepository
import javax.inject.Inject

class RefuelsRepositoryImpl @Inject constructor(
    private val refuelsDao: RefuelsDao
) : RefuelsRepository {
    override suspend fun addNew(refuel: Refuel) {
        refuelsDao.addRefuel(refuel = RefuelEntity.toEntity(refuel))
    }

    override suspend fun edit() {
        TODO("Not yet implemented")
    }

    override suspend fun delete() {
        TODO("Not yet implemented")
    }

    override suspend fun refuelLogs() {
        TODO("Not yet implemented")
    }
}