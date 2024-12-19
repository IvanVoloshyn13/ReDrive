package voloshyn.android.domain.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.logs.RefuelLog
import voloshyn.android.domain.models.refuel.Refuel
import voloshyn.android.domain.models.refuel.RefuelTuple

interface RefuelRepository {

    suspend fun addNew(refuel: Refuel)


    suspend fun edit(refuel: Refuel)
    suspend fun delete(refuelId: Long)
    suspend fun observe(): Flow<Refuel>
    fun refuelTuple(): Flow<RefuelTuple>
    fun observeLastRefuelLog(): Flow<RefuelLog>


    // suspend fun costPerDistance(): List<Double>

}

