package voloshyn.android.domain.repository.tabs

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.logs.RefuelLog
import voloshyn.android.domain.models.refuel.Refuel
import voloshyn.android.domain.models.refuel.RefuelTuple

interface RefuelRepository {

    suspend fun addNew(refuel: Refuel)
    suspend fun edit(refuel: Refuel)
    suspend fun delete(refuelId:Long)
    suspend fun refuels():Flow<Refuel>
     fun refuelTuple():Flow<RefuelTuple>
    fun lastRefuelLog(): Flow<RefuelLog>


   // suspend fun costPerDistance(): List<Double>

}

